package gold.utils;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
        try {
            return new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource(filePath).toURI())));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }
}