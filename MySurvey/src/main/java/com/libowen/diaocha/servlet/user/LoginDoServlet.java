package com.libowen.diaocha.servlet.user;


import com.libowen.diaocha.service.UserService;
import com.libowen.diaocha.util.Log;
import com.libowen.diaocha.view_object.UserVO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/user/login.do")
public class LoginDoServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //读取用户名+密码
        req.setCharacterEncoding("utf-8");
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        Log.println("输入的用户名是: "+username);
        Log.println("输入的密码是: "+password);

        //进行登录验证
        UserVO userVO = UserService.login(username,password);
        Log.println("登录得到的用户为: "+userVO);

        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/plain");
        PrintWriter writer = resp.getWriter();


        if(userVO==null){
            //登录失败
            Log.println("登录失败");
            writer.println("登录失败!");
            return;
        }

        HttpSession session = req.getSession();
        session.setAttribute("currentUser",userVO);
        Log.println("将用户放入session中");

        writer.println("登录成功!");
    }
}
