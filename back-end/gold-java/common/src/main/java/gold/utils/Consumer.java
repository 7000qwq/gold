package gold.utils;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class Consumer<T> {

    private final SimpMessagingTemplate messagingTemplate;

    public Consumer(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * 监听指定队列的消息，并处理。
     *
     * @param queueName 队列名称
     * @param message   消息内容
     */
    @RabbitListener(queues = "#{queueName}")  // 通过队列名称动态监听
    public void consume(String queueName, T message) {
        // 可根据队列名称、消息类型等进行不同的处理
        messagingTemplate.convertAndSend("/topic/" + queueName, message);
    }
}
