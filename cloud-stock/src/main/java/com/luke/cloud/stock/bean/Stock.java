package com.luke.cloud.stock.bean;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Descrtption Stock
 * @Author luke
 * @Date 2019/9/25
 **/
@Data
@TableName(value = "tbl_stock")
public class Stock implements Serializable {

    private static final long serialVersionUID = -4501938787939428308L;

    @TableId
    private String id;

    private String productName;

    private Integer totalStock;

    private Integer lockStock;

    private BigDecimal price;

    private Date createTime;

    private Date updateTime;

}
