package com.luke.cloud.order.service;

import com.luke.cloud.order.request.PaymentOrderReq;

public interface OrderService {

    void createOrder(PaymentOrderReq req);



}
