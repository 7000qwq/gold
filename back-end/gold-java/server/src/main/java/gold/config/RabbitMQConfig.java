package gold.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // 定义交换机
    @Bean
    public TopicExchange goldPriceExchange() {
        return new TopicExchange("gold-price-exchange");
    }

    // 定义队列
    @Bean
    public Queue goldPriceQueue() {
        return new Queue("gold-price-queue");
    }

    // 将队列绑定到交换机
    @Bean
    public Binding binding1(Queue goldPriceQueue, TopicExchange goldPriceExchange) {
        return BindingBuilder.bind(goldPriceQueue).to(goldPriceExchange).with("gold-price-routing-key");
    }



    // 定义交换机
    @Bean
    public TopicExchange sentEmailExchange() {
        return new TopicExchange("sent-email-exchange");
    }

    // 定义队列
    @Bean
    public Queue sentEmailQueue() {
        return new Queue("sent-email-queue");
    }

    // 将队列绑定到交换机
    @Bean
    public Binding binding2(Queue sentEmailQueue, TopicExchange sentEmailExchange) {
        return BindingBuilder.bind(sentEmailQueue).to(sentEmailExchange).with("sent-email-routing-key");
    }
}

