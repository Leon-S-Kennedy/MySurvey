function initOption(question) {
    return {
        title: {
            text: question.question,
            left: 'center'
        },
        tooltip: {
            trigger: 'item'
        },
        legend: {
            orient: 'vertical',
            left: 'left'
        },
        series: [
            {
                name: 'Access From',
                type: 'pie',
                radius: '50%',
                data: question.results,
                emphasis: {
                    itemStyle: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }
        ]
    }
}

function renderQuestion(question) {
    var div = document.createElement('div')
    div.id = 'qid-' + question.qid
    div.style.width = '600px';
    div.style.height = '400px';

    var e = echarts.init(div)
    var option = initOption(question)
    e.setOption(option)

    return div
}

function render(data) {
    document.querySelector('.title').textContent = data.title
    document.querySelector('.brief').textContent = data.brief
    document.querySelector('.startedAt').textContent = data.startedAt
    document.querySelector('.endedAt').textContent = data.endedAt

    var list = data.questionList
    var oContainer = document.querySelector('.resultList')
    for (var i in list) {
        var q = list[i]
        console.log(q)
        var div = renderQuestion(q)
        oContainer.appendChild(div)
    }
}

window.onload = function() {
    var xhr = new XMLHttpRequest()
    xhr.open('get', '/activity/result.json' + location.search)
    xhr.onload = function() {
        console.log(this.responseText)
        var data = JSON.parse(this.responseText)
        render(data)
    }
    xhr.send()
}