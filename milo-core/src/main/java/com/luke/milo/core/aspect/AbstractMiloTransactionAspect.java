package com.luke.milo.core.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import com.luke.milo.core.annotation.MiloTCC;

/**
 * @Descrtption AbstractMiloTransactionAspect
 * aop 拦截@MiloTCC注解方法
 * @Author luke
 * @Date 2019/9/20
 **/
@Aspect
public abstract class AbstractMiloTransactionAspect {

    private MiloTransactionAspectHandler miloTransactionAspectHandler;

    /**
     * 设置拦截后的处理器
     * @param miloTransactionAspectHandler
     */
    protected void setMiloTransactionAspect(final MiloTransactionAspectHandler miloTransactionAspectHandler) {
        this.miloTransactionAspectHandler = miloTransactionAspectHandler;
    }

    /**
     * 拦截MiloTCC注解 {@linkplain MiloTCC}
     */
    @Pointcut("@annotation(com.luke.milo.core.annotation.MiloTCC)")
    public void interceptPointcut(){
    }

    /**
     * 拦截器处理方法
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around("interceptPointcut()")
    public Object interceptPointcutHandleMethod(final ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("环绕通知.......");
        return miloTransactionAspectHandler.handleAspectPointcutMethod(pjp);
    }

}
