package com.libowen.diaocha.view_object;

import com.libowen.diaocha.repository.ResultRepo;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ResultView {
    public UserVO currentUser;
    public Integer aid;
    public String startedAt;
    public String endedAt;
    public Integer sid;
    public String title;
    public String brief;
    public List<ResultQuestionView> questionList=new ArrayList<>();

    public ResultView(UserVO currentUser){
        this.currentUser=currentUser;
    }

    @SneakyThrows
    public void set(ResultSet rs) {
        this.aid = rs.getInt("aid");
        this.startedAt = tsToString(rs.getTimestamp("started_at"));
        this.endedAt = tsToString(rs.getTimestamp("ended_at"));
        this.sid = rs.getInt("sid");
        this.title = rs.getString("title");
        this.brief = rs.getString("brief");
    }

    private String tsToString(Timestamp ts) {
        LocalDateTime localDateTime = ts.toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return formatter.format(localDateTime);
    }

    public void setQuestionList(List<Integer> qidList, Map<Integer, ResultRepo.Question> qidToQuestionMap) {

        for (int qid : qidList) {
            ResultRepo.Question question = qidToQuestionMap.get(qid);
            ResultQuestionView view = new ResultQuestionView(question);
            this.questionList.add(view);
        }
    }
}
