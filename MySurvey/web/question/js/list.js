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

function renderList(questionList) {
    var tbody=document.querySelector("tbody")

    for (var i in questionList){
        var question=questionList[i]
        var html=`<tr><td>${question.qid}</td><td>${question.question}</td>`
        for (var j in question.options){
            var option=question.options[j]
            html+=`<td>${option}</td>`
        }
        html+=`<td>${question.refCount}</td></tr>`

        tbody.innerHTML+=html
    }
}
window.onload=function (){
    var xhr=new XMLHttpRequest()
    xhr.open('get','/question/list.json'+location.search)
    xhr.onload=function (){
        if(xhr.status!==200){
            alert('访问/question/list.json出错，优先调试此url')
            return
        }
        console.log(xhr.responseText)
        var data = JSON.parse(xhr.responseText);

        renderWho(data.currentUser)
        if(data.pagination){
            renderPagination(data.pagination)
        }
        if(data.questionList){
            renderList(data.questionList)
        }
    }
    xhr.send()
}