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

    // @KafkaListener(topics = "gold-price-topic", groupId = "my-group")
    @RabbitListener(queues = "gold-price-queue") // 监听队列 rabbitMQ
    public void consumeGoldPrice(Map<String, Object> message) {
        System.out.println("Consumed message from RabbitMQ: " + message);
        // System.out.println("Consumed message from Kafka: " + message);
        // 推送到 WebSocket
        messagingTemplate.convertAndSend("/topic/gold-price", message);
    }
}
