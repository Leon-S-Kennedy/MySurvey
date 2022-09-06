package com.libowen.diaocha.servlet.user;

import com.libowen.diaocha.util.Log;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/user/quit.do")
public class QuitDoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        Log.println("session对象: "+session);

        if(session!=null){
            session.removeAttribute("currentUser");
            Log.println("清空session对象中的currentUser");
        }

        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/plain");
        PrintWriter writer = resp.getWriter();
        Log.println("退出登录成功");
        writer.println("退出成功");
    }
}
