package com.libowen.diaocha.repository;

import com.libowen.diaocha.util.DBUtil;
import com.libowen.diaocha.util.Log;
import com.libowen.diaocha.view_object.ActivityView;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ActivitySurveyRepo {
    @SneakyThrows
    public List<ActivityView> selectListByUid(int uid) {
        String sql = "select aid, a.sid, s.title, a.started_at, a.ended_at from activities a join surveys s on a.sid = s.sid and a.uid = s.uid where a.uid = ? order by a.aid desc";

        List<ActivityView> list = new ArrayList<>();
        try (Connection c = DBUtil.connection()) {
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setInt(1, uid);

                Log.println("执行 SQL: " + ps);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        ActivityView view = new ActivityView(
                                rs.getInt("aid"),
                                rs.getInt("sid"),
                                rs.getString("title"),
                                rs.getTimestamp("started_at"),
                                rs.getTimestamp("ended_at")
                        );

                        list.add(view);
                    }
                }
            }
        }

        return list;
    }
}
