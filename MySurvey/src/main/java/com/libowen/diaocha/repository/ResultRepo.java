package com.libowen.diaocha.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.libowen.diaocha.util.DBUtil;
import com.libowen.diaocha.util.Log;
import com.libowen.diaocha.view_object.ResultView;
import com.libowen.diaocha.view_object.UserVO;
import lombok.Data;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ResultRepo {
    @Data
    public static class OptionCount {
        public String option;
        public Integer count;

        public OptionCount(String option) {
            this.option = option;
            this.count = 0;
        }
    }

    @Data
    public static class Question {
        public Integer qid;
        public String question;
        public OptionCount[] options = new OptionCount[4];

        @SneakyThrows
        public Question(ResultSet rs, ObjectMapper objectMapper) {
            this.qid = rs.getInt("qid");
            this.question = rs.getString("question");
            List<String> optionList = objectMapper.readValue(rs.getString("options"), new TypeReference<List<String>>() {});
            this.options[0] = new OptionCount(optionList.get(0));
            this.options[1] = new OptionCount(optionList.get(1));
            this.options[2] = new OptionCount(optionList.get(2));
            this.options[3] = new OptionCount(optionList.get(3));
        }
    }



    @SneakyThrows
    public void select(UserVO currentUser , String aid, ResultView resultView, ObjectMapper objectMapper, Map<Integer, Question> qidToQuestionMap){
        try (Connection c = DBUtil.connection()) {
            {
                String sql = "select aid, started_at, ended_at, a.sid, title, brief from activities a join surveys s on a.sid = s.sid where a.uid = ? and aid = ?";
                try (PreparedStatement ps = c.prepareStatement(sql)) {
                    ps.setInt(1, currentUser.uid);
                    ps.setString(2, aid);

                    Log.println("执行 SQL: " + ps);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new RuntimeException("aid 对应的调查活动不存在");
                        }

                        resultView.set(rs);
                    }
                }
            }

            List<Integer> qidList = new ArrayList<>();
            List<String>  qidStringList = new ArrayList<>();
            {
                String sql = "select qid from relations where sid = ? order by rid";
                try (PreparedStatement ps = c.prepareStatement(sql)) {
                    ps.setInt(1, resultView.sid);

                    Log.println("执行 SQL: " + ps);

                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            int qid = rs.getInt("qid");
                            qidList.add(qid);
                            qidStringList.add(String.valueOf(qid));
                        }
                    }
                }
            }
            if (qidList.isEmpty()) {
                throw new RuntimeException("问卷完全没有关联任何题目");
            }

            {
                String sqlFormat = "select qid, question, options from questions where qid in (%s)";
                String s = String.join(", ", qidStringList);
                String sql = String.format(sqlFormat, s);

                try (PreparedStatement ps = c.prepareStatement(sql)) {
                    Log.println("执行 SQL: " + ps);
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            Question question = new Question(rs, objectMapper);
                            qidToQuestionMap.put(question.qid, question);
                        }
                    }
                }
            }

            {
                String sql = "select answer from results where aid = ?";
                try (PreparedStatement ps = c.prepareStatement(sql)) {
                    ps.setString(1, aid);

                    Log.println("执行 SQL: " + ps);

                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) { // 第一层循环        N: 循环本次参与调查的人数
                            String answerJson = rs.getString("answer");
                            Map<String, String> answerMap = objectMapper.readValue(answerJson, new TypeReference<Map<String, String>>() {});

                            for (Map.Entry<String, String> entry : answerMap.entrySet()) {  // 第二层循环    M: 本次调查的题目数量
                                String key = entry.getKey();
                                String value = entry.getValue();

                                int qid = Integer.parseInt(key);
                                int index = Integer.parseInt(value);

                                if (!qidToQuestionMap.containsKey(qid)) {
                                    throw new RuntimeException("这个 qid 不存在");
                                }

                                Question question = qidToQuestionMap.get(qid);
                                OptionCount[] options = question.options;
                                OptionCount optionCount = options[index];
                                optionCount.count++;
                            }
                        }
                    }
                }
            }

            Log.println(qidToQuestionMap);

            // qidToQuestionMap -> ResultView
            // qidList 是保留题目的顺序，因为 map 中，把顺序信息丢失了
            resultView.setQuestionList(qidList, qidToQuestionMap);


        }
    }
}
