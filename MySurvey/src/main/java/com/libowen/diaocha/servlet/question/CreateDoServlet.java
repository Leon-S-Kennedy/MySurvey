package com.libowen.diaocha.servlet.question;

import com.libowen.diaocha.service.QuestionService;
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
import java.util.Arrays;

@WebServlet("/question/create.do")
public class CreateDoServlet extends HttpServlet {
    private final QuestionService questionService = new QuestionService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        //获取用户输入的题目和选项
        String question = req.getParameter("question");
        String[] options = req.getParameterValues("option");

        Log.println("用户提交的题目: "+question);
        Log.println("用户提交的选项: "+ Arrays.toString(options));

        //判断用户是否登录,如果没有就提示登录后使用
        HttpSession session = req.getSession(false);

        UserVO currentUser=null;
        if(session!=null){
            currentUser = (UserVO)session.getAttribute("currentUser");
        }else {
            Log.println("session不存在");
        }
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/plain");
        PrintWriter writer = resp.getWriter();

        if(currentUser==null){
            Log.println("说明用户没有登录");
            writer.println("必须登录后使用");
            return;
        }
        //一定有用户登录
        Log.println("当前登录的用户为: "+currentUser);

        questionService.save(currentUser,question,options);

        Log.println("题目保存成功");
        writer.println("题目保存成功");
    }
}
