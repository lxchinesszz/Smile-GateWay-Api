package iflyer.gateway.util;

import org.springframework.context.ApplicationContext;

/**
 * @Package: spider.tjbank.util
 * @Description: 上下文对象
 * @author: liuxin
 * @date: 2017/9/13 下午5:29
 */
public class SpringContextUtils {
    private static ApplicationContext applicationContext;

    public static void setApplicationContext(ApplicationContext context) {
        applicationContext = context;
    }

    public static Object getBean(String beanId) {
        return applicationContext.getBean(beanId);
    }
}