package com.libowen.diaocha.servlet.activity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.libowen.diaocha.repository.ExamRepo;
import com.libowen.diaocha.util.Log;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/activity/exam.do")
public class ExamDoServlet extends HttpServlet {
    private final ObjectMapper objectMapper=new ObjectMapper();
    private final ExamRepo examRepo=new ExamRepo();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");

        String aid = req.getParameter("aid");
        String nickname = req.getParameter("nickname");
        String phone = req.getParameter("phone");
        Enumeration<String> parameterNames = req.getParameterNames();
        Map<String,String> answer=new HashMap<>();
        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            if(name.startsWith("qid-")){
                String qid=name.substring("qid-".length());
                String value =req.getParameter(name);
                answer.put(qid,value);
            }
        }
        Log.println("aid: "+aid);
        Log.println("nickname: "+nickname);
        Log.println("phone: "+phone);

        String answerJson=objectMapper.writeValueAsString(answer);
        Log.println("answer= "+answerJson);

        examRepo.insert(aid,nickname,phone,answerJson);

        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/plain");
        PrintWriter writer = resp.getWriter();

        writer.println("感谢参加调查问卷");
    }
}
