package com.libowen.diaocha.servlet.user;


import com.libowen.diaocha.service.UserService;
import com.libowen.diaocha.view_object.UserVO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/user/register.do")
public class RegisterDoServlet extends HttpServlet {
    private final UserService userService=new UserService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        //2.进行注册（加密+数据库插入）交给service对象
        UserVO userVO = userService.register(username,password);

        //3.注册成功后，同步登录
        HttpSession session = req.getSession();
        session.setAttribute("currentUseer",userVO);

        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/plain");
        PrintWriter writer = resp.getWriter();
        writer.println("注册成功！");


    }
}
