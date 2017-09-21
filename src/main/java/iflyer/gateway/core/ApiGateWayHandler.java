package iflyer.gateway.core;

import com.google.gson.Gson;
import org.reflections.ReflectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Set;

/**
 * @Package: iflyer.gateway.core
 * @Description: 处理api请求
 * @author: liuxin
 * @date: 2017/9/17 下午7:14
 */
@Component
public class ApiGateWayHandler implements ApplicationContextAware {

    private static final String APINAME = "apiName";
    public static final String PARAMTER = "paramter";
    public static final String CONTENT = "application/json";
    private static ApiStore apiStore;
    private static final Gson gson = new Gson();

    public void handle(HttpServletRequest request, HttpServletResponse response) {
        String apiName = request.getParameter(APINAME);
        String parameter = request.getParameter(PARAMTER);
        String method = request.getMethod();
        if (!checkMethod(apiName)) {
            String format = String.format("{\"message\":\"未发现该API:%s\"}", apiName);
            responseBuiler(response, "", format);
            return;
        }
        ApiStore.ApiRunnable apiRunable = apiStore.findApiRunable(apiName);
        if (!checkRequestMethod(apiRunable,method)){
            String format = String.format("{\"message\":\"当前方法:%s,不被允许\"}", method);
            responseBuiler(response,"",format);
            return;
        }
        Object resultObj = getJsonOrString(response, apiRunable, parameter);
        responseBuiler(response, CONTENT, resultObj);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        apiStore = new ApiStore(applicationContext);
    }

    public boolean checkMethod(String apiName) {
        ApiStore.ApiRunnable apiRunable = apiStore.findApiRunable(apiName);
        if (ObjectUtils.isEmpty(apiRunable)) {
            return false;
        }
        return true;
    }

    public void responseBuiler(HttpServletResponse response, String contentType, Object result) {
        String type = contentType == null || contentType == "" ? "application/json" : contentType;
        response.setContentType(type);
        try {
            if (result instanceof String) {
                response.getOutputStream().write(((String) result).getBytes());
            } else {
                response.getOutputStream().write(gson.toJson(result).getBytes());
            }
        } catch (Exception e) {

        }
    }

    public boolean checkRequestMethod(ApiStore.ApiRunnable apiRunnable, String method) {
        return apiRunnable.getMethod().equalsIgnoreCase(method);
    }

    public Object getJsonOrString(HttpServletResponse response, ApiStore.ApiRunnable apiRunable, String parameter) {
        Object result = null;
        Object target = apiRunable.getTarget();
        Method targetMethod = apiRunable.getTargetMethod();
        Set<Method> methods = ReflectionUtils.getMethods(target.getClass(), ReflectionUtils.withName(targetMethod.getName()));
        Iterator<Method> iterator = methods.iterator();
        Method method = null;
        while (iterator.hasNext()) {
            method = iterator.next();
        }
        Class<?>[] parameterTypes = method.getParameterTypes();
        try {
            Class<?> aClass = Class.forName(parameterTypes[0].getName());
            Class<String> stringClass = String.class;
            if (stringClass == aClass) {
                result = apiRunable.run(parameter);
            } else {
                //不为String类型就要强制装换
                Object json = null;
                try {
                    json = gson.fromJson(parameter, aClass);
                    result = apiRunable.run(json);
                } catch (Exception e) {
                    String format = String.format("{\"message\":\"参数类型转换错误,请确认参数类型为:%s\"}", aClass.getName());
                    result=format;
                }
            }
        } catch (ClassNotFoundException cnfe) {

        } catch (IllegalAccessException iae) {

        } catch (InvocationTargetException it) {

        }
        return result;
    }
}
