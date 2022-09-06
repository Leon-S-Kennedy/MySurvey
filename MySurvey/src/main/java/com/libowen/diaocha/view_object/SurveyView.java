package com.libowen.diaocha.view_object;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.libowen.diaocha.data_object.SurveyDO;
import lombok.Data;

@Data
public class SurveyView {
    public Integer sid;
    public String title;
    public String brief;
    public Integer refCount;

    public SurveyView( SurveyDO surveyDO,int refCount) {
        this.sid=surveyDO.sid;
        this.title=surveyDO.title;
        this.brief=surveyDO.title;
        this.refCount=refCount;
    }
}
