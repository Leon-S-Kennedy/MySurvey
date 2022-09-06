function render(data) {
    document.querySelector('#survey-title').textContent = data.sid + ' | ' + data.title
    document.querySelector('#survey-brief').textContent = data.brief
    document.querySelector('#sid').value = data.sid

    var ol = document.querySelector('ol')
    for (var i in data.questionList) {
        var q = data.questionList[i]

        var oLi = document.createElement('li')
        var oInput = document.createElement('input')
        oInput.type = 'checkbox'
        oInput.name = 'bind-qid'
        oInput.value = q.qid
        if (q.bounden) {
            oInput.checked = true
        }

        var oText = document.createTextNode(q.question)

        oLi.appendChild(oInput)
        oLi.appendChild(oText)
        ol.append(oLi)
    }
}

window.onload = function() {
    var xhr = new XMLHttpRequest()
    // sid 和 page
    xhr.open('get', '/survey/bind.json' + location.search);
    xhr.onload = function() {
        var data = JSON.parse(this.responseText)
        render(data)
    }
    xhr.send()

    document.querySelector('button').onclick = function(evt) {
        // 停止这个按钮的默认动作 —— 提交表单
        evt.preventDefault()
        evt.stopPropagation()

        var oForm = document.querySelector('form')

        var ol = document.querySelector('ol')
        // ol.querySelectorAll(...)  以 ol 为根的树查找
        // input:not(:checked)   找到 ol 下所有没有 checked 的 input 标签
        var uncheckedList = ol.querySelectorAll('input:not(:checked)')

        for (var i = 0; i < uncheckedList.length; i++) {
            var oInput = uncheckedList[i]
            var qid = oInput.value


            var oHidden = document.createElement('input')
            oHidden.name = 'unbind-qid'
            oHidden.value = qid
            oHidden.type = 'hidden'
            oForm.appendChild(oHidden)
        }



        oForm.submit()
    }
}