package com.luke.cloud.order.request;

import lombok.Data;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Descrtption PaymentOrderReq
 * @Author luke
 * @Date 2019/9/23
 **/
@Data
public class PaymentOrderReq implements Serializable {

    private String orderId;

    @NotNull
    private String username;

    @NotNull
    private String productName;

    @NotNull
    private Integer count;

}
