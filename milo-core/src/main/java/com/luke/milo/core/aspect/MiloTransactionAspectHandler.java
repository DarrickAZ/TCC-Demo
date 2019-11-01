package com.luke.milo.core.aspect;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @Descrtption MiloTransactionAspectHandler
 * 切面处理器
 * @Author luke
 * @Date 2019/9/20
 **/
@FunctionalInterface
public interface MiloTransactionAspectHandler {

    /**
     * 处理切点方法
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    Object handleAspectPointcutMethod(ProceedingJoinPoint proceedingJoinPoint) throws Throwable;

}
