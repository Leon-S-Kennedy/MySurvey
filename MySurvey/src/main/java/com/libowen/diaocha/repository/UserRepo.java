package com.libowen.diaocha.repository;

import com.libowen.diaocha.data_object.UserDO;
import com.libowen.diaocha.util.DBUtil;
import com.libowen.diaocha.util.Log;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

//此处执行真正的sql
public class UserRepo {
    @SneakyThrows
    public static UserDO selectOneByUsername(String username) {
        String sql ="select uid,username,password from users where username = ?";
        try(Connection c = DBUtil.connection()){
            try(PreparedStatement ps = c.prepareStatement(sql)){
                Log.println("执行SQL: "+ps);
                ps.setString(1,username);
                try(ResultSet rs = ps.executeQuery()){
                    if(!rs.next()){
                        return null;
                    }
                    return new UserDO(
                            rs.getInt("uid"),
                            rs.getString("username"),
                            rs.getString("password")
                    );
                }
            }
        }
    }

    @SneakyThrows
    public void insert(UserDO userDO) {
        String sql="insert into users (username, password) values (?, ?)";
        try(Connection c = DBUtil.connection()){
            try(PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
                Log.println("执行SQL: "+ps);
                ps.setString(1,userDO.username);
                ps.setString(2,userDO.password);
                ps.executeUpdate();
                try(ResultSet rs = ps.getGeneratedKeys()){
                    rs.next();
                    userDO.uid=rs.getInt(1);
                }
            }
        }
    }
}
