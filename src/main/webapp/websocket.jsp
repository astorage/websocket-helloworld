<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>Insert title here</title>
</head>
<script type="text/javascript" src="http://cdn.bootcss.com/jquery/3.1.0/jquery.min.js"></script>
<script type="text/javascript" src="http://cdn.bootcss.com/sockjs-client/1.1.1/sockjs.js"></script>
<script type="text/javascript">
    var websocket = null;
    if ('WebSocket' in window) {
        websocket = new WebSocket("ws://10.10.2.176:8080/websocket/webSocketServer");
    }
    else if ('MozWebSocket' in window) {
        websocket = new MozWebSocket("ws://10.10.2.176:8080/websocket/webSocketServer");
    }
    else {
        websocket = new SockJS("http://10.10.2.176:8080/sockjs/webSocketServer");
    }
    websocket.onopen = onOpen;
    websocket.onmessage = onMessage;
    websocket.onerror = onError;
    websocket.onclose = onClose;

    function onOpen(openEvt) {
        //alert(openEvt.Data);
    }

    function onMessage(evt) {
        var inputMsg = document.getElementById("inputMsg");
        inputMsg.value = evt.data;
        //alert();
    }
    function onError() {}
    function onClose() {}

    function doSend() {
        if (websocket.readyState == websocket.OPEN) {
            var msg = document.getElementById("inputMsg").value;
            websocket.send(msg);//调用后台handleTextMessage方法
            //alert("发送成功!");
        } else {
            //alert("连接失败!");
        }
    }
    window.close=function()
    {
        websocket.onclose();
    }

</script>
请输入：<textarea rows="5" cols="40" id="inputMsg" name="inputMsg"></textarea>
<button onclick="doSend();">发送</button>
</body>
</html>