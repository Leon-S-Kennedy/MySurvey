package com.libowen.diaocha.view_object;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.libowen.diaocha.data_object.QuestionDO;
import lombok.Data;
import lombok.SneakyThrows;

import java.util.List;

@Data
public class QuestionView {
    public Integer qid;
    public String question;
    public List<String> options;
    public Integer refCount;
    @SneakyThrows
    public QuestionView(ObjectMapper objectMapper, QuestionDO questionDO,int refCount) {
        this.qid=questionDO.qid;
        this.question=questionDO.question;

        this.options = objectMapper.readValue(questionDO.options, new TypeReference<List<String>>() {
        });
        this.refCount=refCount;
    }
}
