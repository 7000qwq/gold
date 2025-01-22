package gold;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration.class,
        //org.springframework.boot.autoconfigure.kafka.KafkaReactiveStreamsAutoConfiguration.class
})
@EnableTransactionManagement
@EnableScheduling
@Slf4j
@EnableCaching
public class GoldApplication {
    public static void main(String[] args) {
        SpringApplication.run(GoldApplication.class, args);
        log.info("server started");
    }

}
