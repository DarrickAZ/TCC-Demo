package com.luke.milo.core.service.handler.impl;

import com.luke.milo.common.bean.MiloInvocationBean;
import com.luke.milo.common.bean.MiloParticipantBean;
import com.luke.milo.common.bean.MiloTransactionBean;
import com.luke.milo.common.context.MiloTransactionContext;
import com.luke.milo.common.enums.MiloPhaseEnum;
import com.luke.milo.core.service.handler.MiloTransactionHandler;
import com.luke.milo.core.service.handler.executor.MiloTransactionExecutor;
import com.luke.milo.core.threadlocal.MiloTransactionContextThreadLocal;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Service;
import java.lang.reflect.Method;

/**
 * @Descrtption ConsumerMiloTransactionHandler
 * @Author luke
 * @Date 2019/9/25
 **/
@Slf4j
@Service
public class ConsumerMiloTransactionHandler implements MiloTransactionHandler{

    private final MiloTransactionExecutor miloTransactionExecutor;

    public ConsumerMiloTransactionHandler(MiloTransactionExecutor miloTransactionExecutor) {
        this.miloTransactionExecutor = miloTransactionExecutor;
    }

    /**传递事务上下文*/
    @Override
    public Object handle(MiloTransactionContext context, ProceedingJoinPoint pjp) throws Throwable {
        if(context.getPhase() == MiloPhaseEnum.CONFIRMING.getCode()){
            log.info("此时远程调用参与者的feign接口去间接执行confirm方法");
            return pjp.proceed();
        }else if(context.getPhase() == MiloPhaseEnum.CANCELING.getCode()){
            log.info("此时远程调用参与者的feign接口去间接执行cancel方法");
            return pjp.proceed();
        }else{
            //添加参与者（此时feign调用，接口有@MiloTCC）
            MiloTransactionBean transactionBean = MiloTransactionContextThreadLocal.getInstance().getBean();
            MiloParticipantBean participantBean = createParticipant(context,pjp);
            transactionBean.addParticipant(participantBean);
            //更新发起者事务日志的参与者信息
            miloTransactionExecutor.updateUpdateParticipantList(transactionBean);
            return pjp.proceed();
        }
    }

    private MiloParticipantBean createParticipant(MiloTransactionContext context,ProceedingJoinPoint pjp){
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        Object[] args = pjp.getArgs();
        final Class<?> declaringClass = method.getDeclaringClass();
        MiloInvocationBean confirmInvocationBean = new MiloInvocationBean(declaringClass,method.getName(),method.getParameterTypes(),args);
        MiloInvocationBean cancelInvocationBean = new MiloInvocationBean(declaringClass,method.getName(),method.getParameterTypes(),args);
        MiloParticipantBean participantBean = new MiloParticipantBean(context.getTransId(),confirmInvocationBean,cancelInvocationBean);
        return participantBean;
    }

}
