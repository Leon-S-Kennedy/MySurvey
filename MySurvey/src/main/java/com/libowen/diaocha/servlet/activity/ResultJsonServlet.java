package com.libowen.diaocha.servlet.activity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.libowen.diaocha.repository.ResultRepo;
import com.libowen.diaocha.view_object.ResultView;
import com.libowen.diaocha.view_object.UserVO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.SneakyThrows;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/activity/result.json")
public class ResultJsonServlet extends HttpServlet {
    private final ObjectMapper objectMapper=new ObjectMapper();
    private final ResultRepo resultRepo=new ResultRepo();




    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String aid = req.getParameter("aid");
        if (aid == null || aid.trim().isEmpty()) {
            throw new RuntimeException("必须带有 aid 参数");
        }

        try {
            Integer.parseInt(aid);
        } catch (NumberFormatException exc) {
            throw new RuntimeException("aid 参数必须是数字", exc);
        }

        HttpSession session = req.getSession(false);
        if (session == null) {
            throw new RuntimeException("用户未登录");
        }

        UserVO currentUser = (UserVO) session.getAttribute("currentUser");
        if (currentUser == null) {
            throw new RuntimeException("用户未登录");
        }

        ResultView resultView =new ResultView(currentUser);
        Map<Integer, ResultRepo.Question> qidToQuestionMap = new HashMap<>();
        resultRepo.select(currentUser,aid,resultView,objectMapper,qidToQuestionMap);


        String json = objectMapper.writeValueAsString(resultView);
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        writer.println(json);

    }
}
