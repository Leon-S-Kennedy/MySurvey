package com.libowen.diaocha.repository;

import com.libowen.diaocha.util.DBUtil;
import com.libowen.diaocha.util.Log;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class ActivityRepo {
    @SneakyThrows
    public void insert(Integer uid, String sid, String started_at, String ended_at) {
        String sql = "insert into activities (uid, sid, started_at, ended_at) values (?, ?, ?, ?)";
        try (Connection c = DBUtil.connection()) {
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setInt(1, uid);
                ps.setString(2, sid);
                ps.setString(3, started_at);
                ps.setString(4, ended_at);

                Log.println("执行 SQL: " + ps);

                ps.executeUpdate();
            }
        }
    }
}
