package com.libowen.diaocha.repository;

import com.libowen.diaocha.data_object.QuestionDO;
import com.libowen.diaocha.data_object.SurveyDO;
import com.libowen.diaocha.util.DBUtil;
import com.libowen.diaocha.util.Log;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SurveyRepo {
    @SneakyThrows
    public void insert(SurveyDO surveyDO) {
        String sql="insert into surveys (uid,title,brief) values (?,?,?)";
        try(Connection c = DBUtil.connection()){
            try(PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
                ps.setInt(1,surveyDO.uid);
                ps.setString(2,surveyDO.title);
                ps.setString(3,surveyDO.brief);

                Log.println("执行sql： "+ps);
                ps.executeUpdate();
                try(ResultSet rs = ps.getGeneratedKeys()){
                    rs.next();
                    surveyDO.sid=rs.getInt(1);
                }

            }
        }
    }
    @SneakyThrows
    public int selectCountByUid(Integer uid) {
        String sql="select count(*) from surveys where uid=?";
        try(Connection c = DBUtil.connection()){
            try(PreparedStatement ps = c.prepareStatement(sql)){
                ps.setInt(1,uid);

                Log.println("执行 SQL: " + ps);
                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    return rs.getInt(1);
                }
            }
        }
    }
    @SneakyThrows
    public List<SurveyDO> selectListByUidLimitOffset(Integer uid, int limit, int offset) {
        String sql = "select sid, uid, title, brief from surveys where uid = ? order by sid desc limit ? offset ?";
        List<SurveyDO> list = new ArrayList<>();
        try (Connection c = DBUtil.connection()) {
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setInt(1, uid);
                ps.setInt(2, limit);
                ps.setInt(3, offset);

                Log.println("执行 SQL: " + ps);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        SurveyDO surveyDO = new SurveyDO(
                                rs.getInt("sid"),
                                rs.getInt("uid"),
                                rs.getString("title"),
                                rs.getString("brief")
                        );
                        list.add(surveyDO);
                    }
                }
            }
        }
        return list;
    }

    @SneakyThrows
    public SurveyDO selectOneBySidAndUid(int sid, int uid) {
        String sql = "select sid, uid, title, brief from surveys where sid = ? and uid = ?";
        try (Connection c = DBUtil.connection()) {
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setInt(1, sid);
                ps.setInt(2, uid);

                Log.println("执行 SQL: " + ps);

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        return null;
                    }

                    return new SurveyDO(
                            rs.getInt("sid"),
                            rs.getInt("uid"),
                            rs.getString("title"),
                            rs.getString("brief")
                    );
                }
            }
        }
    }
    @SneakyThrows
    public List<SurveyDO> selectListByUid(Integer uid) {
        String sql = "select sid, uid, title, brief from surveys where uid = ? order by sid desc";

        List<SurveyDO> list = new ArrayList<>();
        try (Connection c = DBUtil.connection()) {
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setInt(1, uid);

                Log.println("执行 SQL: " + ps);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        SurveyDO surveyDO = new SurveyDO(
                                rs.getInt("sid"),
                                rs.getInt("uid"),
                                rs.getString("title"),
                                rs.getString("brief")
                        );
                        list.add(surveyDO);
                    }
                }
            }
        }
        return list;
    }
}
