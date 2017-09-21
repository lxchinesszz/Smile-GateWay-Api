package iflyer.gateway.modle;

import lombok.Builder;
import lombok.Data;

/**
 * @Package: iflyer.gateway.modle
 * @Description: ${todo}
 * @author: liuxin
 * @date: 2017/9/19 上午11:10
 */
@Data
@Builder
public class OrderInfo {
    private String id;
    private String name;
    private double price;
}
