package com.libowen.diaocha.view_object;

import com.libowen.diaocha.data_object.UserDO;
import lombok.Data;

@Data
public class UserVO {
    public Integer uid;
    public String username;


    public UserVO(UserDO userDO) {
        this.uid=userDO.uid;
        this.username=userDO.username;
    }
}
