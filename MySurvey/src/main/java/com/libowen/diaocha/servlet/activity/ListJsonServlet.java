package com.libowen.diaocha.servlet.activity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.libowen.diaocha.repository.ActivitySurveyRepo;
import com.libowen.diaocha.view_object.ActivityView;
import com.libowen.diaocha.view_object.UserVO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/activity/list.json")
public class ListJsonServlet extends HttpServlet {
    private final ActivitySurveyRepo activityRepo = new ActivitySurveyRepo();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 这个里只写正常流程，所有非正常情况全部以异常的形式抛出
        HttpSession session = req.getSession(false);
        if (session == null) {
            throw new RuntimeException("session 为空，说明没登录");
        }

        UserVO currentUser = (UserVO) session.getAttribute("currentUser");
        if (currentUser == null) {
            throw new RuntimeException("用户没有登录");
        }

        List<ActivityView> activityViewList = activityRepo.selectListByUid(currentUser.uid);
        // 使用 Map 替代专门定义一个类
        Map<String, Object> view = new HashMap<>();
        view.put("currentUser", currentUser);
        view.put("activityList", activityViewList);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(view);

        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json");
        resp.getWriter().println(json);
    }
}
