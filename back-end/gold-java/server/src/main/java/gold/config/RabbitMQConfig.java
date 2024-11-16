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
    public Binding binding(Queue goldPriceQueue, TopicExchange goldPriceExchange) {
        return BindingBuilder.bind(goldPriceQueue).to(goldPriceExchange).with("gold-price-routing-key");
    }
}

