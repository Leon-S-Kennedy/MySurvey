var xhr = new XMLHttpRequest()
xhr.open('get', '/activity/list.json')
xhr.onload = function() {
    var data = JSON.parse(this.responseText)
    var oContainer = document.querySelector('.container')
    var list = data.activityList
    for (var i in list) {
        var a = list[i]

        var html = `<div>${a.aid} ${a.title} ${a.state} ${a.startedAt} ${a.endedAt}</div>`
        oContainer.innerHTML += html
    }
}
xhr.send()