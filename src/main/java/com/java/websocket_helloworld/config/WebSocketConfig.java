package com.java.websocket_helloworld.config;

import com.java.websocket_helloworld.service.HelloWorldService;
import com.java.websocket_helloworld.socket.SpringWebSocketHandler;
import com.java.websocket_helloworld.socket.SpringWebSocketHandlerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private SpringWebSocketHandler springWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(springWebSocketHandler,"/websocket/webSocketServer").setAllowedOrigins("*").addInterceptors(new SpringWebSocketHandlerInterceptor());
        registry.addHandler(springWebSocketHandler, "/sockjs/webSocketServer").setAllowedOrigins("*").addInterceptors(new SpringWebSocketHandlerInterceptor()).withSockJS();
    }

    @Bean
    public HelloWorldService helloWorldService(){
        return new HelloWorldService();
    }

}
