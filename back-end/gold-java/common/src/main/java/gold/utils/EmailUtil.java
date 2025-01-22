package gold.utils;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;

@Component
public class EmailUtil {

    @Value("${gold.resend.ak}")
    private String ak;

    @Value("${gold.resend.from}")
    private String from;

    public void sendReminderMail(String to, BigDecimal price) throws ResendException {
        Resend resend = new Resend(ak);

        // 读取模板文件内容
        String html = loadTemplate("template/reminder_email.html");
        if (html != null) {
            // 替换 [[PRICE]] 占位符为实际价格
            html = html.replace("[[${bigDecimalData}]]", price.toPlainString());
        }

        // 构建邮件内容
        CreateEmailOptions createEmailOptions = CreateEmailOptions.builder()
                .from(from)
                .to(to)
                .subject("Gold Price Notification")
                .html(html)
                .build();

        CreateEmailResponse data = resend.emails().send(createEmailOptions);
    }

    public void sendSignupMail(String to, String url) throws ResendException {
        Resend resend = new Resend(ak);

        // 读取模板文件内容

        String html = loadTemplate("template/signup_email.html");
        // 替换 [[verificationLink]] 占位符为带token的验证url
        if (html != null) html = html.replace("[[verificationLink]]", url);

        // 构建邮件内容
        CreateEmailOptions createEmailOptions = CreateEmailOptions.builder()
                .from(from)
                .to(to)
                .subject("Sign up Verification")
                .html(html)
                .build();

        CreateEmailResponse data = resend.emails().send(createEmailOptions);
    }

    // 加载 HTML 模板
    private String loadTemplate(String filePath) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
            return content.toString();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace(); // 打印异常日志，方便调试
            return null; // 如果文件加载失败，返回null
        }
    }

    public void sendBuyMail(String mail, BigDecimal price) throws ResendException {
        Resend resend = new Resend(ak);
        String message = "没有价格在xxx的持仓，当前金价为xxx.xx，请买入";
        // 获取整数部分和带两位小数的字符串
        String integerPart = price.toBigInteger().toString();
        String strPrice = price.toString();

        // 替换 message 中的 xxx 和 xxx.xx
        message = message.replace("xxx.xx", strPrice)
                .replace("xxx", integerPart);

        // 构建邮件内容
        CreateEmailOptions createEmailOptions = CreateEmailOptions.builder()
                .from(from)
                .to(mail)
                .subject("Gold Price: BUY")
                .html(message)
                .build();

        CreateEmailResponse data = resend.emails().send(createEmailOptions);
    }

    public void sendSellMail(String mail, BigDecimal price, BigDecimal goldPrice, BigDecimal weight) throws ResendException {

        Resend resend = new Resend(ak);
        String message = "有价格为yyy.yy的持仓x.xxxx克，当前金价为xxx.xx，请卖出";
        // 获取字符串
        String strPrice = price.toString();
        String strGoldPrice = goldPrice.toString();
        String strWeight = weight.toString();

        // 替换 message 中的 xxx 和 xxx.xx
        message = message.replace("xxx.xx", strPrice)
                .replace("yyy.yy", strGoldPrice)
                .replace("x.xxxx", strWeight);

        // 构建邮件内容
        CreateEmailOptions createEmailOptions = CreateEmailOptions.builder()
                .from(from)
                .to(mail)
                .subject("Gold Price: SELL")
                .html(message)
                .build();

        CreateEmailResponse data = resend.emails().send(createEmailOptions);
    }
}