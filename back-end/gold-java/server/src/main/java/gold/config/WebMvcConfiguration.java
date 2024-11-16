package gold.config;

import gold.interceptor.JwtTokenUserInterceptor;
import gold.json.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

/**
 * 配置类，注册web层相关组件
 */
@Configuration
@Slf4j
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    @Autowired
    private JwtTokenUserInterceptor jwtTokenUserInterceptor;

    /**
     * 注册自定义拦截器
     *
     * @param registry
     */
    protected void addInterceptors(InterceptorRegistry registry) {
        log.info("Start registering custom interceptors...");

        registry.addInterceptor(jwtTokenUserInterceptor)
                .addPathPatterns("/goldPrice/position")
                .addPathPatterns("/transaction/**")
                .addPathPatterns("/user/**")
                .excludePathPatterns("/user/login")
                .excludePathPatterns("/user/verify")
                .excludePathPatterns("/user/signup");
    }

    // 设置静态资源映射
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }


    /**
     * 扩展Spring MVC框架的消息转化器
     * @param converters
     */
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters){

        log.info("扩展消息转换器...");

        // 创建一个消息转换器对象
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();

        // 为消息转换器设置对象转换器，对象转换器把java对象序列化为json数据
        converter.setObjectMapper(new JacksonObjectMapper());

        //设置的消息转换器还没有交给框架，需要把消息转换器加到converters容器中
        converters.add(0, converter);
        // 框架里自带了一些消息转换器，我们加的这个排在最后，默认使用不到，在前面加个0就排第一了
    }
}
