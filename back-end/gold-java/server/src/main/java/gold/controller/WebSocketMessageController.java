package gold.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketMessageController {

    @MessageMapping("/send-message") // 前端发送消息的路径
    @SendTo("/topic/gold-price")    // 广播到的订阅路径
    public String handleSendMessage(@Payload String message) {
        // 处理前端发来的消息
        System.out.println("Received message from client: " + message);

        // 返回数据广播给订阅者
        return "Processed message: " + message;
    }
}
