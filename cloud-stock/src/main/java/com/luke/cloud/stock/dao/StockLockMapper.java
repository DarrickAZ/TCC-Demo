package com.luke.cloud.stock.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.luke.cloud.stock.bean.StockLock;
import org.apache.ibatis.annotations.Param;

public interface StockLockMapper extends BaseMapper<StockLock> {

    int deleteLockRecord(@Param("orderId") String orderId);

}
