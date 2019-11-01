package com.luke.cloud.stock.controller;

import com.luke.cloud.stock.request.DecreaseStockReq;
import com.luke.cloud.stock.service.StockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Descrtption StockController
 * @Author luke
 * @Date 2019/9/25
 **/
@Slf4j
@RestController
@RequestMapping("/stock")
public class StockController {

    @Autowired
    private StockService stockService;

    @PostMapping("/decrease")
    public String decreaseStock(@RequestBody @Validated DecreaseStockReq req){
        log.info("decreaseStock,req:{}",req);
        Integer decreaseResult = stockService.decrease(req);
        if(decreaseResult == null || decreaseResult == 0){
            return "failure";
        }
        return "success";
    }

    @GetMapping("/findStockByName")
    public Map<String,Object> findStockByName(@RequestParam("productName") String productName){
        return stockService.findStockByName(productName);
    }

}
