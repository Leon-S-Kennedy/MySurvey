package com.libowen.diaocha.service;

import com.libowen.diaocha.data_object.QuestionDO;
import com.libowen.diaocha.data_object.SidToRefCountDo;
import com.libowen.diaocha.data_object.SurveyDO;
import com.libowen.diaocha.repository.QuestionRepo;
import com.libowen.diaocha.repository.RelationRepo;
import com.libowen.diaocha.repository.SurveyRepo;
import com.libowen.diaocha.util.Log;
import com.libowen.diaocha.view_object.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SurveyService {
    //private final ObjectMapper objectMapper=new ObjectMapper();
    private final SurveyRepo surveyRepo=new SurveyRepo();
    private final RelationRepo relationRepo=new RelationRepo();
    private final QuestionRepo questionRepo=new QuestionRepo();

    public SurveyListView list(UserVO currentUser, int page) {
        int countPerPage=5;//每页5条

        PaginationView paginationView = new PaginationView();
        paginationView.countPerPage=countPerPage;
        paginationView.currentPage=page;

        int count=surveyRepo.selectCountByUid(currentUser.uid);
        Log.println("得到的结果是"+count);

        if(count==0){
            Log.println("为结果是0进行特殊处理");
            SurveyListView resultView=new SurveyListView();
            resultView.currentUser=currentUser;
            paginationView.totalPage=0;
            resultView.pagination=paginationView;
            resultView.surveyList=new ArrayList<>();
            return resultView;
        }

        int totalPage=count/countPerPage;
        if(count%countPerPage!=0){
            totalPage++;
        }
        paginationView.totalPage=totalPage;

        if(page<1){
            Log.println("page过小,修改成1");
            page=1;
        }
        if(page>totalPage){
            Log.println("page过打,修改成"+totalPage);
            page=totalPage;
        }

        int limit=countPerPage;
        int offset=(page-1)*countPerPage;

        List<SurveyDO> surveyDOList =surveyRepo.selectListByUidLimitOffset(currentUser.uid,limit,offset);
        Log.println("从survey表中查到的结果： "+surveyDOList);
        if(surveyDOList.isEmpty()){
            SurveyListView resultView=new SurveyListView();
            resultView.currentUser=currentUser;
            resultView.pagination=paginationView;
            resultView.surveyList=new ArrayList<>();

            return resultView;
        }

        List<Integer> sidList=new ArrayList<>();
        for (SurveyDO surveyDO : surveyDOList) {
            sidList.add(surveyDO.sid);
        }
        List<SidToRefCountDo> sidToRefCountDoList = relationRepo.selectCountGroupBySid(sidList);
        Log.println("查询关联数据： "+sidToRefCountDoList);
        Map<Integer,Integer> sidToRefCountMap=new HashMap<>();
        for (SidToRefCountDo sidToRefCountDo : sidToRefCountDoList) {
            sidToRefCountMap.put(sidToRefCountDo.sid,sidToRefCountDo.refCount);
        }
        Log.println("qid to ref_count map: "+sidToRefCountMap);

        //List<SurveyDO> ---->SurveyListView
        SurveyListView resultView=new SurveyListView();
        resultView.currentUser=currentUser;
        resultView.pagination=paginationView;
        resultView.surveyList=new ArrayList<>();
        for (SurveyDO surveyDO : surveyDOList) {
            SurveyView surveyView=new SurveyView(surveyDO,sidToRefCountMap.getOrDefault(surveyDO.sid,0));
            resultView.surveyList.add(surveyView);
        }
        return resultView;
    }


    public SurveyBindView bindCandidates(UserVO user, int sid, int page) {
        SurveyDO surveyDO = surveyRepo.selectOneBySidAndUid(sid, user.uid);
        if (surveyDO == null) {
            // 要绑定的 sid（问卷）是不存在的
            // TODO: 具体怎么处理
            throw new RuntimeException("404");
        }

        List<Integer> qidBoundenList = relationRepo.selectQidListBySid(sid);

        if (page < 1) {
            page = 1;
        }
        int limit = 5;
        int offset = (page - 1) * limit;    // 暂时不考虑 page 是错误值的情况
        List<QuestionDO> questionDOList = questionRepo.selectListByUidLimitOffset(user.uid, limit, offset);

        return new SurveyBindView(user, surveyDO, qidBoundenList, questionDOList);
    }
}
