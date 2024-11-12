package gold.task;

import com.resend.core.exception.ResendException;
import gold.constant.MessageConstant;
import gold.entity.MinutePrice;
import gold.exception.PriceNotFoundException;
import gold.mapper.GoldPriceMapper;
import gold.mapper.UserMapper;
import gold.utils.EmailUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class SchedulTask {

    @Autowired
    private GoldPriceMapper goldPriceMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private EmailUtil emailUtil;

    private BigDecimal lastPrice = BigDecimal.ZERO;
    private LocalDateTime lastTime;

    @Scheduled(cron = "0 * * * * ?")
    public BigDecimal getPirce() throws IOException, InterruptedException, ResendException {

        String url = "https://www.sge.com.cn/sjzx/yshqbg";

        // 1. 使用 HttpClient 发送 GET 请求
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        // 2. 发送请求并获取响应的 HTML 内容
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String html = response.body();

        // 3. 使用 Jsoup 解析 HTML 内容
        Document doc = Jsoup.parse(html);

        // 4. 找到包含文本 "Au99.99" 的 <td> 元素
        Element auElement = doc.select("td:contains(Au99.99)").first();
        BigDecimal bigDecimalData = BigDecimal.ZERO;
        if (auElement != null) {
            // 5. 找到包含目标数据的下一个兄弟元素
            Element targetElement = auElement.nextElementSibling();

            if (targetElement != null) {
                String data = targetElement.text();
                System.out.println("获取的实时价格: " + data);
                bigDecimalData = new BigDecimal(data);
            } else {
                throw new PriceNotFoundException(MessageConstant.CANNOT_GET_PRICE);
            }
        } else {
            throw new PriceNotFoundException(MessageConstant.CANNOT_GET_PRICE);
        }

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
        //

        // 向minute_price插入数据
        MinutePrice minutePrice = new MinutePrice(LocalDateTime.now(), bigDecimalData);
        goldPriceMapper.insert(minutePrice);

        return bigDecimalData;
    }


}
