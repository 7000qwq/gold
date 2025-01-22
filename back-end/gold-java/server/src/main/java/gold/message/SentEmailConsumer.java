package gold.message;

import com.resend.core.exception.ResendException;
import gold.utils.EmailUtil;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SentEmailConsumer {

    @Autowired
    private EmailUtil emailUtil;

    // @KafkaListener(topics = "sent-email-topic", groupId = "email-group")
    @RabbitListener(queues = "sent-email-queue") // 监听队列
    public void consumeSentEmail(Map<String, String> message) throws ResendException {
        System.out.println("Consumed message from RabbitMQ: " + message);
        // System.out.println("Consumed message from Kafka: " + message);
        emailUtil.sendSignupMail(message.get("email"), message.get("link"));
    }
}
