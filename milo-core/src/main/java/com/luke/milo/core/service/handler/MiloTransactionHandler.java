package com.luke.milo.core.service.handler;

import com.luke.milo.common.context.MiloTransactionContext;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @Descrtption MiloTransactionHandler
 * @Author luke
 * @Date 2019/9/20
 **/
@FunctionalInterface
public interface MiloTransactionHandler {

    /**
     * handle
     * @param context
     * @param pjp
     * @return
     * @throws Throwable
     */
    Object handle(MiloTransactionContext context,ProceedingJoinPoint pjp) throws Throwable;

}
