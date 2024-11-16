package gold.service.impl;

import gold.constant.MessageConstant;
import gold.exception.PriceNotFoundException;
import gold.service.GoldPriceService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class GoldPriceServiceImpl implements GoldPriceService {


    @Override
    public BigDecimal newestPrice() throws IOException, InterruptedException {

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

        return bigDecimalData;

    }


}
