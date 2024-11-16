package gold.message;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GoldPriceConsumer {

    private final SimpMessagingTemplate messagingTemplate;

    public GoldPriceConsumer(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @RabbitListener(queues = "gold-price-queue") // 监听队列
    public void consumeGoldPrice(Map<String, Object> message) {
        System.out.println("Consumed message from RabbitMQ: " + message);

        // 推送到 WebSocket
        messagingTemplate.convertAndSend("/topic/gold-price", message);
    }
}
