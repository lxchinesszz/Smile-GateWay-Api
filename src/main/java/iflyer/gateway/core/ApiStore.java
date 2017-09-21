package iflyer.gateway.core;

import org.reflections.ReflectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

/**
 * @Package: iflyer.gateway.core
 * @Description: 收集api注解
 * @author: liuxin
 * @date: 2017/9/17 下午6:47
 */
public class ApiStore  {

    private ApplicationContext applicationContext;
    private HashMap<String ,ApiRunnable> apiMap=new HashMap<>();

    public ApiStore(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        loadApiFromSpringBeans();
    }

    /**
     * 对标记的API方法进行绑定
     */
    public void loadApiFromSpringBeans() {
        String[] names = applicationContext.getBeanDefinitionNames();
        Class<?> type;
        for (String name : names) {
            type = applicationContext.getType(name);
            for (Method method : type.getDeclaredMethods()) {
                APIMapping apiMapping = method.getDeclaredAnnotation(APIMapping.class);
                if (apiMapping!=null){
                    addApiItem(apiMapping,name,method);
                }
            }
        }
    }



    public ApiRunnable findApiRunable(String apiName){
        return apiMap.get(apiName);
    }

    private void addApiItem(APIMapping apiMapping,String beanName,Method method){
        ApiRunnable apiRunnable=new ApiRunnable();
        apiRunnable.setApiName(apiMapping.value());
        apiRunnable.setTargetMethod(method);
        apiRunnable.setTargetName(beanName);
        apiRunnable.setMethod(apiMapping.method().getValue());
        apiMap.put(apiMapping.value(),apiRunnable);

    }
    public class ApiRunnable {
        private String apiName;
        private Method targetMethod;
        private String targetName;
        private Object target;
        private String Method;

        /**
         * 通过反射执行
         * @param args
         * @return
         * @throws IllegalAccessException
         * @throws InvocationTargetException
         */
        public Object run(Object...args)throws IllegalAccessException,InvocationTargetException{
            if (target==null){
                target = applicationContext.getBean(targetName);
            }
           return targetMethod.invoke(target,args);
        }

        public Object getTarget() {
            if (target==null){
                target = applicationContext.getBean(targetName);
            }
            return target;
        }


        public String getMethod() {
            return Method;
        }

        public void setMethod(String method) {
            Method = method;
        }

        public void setTarget(Object target) {
            this.target = target;
        }

        public String getApiName() {
            return apiName;
        }

        public void setApiName(String apiName) {
            this.apiName = apiName;
        }

        public Method getTargetMethod() {
            return targetMethod;
        }

        public void setTargetMethod(Method targetMethod) {
            this.targetMethod = targetMethod;
        }

        public String getTargetName() {
            return targetName;
        }

        public void setTargetName(String targetName) {
            this.targetName = targetName;
        }
    }
}
