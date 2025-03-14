package gold.service.impl;

import gold.constant.MessageConstant;
import gold.exception.PriceNotFoundException;
import gold.service.GoldPriceService;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class GoldPriceServiceImpl implements GoldPriceService {

    @Value("${gold.python-http.url}")
    private String python_url;

    // 从上海黄金交易所网页爬数据
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

    @Override
    public BigDecimal getCurrentGoldPrice() throws IOException, InterruptedException {
        // 创建 HttpClient 实例
        HttpClient client = HttpClient.newHttpClient();

        // 开发环境下用 http://localhost:5000/get_price
        // 构建 GET 请求
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(python_url))  // Python 接口的 URL
                .GET()
                .build();

        // 发送请求并获取响应
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            // 解析响应 JSON
            JSONObject jsonResponse = new JSONObject(response.body());
            String priceStr = jsonResponse.getString("price");

            // 将价格转换为 BigDecimal 并返回
            return new BigDecimal(priceStr);
        } else {
            throw new IOException("Failed to get price: " + response.statusCode());
        }
    }

    // 从京东金融网页爬数据
    @Override
    public BigDecimal newestJDPrice() throws IOException, InterruptedException {
        // 1. 接口地址
        String url = "https://ms.jr.jd.com/gw/generic/hj/h5/m/latestPrice?reqData=%7B%7D";

        // 2. 使用 HttpClient 发送 GET 请求
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        // 3. 获取响应内容（JSON 格式）
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String json = response.body();

        // 4. 解析 JSON
        //    这里演示用 org.json，如果用 Jackson 或其他 JSON 库都可以
        JSONObject jsonObject = new JSONObject(json);
        JSONObject datas = jsonObject
                .getJSONObject("resultData")
                .getJSONObject("datas");

        // 5. 提取 price 字段
        String priceStr = datas.getString("price");
        System.out.println("获取的实时金价: " + priceStr);

        // 6. 转换成 BigDecimal 返回
        return new BigDecimal(priceStr);
    }
}
