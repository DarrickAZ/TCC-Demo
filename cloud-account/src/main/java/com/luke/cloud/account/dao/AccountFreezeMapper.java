package com.luke.cloud.account.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.luke.cloud.account.bean.AccountFreeze;
import org.apache.ibatis.annotations.Param;

public interface AccountFreezeMapper extends BaseMapper<AccountFreeze> {

    int deleteFreezeRecord(@Param("orderId") String orderId);

}
