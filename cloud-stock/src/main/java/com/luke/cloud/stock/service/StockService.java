package com.luke.cloud.stock.service;

import com.luke.cloud.stock.request.DecreaseStockReq;
import java.util.Map;

public interface StockService {

    Integer decrease(DecreaseStockReq req);

    Map<String, Object> findStockByName(String productName);

}
