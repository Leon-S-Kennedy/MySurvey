package com.libowen.diaocha.view_object;

import lombok.Data;

import java.util.List;

@Data
public class SurveySimpleListView {
    public UserVO currentUser;
    public List<SimpleSurveyView> surveyList;
}
