package com.libowen.diaocha.view_object;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
public class QuestionListView {
    public UserVO currentUser;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public PaginationView pagination;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public List<QuestionView> questionList;
}
