package com.libowen.diaocha.servlet.survey;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.libowen.diaocha.service.SurveyService;
import com.libowen.diaocha.util.Log;
import com.libowen.diaocha.view_object.SurveyListView;
import com.libowen.diaocha.view_object.UserVO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/survey/list.json")
public class ListJsonServlet extends HttpServlet {
    private final ObjectMapper objectMapper=new ObjectMapper();
    private final SurveyService surveyService=new SurveyService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        Log.println("query string"+req.getQueryString());

        String pageString = req.getParameter("page");
        int page;
        if(pageString==null||pageString.trim().isEmpty()){
            Log.println("用户没有传入page信息,让page为1");
            page=1;
        }else{
            try{
                page=Integer.parseInt(pageString.trim());
            }catch (NumberFormatException exc){
                Log.println("用户传入page不是合法数字,让page为1");
                page=1;
            }
        }

        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();

        UserVO currentUser = null;
        HttpSession session = req.getSession(false);
        if (session != null) {
            currentUser = (UserVO) session.getAttribute("currentUser");
        }
        if(currentUser==null){
            Log.println("用户未登录");
            SurveyListView surveyListView=new SurveyListView();
            //这两句可以不写
            surveyListView.currentUser=null;
            surveyListView.surveyList=null;

            String json = objectMapper.writeValueAsString(surveyListView);
            writer.println(json);
            return;
        }

        SurveyListView resultView =surveyService.list(currentUser,page);
        Log.println("得到的结果对象： "+resultView);
        //将对象JSON格式的序列化
        String json = objectMapper.writeValueAsString(resultView);
        Log.println("JSON序列化： "+json);
        //响应数据
        writer.println(json);


    }
}
