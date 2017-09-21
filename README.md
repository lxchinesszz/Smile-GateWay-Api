> 以HTTP接口形式的应用,是目前大部分中小型企业最常见的微服务夸语言交互的实现方式
>
> 即:定义多个接口,外部调用,经网关解析进行分发，小编遇到的这种情况是，有多个服务，每个服务都需要单独有网关开墙，很是头疼，每上线一个服务都需要网关配置，极其头疼，再次实现一种暴露一个接口，通过参数来实现调用不同的方法的案例

[GITHUB项目地址](https://github.com/lxchinesszz/Smile-GateWay-Api.git)

## 目录





### 思路分析

![流程图](http://note.youdao.com/yws/api/personal/file/WEB0e5b77dfa154d631dd892fa480032377?method=download&shareKey=cabda2685b8a79be3381cbedaf82f0ad)

实现方案:

- 1. 自定义注解 APiMapping
  2. 自定义ApiGateWayServlet
  3. 利用 Spring IOC 拆分方法并与 ApiMaping 做绑定由 ApiStore中HashMap维护



### 注解定义及利用IOC绑定注解与方法

api注解: `APIMapping`

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface APIMapping {
    String value();
    RequestMethod method();
}
```

通过注解对业务方法标记

```java
  @APIMapping(value = "biz.api.order",method = RequestMethod.GET)
    public OrderInfo getOrderInfo(String orderId) {
        OrderInfo orderInfo = OrderInfo.builder().id(orderId).name("测试订单").price(12.2).build();
        return orderInfo;
    }
    @APIMapping(value = "biz.api.order2",method = RequestMethod.POST)
    public OrderInfo getOrderDo(OrderInfo orderInfo){
        return orderInfo;
    }
```

利用Spring 上下文对标记的方法进行绑定
初始化时候，扫描APIMapping接口

```
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
```

重写自定义Servlet方法中的POST和GET

```
public class ApiGateWayServlet extends HttpServlet {
    private ApplicationContext applicationContext;

    private ApiGateWayHandler apiGateWayHandler;

    @Override
    public void init() throws ServletException {
        super.init();
        applicationContext = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
        apiGateWayHandler = applicationContext.getBean(ApiGateWayHandler.class);

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        apiGateWayHandler.handle(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        apiGateWayHandler.handle(req,resp);
    }
}

```

根据接口绑定获取到执行的方法,利用反射执行

```
 public class ApiRunnable {
        private String apiName;
        private Method targetMethod;
        private String targetName;
        private Object target;
        private String Method;
```

```
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
                
        ....
```