package com.libowen.diaocha.data_object;

import lombok.Data;

@Data
public class SurveyDO {
    public Integer sid;
    public Integer uid;
    public String title;
    public String brief;

    public SurveyDO(Integer uid, String title, String brief) {
        this.uid = uid;
        this.title = title;
        this.brief = brief;
    }

    public SurveyDO(Integer sid, Integer uid, String title, String brief) {
        this.sid = sid;
        this.uid = uid;
        this.title = title;
        this.brief = brief;
    }
}
