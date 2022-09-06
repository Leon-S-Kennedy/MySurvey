function renderWho(user) {
    if(user){
        document.querySelector(".who").textContent=user.username
    }else {
        document.querySelector(".who").textContent="请登录后使用"
    }
}
function renderPagination(pagination) {
    var currentPage = parseInt(pagination.currentPage)
    var totalPage = parseInt(pagination.totalPage)
    if (currentPage === 1) {
        document.querySelector('#prevPage').href += 1
    } else {
        document.querySelector('#prevPage').href += (currentPage - 1)
    }

    document.querySelector('#countPerPage').textContent = pagination.countPerPage
    document.querySelector('#currentPage').textContent = currentPage
    document.querySelector('#totalPage').textContent = totalPage

    if (currentPage >= totalPage) {
        document.querySelector('#nextPage').href += totalPage
    } else {
        document.querySelector('#nextPage').href += (currentPage + 1)
    }

    document.querySelector('#lastPage').href += totalPage
}

function renderList(surveyList) {
    var tbody=document.querySelector('tbody')
    for (var i in surveyList){
        var survey=surveyList[i]
        var html =`<tr><td>${survey.sid}</td><td>${survey.title}</td><td>${survey.brief}</td><td>${survey.refCount}</td></tr>`
        tbody.innerHTML+=html
    }

}
window.onload=function (){
    var xhr=new XMLHttpRequest()
    xhr.open('get',"/survey/list.json"+location.search)
    xhr.onload=function () {
        if(xhr.status!==200){
            alert("请求后端json出错，请检查/survey/list.json")
        }
        console.log(xhr.responseText)
        var data = JSON.parse(xhr.responseText);

        renderWho(data.currentUser)
        if(data.pagination){
            renderPagination(data.pagination)
        }
        if(data.surveyList){
            renderList(data.surveyList)
        }

    }
    xhr.send();
}