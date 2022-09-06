package com.libowen.diaocha.view_object;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.libowen.diaocha.data_object.QuestionDO;
import com.libowen.diaocha.data_object.SurveyDO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SurveyBindView {
    public UserVO currentUser;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Integer sid;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String title;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String brief;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public List<QuestionBindView> questionList;


    public SurveyBindView(UserVO user, SurveyDO surveyDO, List<Integer> qidBoundenList, List<QuestionDO> questionDOList) {
        this.currentUser=user;
        this.sid=surveyDO.sid;
        this.title=surveyDO.title;
        this.brief=surveyDO.brief;
        this.questionList=new ArrayList<>();
        for (QuestionDO questionDO : questionDOList) {
            QuestionBindView view=new QuestionBindView(questionDO,qidBoundenList);
            this.questionList.add(view);
        }

    }

    public SurveyBindView() {

    }
}
