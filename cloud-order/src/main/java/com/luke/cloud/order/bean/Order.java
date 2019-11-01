package com.luke.cloud.order.bean;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Descrtption Order
 * @Author luke
 * @Date 2019/9/23
 **/
@Data
@TableName(value = "tbl_order")
public class Order implements Serializable {

    private static final long serialVersionUID = -9193081934480304568L;

    /**订单号*/
    @TableId
    private String id;

    /**用户*/
    private String userId;

    /**商品*/
    private String productId;

    /**商品数量*/
    private Integer count;

    /**总金额*/
    private BigDecimal amount;

    /**状态 1：未支付 2：支付中 3：支付失败 4：支付成功*/
    private Integer status;

    private Date createTime;

    private Date updateTime;

}
