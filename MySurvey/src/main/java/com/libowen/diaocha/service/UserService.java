package com.libowen.diaocha.service;

import com.libowen.diaocha.data_object.UserDO;
import com.libowen.diaocha.repository.UserRepo;
import com.libowen.diaocha.util.Log;
import com.libowen.diaocha.view_object.UserVO;
import org.mindrot.jbcrypt.BCrypt;

public class UserService {
    private final UserRepo userRepo=new UserRepo();

    public static UserVO login(String username, String password) {
        //先查询相应记录
        UserDO userDO = UserRepo.selectOneByUsername(username);
        Log.println("从表中查到的用户为: "+userDO);
        if(userDO==null){
            //查不到用户
            Log.println("根据用户名无法从表中查询到数据");
            return null;
        }
        //判断密码是否正确
        //前面是明文密码
        if(!BCrypt.checkpw(password,userDO.password)){
            //没有匹配上
            Log.println("用户名密码不匹配");
            return null;
        }
        Log.println("用户名密码正确");
        return new UserVO(userDO);

    }

    public UserVO register(String username, String password) {
        //1.针对密码进行加密
        String salt = BCrypt.gensalt();
        password = BCrypt.hashpw(password, salt);

        //2.将数据插入
        UserDO userDO=new UserDO(username,password);
        userRepo.insert(userDO);
        return new UserVO(userDO);


    }
}
