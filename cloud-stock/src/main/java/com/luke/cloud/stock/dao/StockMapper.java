package com.luke.cloud.stock.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.luke.cloud.stock.bean.Stock;
import org.apache.ibatis.annotations.Param;
import java.util.Date;

public interface StockMapper extends BaseMapper<Stock> {

    int lockStock(@Param("id") String id, @Param("lockStock") Integer lockStock, @Param("time") Date time);

    int lockStockConfirm(@Param("productName") String productName, @Param("lockStock") Integer lockStock, @Param("time") Date time);

    int lockStockCancel(@Param("productName") String productName, @Param("lockStock") Integer lockStock, @Param("time") Date time);

}
