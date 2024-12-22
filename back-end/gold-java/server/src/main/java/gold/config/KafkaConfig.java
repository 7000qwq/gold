package gold.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

    // 定义主题：gold-price-topic
    @Bean
    public NewTopic goldPriceTopic() {
        // 参数：主题名称，分区数，副本因子
        return new NewTopic("gold-price-topic", 3, (short) 1);
    }

    // 定义主题：sent-email-topic
    @Bean
    public NewTopic sentEmailTopic() {
        return new NewTopic("sent-email-topic", 1, (short) 1);
    }
}
