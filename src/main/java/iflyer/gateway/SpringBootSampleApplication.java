package iflyer.gateway;

import iflyer.gateway.core.ApiGateWayServlet;
import iflyer.gateway.util.SpringContextUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * @Package: iflyer.gateway
 * @Description: 启动类
 * @author: liuxin
 * @date: 2017/9/17 下午7:10
 */
@SpringBootApplication
public class SpringBootSampleApplication {
    /**
     * 使用代码注册Servlet（不需要@ServletComponentScan注解）
     *
     * @return
     * @author SHANHY
     * @create  2016年1月6日
     */
    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        return new ServletRegistrationBean(new ApiGateWayServlet(), "/api");// ServletName默认值为首字母小写，即myServlet
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(SpringBootSampleApplication.class, args);
        SpringContextUtils.setApplicationContext(configurableApplicationContext);
    }
}
