package com.luke.cloud.order.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Descrtption DecreaseStockReq
 * @Author luke
 * @Date 2019/9/25
 **/
@Data
public class DecreaseStockReq implements Serializable {

    private static final long serialVersionUID = -1357798122872533054L;

    @NotNull
    private String orderId;

    @NotNull
    private String productName;

    @NotNull
    private Integer stock;

}
