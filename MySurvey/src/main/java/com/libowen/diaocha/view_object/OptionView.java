package com.libowen.diaocha.view_object;

import com.libowen.diaocha.repository.ResultRepo;

public class OptionView {
    public String name;
    public Integer value;

    public OptionView(ResultRepo.OptionCount optionCount) {
        this.name = optionCount.option;
        this.value = optionCount.count;
    }
}
