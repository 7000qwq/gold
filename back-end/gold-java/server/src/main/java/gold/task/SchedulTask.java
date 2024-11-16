package gold.task;

import com.resend.core.exception.ResendException;
import gold.entity.MinutePrice;
import gold.mapper.GoldPriceMapper;
import gold.mapper.UserMapper;
import gold.service.GoldPriceService;
import gold.utils.EmailUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class SchedulTask {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private GoldPriceMapper goldPriceMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private EmailUtil emailUtil;

    @Autowired
    private GoldPriceService goldPriceService;

    private BigDecimal lastPrice = BigDecimal.ZERO;
    private LocalDateTime lastTime;

    @Scheduled(cron = "0 * * * * ?")
    public void getPirce() throws IOException, InterruptedException, ResendException {

        BigDecimal bigDecimalData = goldPriceService.newestPrice();

        // 如果reminder,检查价格[需要检查所有用户redis信息,需要先在user表里把全部user_id取出],如果满足发送邮件
        List<Long> list = userMapper.getAllId();
        for (Long id : list) {
            Integer emailNotification = (Integer) redisTemplate.opsForHash().get(id, "emailNotification");
            if (emailNotification != null && emailNotification == 1) {
                BigDecimal highPrice = (BigDecimal) redisTemplate.opsForHash().get(id, "highPrice");
                BigDecimal lowPrice = (BigDecimal) redisTemplate.opsForHash().get(id, "lowPrice");
                if ( (highPrice != null && highPrice.compareTo(bigDecimalData) != 1) || (lowPrice != null && lowPrice.compareTo(bigDecimalData) != -1) ) {
                    if ( (lastTime == null || Duration.between(lastTime, LocalDateTime.now()).toHours() > 6 ) && lastPrice.subtract(bigDecimalData).abs().compareTo(BigDecimal.ONE) == 1 ) {

                        log.info("此处应发邮件提醒goldPrice:{}", bigDecimalData);
                        // emailUtil.sendReminderMail(userMapper.getById(id).getEmail(), bigDecimalData);
                        lastTime = LocalDateTime.now();
                        lastPrice = bigDecimalData;
                    }
                }
            }
        }

        // 向minute_price插入数据
        MinutePrice minutePrice = new MinutePrice(LocalDateTime.now(), bigDecimalData);
        goldPriceMapper.insert(minutePrice);

        /*
        // 通过websocket主动向客户端(前端)发送消息
        webSocketServer.sendMessageToClient("/topic/gold-price", bigDecimalData);
         */

        Map<String, Object> message = new HashMap<>();
        message.put("price", bigDecimalData);
        message.put("timestamp", System.currentTimeMillis()); // 当前时间戳
        rabbitTemplate.convertAndSend("gold-price-exchange", "gold-price-routing-key", message);

    }


}
