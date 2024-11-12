package gold.websocket;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketServer {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketServer(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * 向指定的 WebSocket 目标发送消息。
     *
     * @param topic   消息目标路径（例如：/topic/gold-price）
     * @param message 消息内容
     */
    public <T> void sendMessageToClient(String topic, T message) {
        messagingTemplate.convertAndSend(topic, message);
    }
}
