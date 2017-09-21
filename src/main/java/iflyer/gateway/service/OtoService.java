package iflyer.gateway.service;

import iflyer.gateway.core.APIMapping;
import iflyer.gateway.modle.OrderInfo;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @Package: iflyer.gateway.service
 * @Description: ${todo}
 * @author: liuxin
 * @date: 2017/9/19 上午11:06
 */
@Service
public interface OtoService {
    void payment(String orderId);

    OrderInfo getOrderInfo(String orderId);


    OrderInfo getOrderDo(OrderInfo orderInfo);

}
