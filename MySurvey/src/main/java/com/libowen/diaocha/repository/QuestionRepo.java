package com.libowen.diaocha.repository;

import com.libowen.diaocha.data_object.QuestionDO;
import com.libowen.diaocha.util.DBUtil;
import com.libowen.diaocha.util.Log;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class QuestionRepo {
    @SneakyThrows
    public void insert(QuestionDO questionDO) {
        String sql="insert into questions (uid,question,options) values (?,?,?)";
        try(Connection c = DBUtil.connection()){
            try(PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
                ps.setInt(1,questionDO.uid);
                ps.setString(2,questionDO.question);
                ps.setString(3,questionDO.options);
                Log.println("执行SQL: "+ps);
                ps.executeUpdate();

                try(ResultSet rs = ps.getGeneratedKeys()){
                    rs.next();
                    questionDO.qid = rs.getInt(1);
                }
            }
        }
    }

    @SneakyThrows
    public List<QuestionDO> selectListByUidLimitOffset(Integer uid,Integer limit ,Integer offset) {
        String sql = "select qid, uid, question, options from questions where uid = ? order by qid desc limit ? offset ?";
        List<QuestionDO> list = new ArrayList<>();
        try (Connection c = DBUtil.connection()) {
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setInt(1, uid);
                ps.setInt(2, limit);
                ps.setInt(3, offset);

                Log.println("执行 SQL: " + ps);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        QuestionDO questionDO = new QuestionDO(
                                rs.getInt("qid"),
                                rs.getInt("uid"),
                                rs.getString("question"),
                                rs.getString("options")
                        );
                        list.add(questionDO);
                    }
                }
            }
        }

        return list;
    }
    @SneakyThrows
    public int selectCountByUid(Integer uid) {
        String sql = "select count(*) from questions where uid = ?";
        try (Connection c = DBUtil.connection()) {
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setInt(1, uid);

                Log.println("执行 SQL: " + ps);
                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    return rs.getInt(1);
                }
            }
        }
    }
}
