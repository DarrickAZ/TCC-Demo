package com.luke.milo.springcloud.configuration;

import com.luke.milo.common.context.MiloTransactionContext;
import com.luke.milo.common.enums.MiloRoleEnum;
import com.luke.milo.core.rpc.RpcMediator;
import com.luke.milo.core.rpc.RpcTransmit;
import com.luke.milo.core.threadlocal.MiloTransactionContextThreadLocal;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;

/**
 * @Descrtption MiloFeignTransactionContextInterceptor
 * 拦截所有feign远程调用http请求，传递事务上下文（不为空时）
 * @Author luke
 * @Date 2019/9/26
 **/
public class MiloFeignTransactionContextInterceptor implements RequestInterceptor {

    private Logger logger;

    public MiloFeignTransactionContextInterceptor(Logger logger) {
        this.logger = logger;
    }

    /**
     * feign远程调用时，传递事务上下文
     * @param requestTemplate
     */
    @Override
    public void apply(final RequestTemplate requestTemplate) {
        MiloTransactionContext context = MiloTransactionContextThreadLocal.getInstance().getContext();
        if(context != null && context.getRole() != MiloRoleEnum.INITIATOR.getCode()){
            logger.info("执行feign线程name:{}",Thread.currentThread().getName());
            logger.info("========feign远程调用时，传递事务上下文start==========context:{}",context);
            RpcTransmit rpcTransmit = (String key, String value) -> requestTemplate.header(key,value);
            RpcMediator rpcMediator = RpcMediator.getInstance();
            rpcMediator.transmit(rpcTransmit,context);
            logger.info("========feign远程调用时，传递事务上下文end==========context:{}",context);
        }
    }

}
