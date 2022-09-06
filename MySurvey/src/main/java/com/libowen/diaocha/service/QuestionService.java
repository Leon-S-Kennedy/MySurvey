package com.libowen.diaocha.service;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.libowen.diaocha.data_object.QidToRefCountDo;
import com.libowen.diaocha.data_object.QuestionDO;
import com.libowen.diaocha.repository.QuestionRepo;
import com.libowen.diaocha.repository.RelationRepo;
import com.libowen.diaocha.util.Log;
import com.libowen.diaocha.view_object.PaginationView;
import com.libowen.diaocha.view_object.QuestionListView;
import com.libowen.diaocha.view_object.QuestionView;
import com.libowen.diaocha.view_object.UserVO;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionService {

    //该属性用来将options进行JSON序列化
    private final ObjectMapper objectMapper;
    private final QuestionRepo questionRepo=new QuestionRepo();
    private final RelationRepo relationRepo=new RelationRepo();

    public QuestionService(){
        objectMapper=new ObjectMapper();
        objectMapper.setDefaultPrettyPrinter(new DefaultPrettyPrinter()); //让序列化的JSON带空格和换行
    }
    @SneakyThrows
    public void save(UserVO userVO, String question, String[] options) {
        Log.println("对选项数组进行JSON序列化");
        String optionsJSONString = objectMapper.writeValueAsString(options);
        Log.println("JSON: "+optionsJSONString);

        //使用questionRepo对象插入表操作
        QuestionDO questionDO = new QuestionDO(userVO.uid, question, optionsJSONString);
        questionRepo.insert(questionDO);
        Log.println("插入成功: "+questionDO);
    }

    public QuestionListView list(UserVO currentUser,int page) {
        int countPerPage=5;//每页5条

        PaginationView paginationView = new PaginationView();
        paginationView.countPerPage=countPerPage;
        paginationView.currentPage=page;

        int count=questionRepo.selectCountByUid(currentUser.uid);
        Log.println("得到的结果是"+count);

        if(count==0){
            Log.println("为结果是0进行特殊处理");
            QuestionListView resultView=new QuestionListView();
            resultView.currentUser=currentUser;
            paginationView.totalPage=0;
            resultView.pagination=paginationView;
            resultView.questionList=new ArrayList<>();
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




        //查询
        List<QuestionDO> questionDOList =questionRepo.selectListByUidLimitOffset(currentUser.uid,limit,offset);
        Log.println("从question表中查到的结果： "+questionDOList);
        if(questionDOList.isEmpty()){
            QuestionListView resultView=new QuestionListView();
            resultView.currentUser=currentUser;
            resultView.pagination=paginationView;
            resultView.questionList=new ArrayList<>();

            return resultView;
        }

        //提取qidList
        List<Integer> qidList=new ArrayList<>();
        for (QuestionDO questionDO : questionDOList) {
            qidList.add(questionDO.qid);
        }
        List<QidToRefCountDo> qidToRefCountDoList = relationRepo.selectCountGroupByQid(qidList);
        Log.println("查询关联数据： "+qidToRefCountDoList);
        Map<Integer,Integer> qidTORefCountMap=new HashMap<>();
        for (QidToRefCountDo qidToRefCountDo : qidToRefCountDoList) {
            qidTORefCountMap.put(qidToRefCountDo.qid,qidToRefCountDo.refCount);
        }
        Log.println("qid to ref_count map: "+qidTORefCountMap);

        //数据格式转换List<QuestionDO> ---> QuestionListView
        QuestionListView resultView=new QuestionListView();

        resultView.currentUser=currentUser;
        resultView.pagination=paginationView;
        resultView.questionList=new ArrayList<>();
        for (QuestionDO questionDO : questionDOList) {
            QuestionView questionView = new QuestionView(objectMapper,questionDO,qidTORefCountMap.getOrDefault(questionDO.qid,0));
            resultView.questionList.add(questionView);
        }
        return resultView;
    }
}
