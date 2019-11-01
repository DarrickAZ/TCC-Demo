package com.luke.milo.core.service;

import com.luke.milo.common.context.MiloTransactionContext;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @Descrtption MiloTransactionAspectService
 * @Author luke
 * @Date 2019/9/20
 **/
@FunctionalInterface
public interface MiloTransactionAspectService {

    /**
     * handle
     * @param context
     * @param pjp
     * @return
     * @throws Throwable
     */
    Object invoke(MiloTransactionContext context, ProceedingJoinPoint pjp) throws Throwable;

}
