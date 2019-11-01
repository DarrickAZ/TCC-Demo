package com.luke.cloud.order.controller;

import com.luke.cloud.order.request.PaymentOrderReq;
import com.luke.cloud.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Descrtption OrderController
 * @Author luke
 * @Date 2019/9/23
 **/
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/payment")
    public String paymentOrder(@RequestBody PaymentOrderReq req){
        log.info("paymentOrder,req:{}",req);
        orderService.createOrder(req);
        return HttpStatus.OK.toString();
    }

}
