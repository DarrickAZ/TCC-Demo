package com.luke.cloud.order.inter;

import com.luke.cloud.order.request.DecreaseStockReq;
import com.luke.milo.core.annotation.MiloTCC;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Map;

@FeignClient(value = "cloud-stock",fallback = StockInterHystrix.class)
public interface StockInter {

    @MiloTCC
    @PostMapping("/stock/decrease")
    String decreaseStock(@RequestBody @Validated DecreaseStockReq req);

    @GetMapping("/stock/findStockByName")
    Map<String,Object> findStockByName(@RequestParam("productName") String productName);

}
