package com.libowen.diaocha.servlet.survey;

import com.libowen.diaocha.data_object.SurveyDO;
import com.libowen.diaocha.repository.SurveyRepo;
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

@WebServlet("/survey/create.do")
public class CreateDoServlet extends HttpServlet {
    private final SurveyRepo surveyRepo=new SurveyRepo();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. 读取用户参数
        req.setCharacterEncoding("utf-8");
        String title = req.getParameter("title");
        String brief = req.getParameter("brief");
        Log.println("参数 title = " + title);
        Log.println("参数 brief = " + brief);

        // 2. 验证用户是否登录
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

        // 3. 一个插入操作
        SurveyDO surveyDO = new SurveyDO(currentUser.uid, title, brief);
        surveyRepo.insert(surveyDO);
        Log.println("插入完成: " + surveyDO);

        // 4. 响应插入完成
        writer.println("插入成功");
    }
}
