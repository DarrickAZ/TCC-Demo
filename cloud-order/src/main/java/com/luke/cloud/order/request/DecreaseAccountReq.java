package com.luke.cloud.order.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Descrtption DecreaseAccountReq
 * @Author luke
 * @Date 2019/9/25
 **/
@Data
public class DecreaseAccountReq implements Serializable {

    private static final long serialVersionUID = 7087168388086184808L;

    @NotNull
    private String orderId;

    @NotNull
    private String username;

    @NotNull
    private BigDecimal amount;

}
