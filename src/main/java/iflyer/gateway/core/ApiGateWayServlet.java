package iflyer.gateway.core;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Package: iflyer.gateway.core
 * @Description: ${todo}
 * @author: liuxin
 * @date: 2017/9/17 下午7:11
 */
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
