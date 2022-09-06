package com.libowen.diaocha.servlet.activity;

import com.libowen.diaocha.repository.ActivityRepo;
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

@WebServlet("/activity/create.do")
public class CreateDoServlet extends HttpServlet {
    private final ActivityRepo activityRepo=new ActivityRepo();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        String sid = req.getParameter("sid");
        String started_at = req.getParameter("started_at");
        String ended_at = req.getParameter("ended_at");

        UserVO currentUser = null;
        HttpSession session = req.getSession(false);
        if (session != null) {
            currentUser = (UserVO) session.getAttribute("currentUser");
        }

        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/plain");
        PrintWriter writer = resp.getWriter();

        if (currentUser == null) {
            Log.println("用户未登录");
            writer.println("必须登录后才能使用");
            return;
        }

        activityRepo.insert(currentUser.uid,sid,started_at,ended_at);

        writer.println("插入成功");
    }
}
