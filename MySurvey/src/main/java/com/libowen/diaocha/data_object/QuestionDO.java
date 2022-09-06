package com.libowen.diaocha.data_object;

import lombok.Data;

@Data
public class QuestionDO {
    public Integer qid;
    public Integer uid;
    public String question;
    public String options;


    public QuestionDO(Integer uid, String question, String optionsJSONString) {
        this.uid=uid;
        this.question=question;
        this.options=optionsJSONString;
    }

    public QuestionDO(Integer qid, Integer uid, String question, String options) {
        this.qid = qid;
        this.uid = uid;
        this.question = question;
        this.options = options;
    }
}
