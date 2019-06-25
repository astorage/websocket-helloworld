package com.java.websocket_helloworld.socket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 客户端连接,关闭,错误,发送等方法,重写这几个方法即可实现自定义业务逻辑
 */
@Slf4j
@Component
public class SpringWebSocketHandler extends TextWebSocketHandler {
    private static final Map<String,WebSocketSession> userMap;//这个会出现性能问题，最好用Map来存储，key用userid
    static {
        userMap = new HashMap<>();
    }

    public SpringWebSocketHandler() {
        // TODO Auto-generated constructor stub
    }

    /**
     * 连接成功时候，会触发页面上onopen方法
     */
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        userMap.put(session.getAttributes().get("WEBSOCKET_USERNAME").toString(), session);
        System.out.println("connect to the websocket success......当前数量:"+userMap.size());
        //这块会实现自己业务，比如，当用户登录后，会把离线消息推送给用户
        TextMessage returnMessage = new TextMessage("你将收到的离线");
        session.sendMessage(returnMessage);
    }

    /**
     * 关闭连接时触发
     */
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        log.debug("websocket connection closed......");
        String username= (String) session.getAttributes().get("WEBSOCKET_USERNAME");
        log.info("用户"+username+"已退出！");
        userMap.remove(session.getAttributes().get("WEBSOCKET_USERNAME").toString(), session);
        log.info("剩余在线用户"+userMap.size());
    }

    /**
     * js调用websocket.send时候，会调用该方法
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String username= (String) session.getAttributes().get("WEBSOCKET_USERNAME");
        despatchMessage(username, message.getPayload());
        super.handleTextMessage(session, message);
    }

    /**
     * 向某个人发送消息，消息用=隔开，前面是接收消息的人，后面是消息内容
     * @param fromUser
     * @param message
     */
    private void despatchMessage(String fromUser, String message) {
        log.info(fromUser + "发送消息：" + message);
        String[] userMessage = message.split("=");
        if (userMessage.length == 1) {
            sendMessageToUsers(new TextMessage(userMessage[0]));
        }else {
            for (String user : userMessage[0].split(",")) {
                sendMessageToUser(user, new TextMessage(userMessage[1]));
            }
        }
    }



    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if(session.isOpen()){session.close();}
        log.debug("websocket connection closed......");
        userMap.remove(session.getAttributes().get("WEBSOCKET_USERNAME").toString(), session);
    }

    public boolean supportsPartialMessages() {
        return false;
    }


    /**
     * 给某个用户发送消息
     *
     * @param userName
     * @param message
     */
    public void sendMessageToUser(String userName, TextMessage message) {
        WebSocketSession webSocketSession = userMap.get(userName);
        if (webSocketSession != null) {
            try {
                if (webSocketSession.isOpen()) {
                    webSocketSession.sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 给所有在线用户发送消息
     *
     * @param message
     */
    public void sendMessageToUsers(TextMessage message) {
        for (WebSocketSession user : userMap.values()) {
            try {
                if (user.isOpen()) {
                    user.sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
