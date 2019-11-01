package com.luke.cloud.order.service.impl;

import com.luke.cloud.order.bean.Order;
import com.luke.cloud.order.dao.OrderMapper;
import com.luke.cloud.order.inter.AccountInter;
import com.luke.cloud.order.inter.StockInter;
import com.luke.cloud.order.request.DecreaseAccountReq;
import com.luke.cloud.order.request.DecreaseStockReq;
import com.luke.cloud.order.request.PaymentOrderReq;
import com.luke.cloud.order.service.PaymentService;
import com.luke.milo.common.exception.MiloException;
import com.luke.milo.core.annotation.MiloTCC;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.Date;

/**
 * @Descrtption PaymentServiceImpl
 * @Author luke
 * @Date 2019/9/28
 **/
@Slf4j
@Service
@Transactional(readOnly = true)
@SuppressWarnings("all")
public class PaymentServiceImpl implements PaymentService {

    @Resource
    private StockInter stockInter;

    @Resource
    private AccountInter accountInter;

    @Resource
    private OrderMapper orderMapper;

    @MiloTCC(confirmMethod = "paymentConfirm",cancelMethod = "paymentCancel")
    @Override
    public void payment(PaymentOrderReq req) {
        log.info("==================payment开始=======================");
        //修改订单状态为支付中
        Order order = orderMapper.selectById(req.getOrderId());
        order.setStatus(2);
        order.setUpdateTime(new Date());
        orderMapper.updateById(order);

        String remoteResult = "failure";
        //扣减库存
        DecreaseStockReq stockReq = new DecreaseStockReq();
        stockReq.setOrderId(order.getId());
        stockReq.setProductName(req.getProductName());
        stockReq.setStock(req.getCount());
        remoteResult = stockInter.decreaseStock(stockReq);
        if ("failure".equals(remoteResult)){
            throw new RuntimeException("decreaseStock error");
        }

        //扣减账户余额
        DecreaseAccountReq accountReq = new DecreaseAccountReq();
        accountReq.setOrderId(order.getId());
        accountReq.setUsername(req.getUsername());
        accountReq.setAmount(order.getAmount());
        remoteResult = accountInter.decreaseAccount(accountReq);
        if ("failure".equals(remoteResult)){
            throw new RuntimeException("decreaseAccount error");
        }
        log.info("==================payment结束=======================");
    }

    /**
     * paymentConfirm 失败时需要抛出异常
     * @param req
     */
    @Transactional(rollbackFor = Exception.class)
    public void paymentConfirm(PaymentOrderReq req){
        log.info("==================paymentConfirm开始=======================");
        Order dbOrder = orderMapper.selectById(req.getOrderId());
        if(dbOrder != null){
            dbOrder.setStatus(4);
            dbOrder.setUpdateTime(new Date());
            int influence = orderMapper.updateById(dbOrder);
            if(influence < 1){
                throw new MiloException("paymentConfirm exception");
            }
        }else{
            log.warn("confirm找不到订单，req:{}",req);
        }
        log.info("==================paymentConfirm结束=======================");
    }

    /**
     * paymentCancel 失败时需要抛出异常
     * @param req
     */
    @Transactional(rollbackFor = Exception.class)
    public void paymentCancel(PaymentOrderReq req){
        log.info("==================paymentCancel开始=======================");
        Order dbOrder = orderMapper.selectById(req.getOrderId());
        if(dbOrder != null){
            dbOrder.setStatus(3);
            dbOrder.setUpdateTime(new Date());
            int influence = orderMapper.updateById(dbOrder);
            if(influence < 1){
                throw new MiloException("paymentCancel exception");
            }
        }else{
            log.warn("cancel找不到订单，req:{}",req);
        }
        log.info("==================paymentCancel结束=======================");
    }

}
