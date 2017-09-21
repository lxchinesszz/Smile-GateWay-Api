package iflyer.gateway.http;

/**
 * @Package: iflyer.gateway.http
 * @Description: ${todo}
 * @author: liuxin
 * @date: 2017/9/19 下午3:48
 */
public enum  RequestMethod {
    GET("get"),POST("post"),PUT("put");

    private String value;

    private RequestMethod(String method){
        this.value=method;
    }

    public String getValue() {
        return value;
    }
}
