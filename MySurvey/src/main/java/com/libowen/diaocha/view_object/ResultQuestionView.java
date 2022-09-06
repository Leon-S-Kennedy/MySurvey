package com.libowen.diaocha.view_object;

import com.libowen.diaocha.repository.ResultRepo;

import java.util.List;

public class ResultQuestionView {
    public Integer qid;
    public String question;
    public OptionView[] results = new OptionView[4];


    public ResultQuestionView(ResultRepo.Question question) {
        this.qid = question.qid;
        this.question = question.question;
        for (int i = 0; i < 4; i++) {
            ResultRepo.OptionCount optionCount = question.options[i];
            OptionView optionView = new OptionView(optionCount);
            results[i] = optionView;
        }
    }
}
