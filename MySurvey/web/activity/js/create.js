function render(surveyList) {
    var oSelect = document.querySelector('select')
    for (var i in surveyList) {
        var s = surveyList[i]

        var html = `<option value=${s.sid}>${s.title}</option>`

        oSelect.innerHTML += html
    }
}

window.onload = function() {
    var xhr = new XMLHttpRequest()
    xhr.open('get', '/survey/simple-list.json')
    xhr.onload = function() {
        var data = JSON.parse(this.responseText)
        render(data.surveyList)
    }
    xhr.send()
}