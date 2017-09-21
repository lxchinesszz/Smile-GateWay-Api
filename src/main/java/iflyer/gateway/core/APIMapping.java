package iflyer.gateway.core;

import iflyer.gateway.http.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Package: iflyer.gateway.core
 * @Description: api接口
 * @author: liuxin
 * @date: 2017/9/17 下午6:51
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface APIMapping {
    String value();
    RequestMethod method();
}
