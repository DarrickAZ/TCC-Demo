package com.luke.cloud.order.inter;

import com.luke.cloud.order.request.DecreaseStockReq;
import com.luke.milo.common.exception.MiloHystrixException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.Map;

/**
 * @Descrtption StockInterHystrix
 * @Author luke
 * @Date 2019/9/28
 **/
@Slf4j
@Component
public class StockInterHystrix implements StockInter{

    @Override
    public String decreaseStock(DecreaseStockReq req) {
        log.error("StockInterHystrix decreaseStock,req:{}",req);
        //熔断需要抛出MiloHystrixException异常
        throw new MiloHystrixException("远程调用StockInter超时");
    }

    @Override
    public Map<String, Object> findStockByName(String productName) {
        log.error("StockInterHystrix findStockByName,productName:{}",productName);
        throw new MiloHystrixException("远程调用StockInter超时");
    }

}
