package com.luke.cloud.order.inter;

import com.luke.cloud.order.request.DecreaseAccountReq;
import com.luke.milo.common.exception.MiloHystrixException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Descrtption AccountInterHystrix
 * @Author luke
 * @Date 2019/9/25
 **/
@Slf4j
@Component
public class AccountInterHystrix implements AccountInter{

    @Override
    public String findAccountByName(String username) {
        log.error("AccountInterHystrix findAccountByName,username:{}",username);
        //熔断需要抛出MiloHystrixException异常
        throw new MiloHystrixException("远程调用AccountInter超时");
    }

    @Override
    public String decreaseAccount(DecreaseAccountReq req) {
        log.error("AccountInterHystrix decreaseAccount,req:{}",req);
        //熔断需要跑出MiloHystrixException异常
        throw new MiloHystrixException("远程调用AccountInter超时");
    }

}
