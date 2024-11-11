package gold.utils;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class Publisher<T> {

    private final RabbitTemplate rabbitTemplate;

    public Publisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * 发送消息到指定的队列。
     *
     * @param queueName 队列名称
     * @param message   发送的消息
     */
    public void publish(String queueName, T message) {
        rabbitTemplate.convertAndSend(queueName, message);
    }
}
