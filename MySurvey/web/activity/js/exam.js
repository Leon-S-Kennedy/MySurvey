var xhr = new XMLHttpRequest()
xhr.open('get', '/activity/exam.json' + location.search)
xhr.onload = function() {
    var data = JSON.parse(this.responseText)
    document.querySelector('#aid').value = data.aid
    document.querySelector('.title').textContent = data.title
    document.querySelector('.brief').textContent = data.brief

    var oQuestionList = document.querySelector('.questionList')
    var list = data.questionList
    for (var i in list) {
        var q = list[i]

        var html = `<div class="questionItem">
            <div class="question">${q.question}</div>
            <div class="option">
            <input type="radio" name="qid-${q.qid}" value="0"> ${q.optionList[0]}
            </div>
            <div class="option">
            <input type="radio" name="qid-${q.qid}" value="1"> ${q.optionList[1]}
            </div>
            <div class="option">
            <input type="radio" name="qid-${q.qid}" value="2"> ${q.optionList[2]}
            </div>
            <div class="option">
            <input type="radio" name="qid-${q.qid}" value="3"> ${q.optionList[3]}
            </div>
            </div>`

        oQuestionList.innerHTML += html
    }
}
xhr.send()