package com.libowen.diaocha.view_object;

import com.libowen.diaocha.data_object.QuestionDO;
import lombok.Data;

import java.util.List;

@Data
public class QuestionBindView {
    public Integer qid;
    public String question;
    public Boolean bounden;

    public QuestionBindView(QuestionDO questionDO, List<Integer> qidBoundenList) {
        this.qid=questionDO.qid;
        this.question=questionDO.question;
        this.bounden=qidBoundenList.contains(questionDO.qid);
    }
}
