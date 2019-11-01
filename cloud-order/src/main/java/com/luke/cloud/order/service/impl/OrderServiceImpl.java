package com.luke.cloud.order.service.impl;

import com.luke.cloud.order.bean.Order;
import com.luke.cloud.order.dao.OrderMapper;
import com.luke.cloud.order.inter.AccountInter;
import com.luke.cloud.order.inter.StockInter;
import com.luke.cloud.order.request.PaymentOrderReq;
import com.luke.cloud.order.service.OrderService;
import com.luke.cloud.order.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * @Descrtption OrderServiceImpl
 * @Author luke
 * @Date 2019/9/23
 **/
@Slf4j
@Service
@Transactional(readOnly = true)
@SuppressWarnings("all")
public class OrderServiceImpl implements OrderService {

    @Autowired(required = false)
    private OrderMapper orderMapper;

    @Autowired
    private PaymentService paymentService;

    /**
     * feign 远程调用账户服务和库存服务
     */
    @Resource
    private AccountInter accountInter;
    @Resource
    private StockInter stockInter;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createOrder(PaymentOrderReq req) {
        /**
         * 下单请求过来后去调用OderService创建订单，创建订单时先通过AccountInter和StockInter的
         * feign接口类去调用账户服务和库存服务查询是否有足够的金额和库存，接着创建未支付的订单。
         */
        //查找用户和库存商品
        String accountId = accountInter.findAccountByName(req.getUsername());
        if (accountId == null){
            throw new RuntimeException("not find account");
        }
        Map<String, Object> stockMap = stockInter.findStockByName(req.getProductName());
        if(stockMap == null){
            throw new RuntimeException("not find stock");
        }

        //插入订单
        Order saveOrder = createOrder(req,accountId,(String)stockMap.get("id"),(Double) stockMap.get("price"));
        int result = orderMapper.insert(saveOrder);
        if (result < 0){
            throw new RuntimeException("insertOrder error");
        }

        //必须通过service调用方法@MiloTCC才起作用（同一个servie嵌套调用会失效）
        req.setOrderId(saveOrder.getId());
        paymentService.payment(req);
    }


    private Order createOrder(PaymentOrderReq req,String accountId,String productId,Double price){
        Order saveOrder = new Order();
        saveOrder.setUserId(accountId);
        saveOrder.setProductId(productId);
        saveOrder.setCount(req.getCount());
        BigDecimal bigDecimal = new BigDecimal(price*req.getCount());
        saveOrder.setAmount(bigDecimal);
        saveOrder.setStatus(1);//订单的状态 1:未支付 2:支付中 3:支付失败 4:支付成功
        saveOrder.setCreateTime(new Date());
        return saveOrder;
    }

}
