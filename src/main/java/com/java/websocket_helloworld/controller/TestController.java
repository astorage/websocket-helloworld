package com.java.websocket_helloworld.controller;

import com.java.websocket_helloworld.service.HelloWorldService;
import com.java.websocket_helloworld.socket.SpringWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private SpringWebSocketHandler springWebSocketHandler;
    @Autowired
    private HelloWorldService helloWorldService;

    @RequestMapping("/hello/{name}")
    public String hello(@PathVariable String name) {
        return "hello " + name;
    }

    /**
     * 首页
     * @return
     */
    @RequestMapping("/")
    public ModelAndView index() {
        return new ModelAndView("index");
    }

    /**
     * 登录，主要是通过HttpSessionHandshakeInterceptor 记录session
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/websocket/login")
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String username = request.getParameter("username");
        System.out.println(username+"登录");
        HttpSession session = request.getSession(true);
        session.setAttribute("SESSION_USERNAME", username);
        return new ModelAndView("websocket");
    }

    /**
     * 向登录的一个人发送消息
     * @param request
     * @return
     */
    @RequestMapping("/websocket/send")
    public String send(HttpServletRequest request) {
        String username = request.getParameter("username");
        System.out.println(helloWorldService.seyHello());
        springWebSocketHandler.sendMessageToUser(username, new TextMessage("你好，测试！！！！"));
        return null;
    }

    /**
     * 测试向登录的人发送消息
     * @return
     */
    @RequestMapping("/websocket/send/group")
    public String sendToAllUser() {
        System.out.println(helloWorldService.seyHello());
        springWebSocketHandler.sendMessageToUsers(new TextMessage("你好，测试！！！！"));
        return null;
    }
}
