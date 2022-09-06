package com.libowen.diaocha.repository;

import com.libowen.diaocha.util.DBUtil;
import com.libowen.diaocha.util.Log;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class ExamRepo {
    @SneakyThrows
    public void insert(String aid, String nickname, String phone, String answerJson) {
        String sql="insert into results (aid,nickname,phone,answer) values (?,?,?,?)";
        try(Connection c = DBUtil.connection()){
            try(PreparedStatement ps = c.prepareStatement(sql)){
                ps.setString(1,aid);
                ps.setString(2,nickname);
                ps.setString(3,phone);
                ps.setString(4,answerJson);

                Log.println("执行sql： "+ps);

                ps.executeUpdate();
            }
        }
    }
}
