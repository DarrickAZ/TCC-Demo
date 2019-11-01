package com.luke.cloud.account.bean;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Descrtption AccountFreeze
 * @Author luke
 * @Date 2019/9/25
 **/
@Data
@TableName(value = "tbl_account_freeze")
public class AccountFreeze implements Serializable {

    private static final long serialVersionUID = 3827297878439777762L;

    @TableId
    private String id;

    private String userId;

    private String orderId;

    private BigDecimal freezeAmount;

    private Date createTime;

    private Date updateTime;

}
