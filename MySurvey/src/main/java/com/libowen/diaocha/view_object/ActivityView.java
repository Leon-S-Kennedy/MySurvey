package com.libowen.diaocha.view_object;

import lombok.Data;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class ActivityView {
    public Integer aid;
    public Integer sid;
    public String title;
    public String startedAt;
    public String endedAt;
    public String state;


    public ActivityView(int aid, int sid, String title, Timestamp startedAt, Timestamp endedAt) {
        this.aid=aid;
        this.sid=sid;
        this.title=title;
        this.startedAt = timestampToString(startedAt);
        this.endedAt = timestampToString(endedAt);
        this.state = calcState(startedAt, endedAt);
    }

    private String calcState(Timestamp startedAt, Timestamp endedAt) {
        Instant now = Instant.now();
        Timestamp nowTS = Timestamp.from(now);
        if (nowTS.before(startedAt)) {
            return "未开始";
        } else if (nowTS.after(endedAt)) {
            return "已结束";
        } else {
            return "进行中";
        }
    }

    private String timestampToString(Timestamp  ts) {
        LocalDateTime localDateTime = ts.toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return formatter.format(localDateTime);
    }
}
