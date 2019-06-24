package com.java.websocket_helloworld.controller;

import com.java.websocket_helloworld.socket.SpringWebSocketHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.socket.TextMessage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
public class TestController {

    private SpringWebSocketHandler socketHandler;

    @RequestMapping("/hello/{name}")
    public String hello(@PathVariable String name) {
        return "hello " + name;
    }


    @RequestMapping("/websocket/login")
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String username = request.getParameter("username");
        System.out.println(username+"登录");
        //HttpSession session = request.getSession(false);
        //session.setAttribute("SESSION_USERNAME", username);
        //response.sendRedirect("/quicksand/jsp/websocket.jsp");
        return new ModelAndView("websocket");
    }

    @RequestMapping("/websocket/send")
    public String send(HttpServletRequest request) {
        String username = request.getParameter("username");
        socketHandler.sendMessageToUser(username, new TextMessage("你好，测试！！！！"));
        return null;
    }
}
