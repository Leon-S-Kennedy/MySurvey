package com.libowen.diaocha.servlet.survey;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.libowen.diaocha.service.SurveyService;
import com.libowen.diaocha.view_object.SurveyBindView;
import com.libowen.diaocha.view_object.UserVO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/survey/bind.json")
public class BindJsonServlet extends HttpServlet {
    private final SurveyService surveyService = new SurveyService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        int page = Integer.parseInt(req.getParameter("page"));
        int sid = Integer.parseInt(req.getParameter("sid"));
        UserVO currentUser = null;
        HttpSession session = req.getSession(false);
        if (session != null) {
            currentUser = (UserVO) session.getAttribute("currentUser");
        }

        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();

        if (currentUser == null) {
            SurveyBindView resultView = new SurveyBindView();
            String json = objectMapper.writeValueAsString(resultView);
            writer.println(json);
        } else {
            SurveyBindView resultView = surveyService.bindCandidates(currentUser, sid, page);
            String json = objectMapper.writeValueAsString(resultView);
            writer.println(json);
        }
    }
}
