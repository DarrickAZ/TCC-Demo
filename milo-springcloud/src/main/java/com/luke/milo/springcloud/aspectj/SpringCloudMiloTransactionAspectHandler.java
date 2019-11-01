package com.luke.milo.springcloud.aspectj;

import com.luke.milo.common.context.MiloTransactionContext;
import com.luke.milo.common.enums.MiloRoleEnum;
import com.luke.milo.core.aspect.MiloTransactionAspectHandler;
import com.luke.milo.core.rpc.RpcMediator;
import com.luke.milo.core.service.MiloTransactionAspectService;
import com.luke.milo.core.threadlocal.MiloTransactionContextThreadLocal;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @Descrtption SpringCloudMiloTransactionAspectHandler
 * @Author luke
 * @Date 2019/9/20
 **/
@Component
@Slf4j
public class SpringCloudMiloTransactionAspectHandler implements MiloTransactionAspectHandler {

    private MiloTransactionAspectService miloTransactionAspectService;

    @Autowired
    public SpringCloudMiloTransactionAspectHandler(MiloTransactionAspectService miloTransactionAspectService) {
        this.miloTransactionAspectService = miloTransactionAspectService;
    }

    @Override
    public Object handleAspectPointcutMethod(ProceedingJoinPoint pjp) throws Throwable {
        log.info("执行TCC事务线程name:{}",Thread.currentThread().getName());
        MiloTransactionContext transactionContext = MiloTransactionContextThreadLocal.getInstance().getContext();
        log.info("==============handleAspectPointcutMethod===============pjp:{},context:{}",pjp,transactionContext);
        if(Objects.nonNull(transactionContext)){
            //调用远程方法时
            if (MiloRoleEnum.INITIATOR.getCode() == transactionContext.getRole()){
                transactionContext.setRole(MiloRoleEnum.CONSUMER.getCode());
            }
        }else{
            try {
                final RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
                transactionContext = RpcMediator.getInstance().acquire(key -> {
                    HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
                    return request.getHeader(key);
                });
                if(transactionContext != null){
                    log.info("从Header获取到上下文，context:{}",transactionContext);
                    transactionContext.setRole(MiloRoleEnum.PROVIDER.getCode());
                }
            } catch (IllegalStateException ex) {
                log.error("can not acquire context info:{}",ex);
            }
        }
        return miloTransactionAspectService.invoke(transactionContext,pjp);
    }

}
