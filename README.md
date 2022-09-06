#在线调查问卷系统
##前言
为了巩固自己的JavaWeb基础，采用了Servlet+JSON+Ajax的方式来完成这个项目
##开发环境
集成开发环境：IDEA  
Web服务器：Tomcat  
项目搭建：Maven  
相关软件包：servlet、mysql、lombok、jackson
##功能点
###用户管理  
**注册：**
管理员用户进行注册，使用form表单收集用户的username和password。插入到数据库表中，为了提高安全性使用BCrypt对用户的密码进行加密。  
**登录:**
从session中判断用户是否登录，登录后可以进行下一步操作，如果没有登录就回到登录页面，根据用户输入的用户名和密码来验证是否注册过。  
**退出：**
从session中删除用户的信息。
###题库管理  
**录入题目：**
使用form表单收集管理员需要录入的题目，然后将题目插入到数据库表中。  
**题目列表：**
查询当前管理员账户所录制的所有题目以及其相关信息，由于题目信息量较大，所以需要加入分页功能。并且需要能够显示该题目被多少问卷所绑定。  
###问卷管理
**新建问卷：**
输入本次问卷的题目和简介，然后将录入的信息进行提交然后存储到数据库中。  
**问卷列表：**
查询当前管理员账户所创建的问卷信息，然后分页显示  
**关联题目：**
将指定的问卷关联题目，即将题目绑定到问卷中。
###活动管理  
**新建活动：**
创建新的活动，指定具体的调查问卷，然后插入到数据库表中。  
**活动列表：**
显示当前管理员用户所创建的活动信息。
**结果显示：**
收集用户提交的调查问卷，对调查问卷的结果进行分析，使用统计图表的方式来直观的展示本次问卷的相关信息  

###调查问卷页
这个是发布给普通用户的调查问卷。包含活动信息，问卷信息，题目信息等。
##数据库设计
本项目中共涉及到6张表：用户表、题目表、问卷表、题目和问卷关系表、活动表、结果表  
* users ： uid、username、password  
* questions ： qid、uid、question、options  
* surveys ： sid、uid、title、brief  
* relations ： rid、sid、qid  
* activies ： aid、uid、sid、started_at、ended_at  
* results ： reid、aid、nickname、phone、answer  

表的关系如下：  
![](D:\javaFile\ideaProjects\MySurvey\blog\img.png)
##具体实现
###用户管理
**注册：**  
/user/register.html ：  
* 静态资源，采用form 表单，提交 post 请求，提交用户名 + 密码  

/user/register.do ：
* 动态资源，读取用户名 + 密码，并对密码进行加密
* 将数据存储到数据库中，将用户信息放入session中  

核心SQL：`insert into users (username, password) values (?, ?);`  
**登录：**  
/user/login.html ：
* 静态资源，采用form 表单，提交 post 请求，提交用户名 + 密码

/user/login.do ：
* 动态资源，读取用户名 + 密码
* 从数据库中查找信息，并合演密码是否正确，将用户信息放入session中  

核心SQL：`select uid,username,password from users where username = ?;`  

**退出：**  
/user/quit.do：  
* 动态资源，将用户的信息从session中删除即可
###题库管理
**题目录入：**  
/question/create.html： 
* 静态资源，采用form表单，提交post请求，将题目信息进行提交  

/question/create.do：
* 动态资源，将提交的题目进行存储  

核心SQL：`insert into questions (uid,question,options) values (?,?,?);`  
**题目列表:**  
/question/list.html: 
* 静态资源，用来展示题目列表的信息（采用了分页的方式） 

/question/js/list.js:  
* 静态资源，等/question/list.html页面加载完毕之后，向/question/list.json发起Ajax请求，获取到带有题目信息的JSON数据后，开始修改DOM树进行页面的渲染  

/question/list.json:  
* 动态资源，根据需要的json格式，从数据库表中查询需要的数据，序列化成json格式。  

核心SQL：  
`select qid, uid, question, options from questions where uid = ? order by qid desc limit ? offset ?`  
`select count(*) from questions where uid = ?`  
`select qid ,count(*) as ref_count from relations where qid in(%s) group by qid order by qid`  

###问卷管理
**创建问卷：**  
/survey/create.html：  
* 静态资源，收集用户输入的问卷基本信息  

/survey/create.do： 
* 动态资源，将收集到问卷信息储存到数据库表中  

核心SQL:`insert into surveys (uid,title,brief) values (?,?,?)`  

**问卷列表：**  
/survey/list.html：  
* 静态资源，用来展示题目列表的信息（采用了分页的方式）  

/survey/js/list.js：  
* 静态资源，用于向后端发起Ajax请求，获取到json数据后对前端页面进行渲染  

/survey/list.json： 
* 动态资源，从数据库表中查询问卷信息，序列化成json格式  

核心SQL:  
`select sid, uid, title, brief from surveys where uid = ? order by sid desc limit ? offset ?`  
`select count(*) from surveys where uid=?`  
`select sid ,count(*) as ref_count from relations where sid in(%s) group by sid order by sid`  

**关联题目：**  
/survey/bind.html：  
* 静态资源，使用单选框来展示问卷和题目之间是否绑定的关系  

/survey/js/bind.js： 
* 静态资源，用于获取后端数据然后对前端进行渲染  

/survey/bind.json：  
* 动态资源，从数据库表中查询问卷信息和题目信息以及两者之间的关系信息。然后序列化成json格式  

/survey/bind.do：
* 动态资源，根据选中的信息来往关系表中插入数据，完成问卷和题目之间的关联。  

核心SQL:  
`select sid, uid, title, brief from surveys where sid = ? and uid = ?`  
`select qid from relations where sid = ? order by rid`  
`select qid, uid, question, options from questions where uid = ? order by qid desc limit ? offset ?`  
`delete from relations where sid = ? and qid in (%s)`  
`insert into relations (sid, qid) values (...)`

###活动管理
**创建活动：**  
/activity/create.html：  
* 静态资源，用下拉列表显示问卷信息，收集活动信息  

/activity/js/create.js：  
* 静态资源，用来向后端请求问卷信息，然后进行渲染  

/survey/simple-list.json：  
* 动态资源，生成JSON格式的问卷信息  

/activity/create.do：  
* 动态资源，将提交的活动信息插入到数据库表中  

核心SQL：  
`select sid, uid, title, brief from surveys where uid = ? order by sid desc`    
`insert into activities (uid, sid, started_at, ended_at) values (?, ?, ?, ?)`    

**活动列表：**  
/activity/list.html：
* 静态资源，用来展示当前管理员用户所创建的活动列表  

/activity/js/list.js：  
* 静态资源，向后端请求JSON格式的数据  

/activity/list.json：  
* 动态资源，生成json格式的数据信息，包含问卷信息，活动信息等数据  

核心SQL：  
`select aid, a.sid, s.title, a.started_at, a.ended_at from activities a join surveys s on a.sid = s.sid and a.uid = s.uid where a.uid = ? order by a.aid desc`  

**结果展示**  
/activity/result.html：  
* 静态资源，用来显示调查问卷各个题目的统计结果

/activity/js/result.js：  
* 静态资源，向后端发起请求，然后对前端进行渲染

/activity/result.json：  
* 动态资源，将问题，问卷，活动，结果等信息进行汇总，然后序列化成为json格式

核心SQL：  
`select aid, started_at, ended_at, a.sid, title, brief from activities a join surveys s on a.sid = s.sid where a.uid = ? and aid = ?`    
`select qid from relations where sid = ? order by rid`  
`select qid, question, options from questions where qid in (%s)`  
`select answer from results where aid = ?`  

###调查问卷页
/activity/exam.html：  
* 静态资源，可供所有用户填写的调查问卷，包含问卷信息和题目信息。

/activity/js/exam.js：  
* 静态资源，静态资源，向后端发起请求，然后对前端进行渲染

/activity/exam.json：  
* 动态资源，生成json格式的问卷信息等

/activity/exam.do：  
* 收集用户的昵称，电话以及调查问卷的结果，将这些信息储存到数据库表中  

核心SQL：  
`select title, brief, q.qid, question, options from activities a join surveys s on a.sid = s.sid join relations r on s.sid = r.sid join questions q on r.qid = q.qid where aid = ? order by q.qid`  
`insert into results (aid,nickname,phone,answer) values (?,?,?,?)`  

##项目总结
该项目属于基础的JavaWeb练手项目，用来巩固自己的MySQL以及JavaWeb的能力。主要是需要理清各个数据库表之间的关系，整理各个功能点对应的SQL代码，剩下的就是将数据一层一层凑成自己需要的格式，然后进行序列化。