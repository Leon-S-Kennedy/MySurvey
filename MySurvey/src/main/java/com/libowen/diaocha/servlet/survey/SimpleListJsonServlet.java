package com.libowen.diaocha.servlet.survey;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.libowen.diaocha.data_object.SurveyDO;
import com.libowen.diaocha.repository.SurveyRepo;
import com.libowen.diaocha.view_object.SimpleSurveyView;
import com.libowen.diaocha.view_object.SurveySimpleListView;
import com.libowen.diaocha.view_object.UserVO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/survey/simple-list.json")
public class SimpleListJsonServlet extends HttpServlet {

    private final SurveyRepo surveyRepo = new SurveyRepo();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserVO currentUser = null;
        HttpSession session = req.getSession(false);
        if (session != null) {
            currentUser = (UserVO) session.getAttribute("currentUser");
        }

        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();

        SurveySimpleListView resultView = new SurveySimpleListView();
        if (currentUser == null) {
            String json = objectMapper.writeValueAsString(resultView);
            writer.println(json);
            return;
        }

        List<SurveyDO> surveyDOList = surveyRepo.selectListByUid(currentUser.uid);
        resultView.currentUser = currentUser;
        resultView.surveyList = new ArrayList<>();
        for (SurveyDO surveyDO : surveyDOList) {
            SimpleSurveyView view = new SimpleSurveyView();
            view.sid = surveyDO.sid;
            view.title = surveyDO.title;
            resultView.surveyList.add(view);
        }

        String json = objectMapper.writeValueAsString(resultView);
        writer.println(json);

    }
}
