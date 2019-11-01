package com.luke.milo.core.service.handler.impl;

import com.luke.milo.common.bean.MiloTransactionBean;
import com.luke.milo.common.context.MiloTransactionContext;
import com.luke.milo.common.enums.MiloPhaseEnum;
import com.luke.milo.common.enums.MiloRoleEnum;
import com.luke.milo.common.exception.MiloException;
import com.luke.milo.core.service.handler.MiloTransactionHandler;
import com.luke.milo.core.service.handler.executor.MiloTransactionExecutor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Descrtption ProviderMiloTransactionHandler
 * @Author luke
 * @Date 2019/9/26
 **/
@Slf4j
@Service
public class ProviderMiloTransactionHandler implements MiloTransactionHandler {

    private InitiatorMiloTransactionHandler initiatorMiloTransactionHandler;

    private final MiloTransactionExecutor miloTransactionExecutor;

    @Autowired
    public ProviderMiloTransactionHandler(InitiatorMiloTransactionHandler initiatorMiloTransactionHandler) {
        this.initiatorMiloTransactionHandler = initiatorMiloTransactionHandler;
        this.miloTransactionExecutor = initiatorMiloTransactionHandler.getMiloTransactionExecutor();
    }

    @Override
    public Object handle(MiloTransactionContext context, ProceedingJoinPoint pjp) throws Throwable {
        if (context == null){
            return null;
        }
        switch (MiloPhaseEnum.acquireByCode(context.getPhase())){
            case TRYING:
                MiloTransactionBean dbTransaction = null;
                try {
                    //try阶段处理非常重要，要保证事务性，事务日志的的操作必须向外报异常（回滚）
                    //从而避免try阶段执行超时，事务发起者已经将cancel指令发送过来执行，那么就会出现没有日志
                    //但是账号扣减金额被冻结的尴尬状态（没有事务记录，就不知道这中间结果是要执行成功，还是回滚，因为其他参与者都执行完了整个流程）
                    log.info("============提供者TRYING开始==================context:{}",context);
                    //创建并保存事务日志
                    dbTransaction = miloTransactionExecutor.createAndSaveTransaction(context.getTransId(), pjp,
                            MiloPhaseEnum.READY.getCode(), MiloRoleEnum.PROVIDER.getCode());
                    Object proceed = pjp.proceed();
                    //miloTransactionExecutor.updateTransactionPhase(dbTransaction.getTransId(),MiloPhaseEnum.TRYING.getCode());
                    log.info("============提供者TRYING结束==================context:{}",context);
                    return proceed;
                } catch (Throwable throwable) {
                    //不删除事务日志
                    //miloTransactionExecutor.removeTransaction(context.getTransId());
                    throw throwable;
                }finally {
                    if(dbTransaction != null){
                        miloTransactionExecutor.updateTransactionPhase(dbTransaction.getTransId(),MiloPhaseEnum.TRYING.getCode());
                    }
                }
            case CONFIRMING:
                Object result = null;
                try {
                    log.info("============提供者CONFIRMING开始==================context:{}",context);
                    //不执行目标try方法
                    MiloTransactionBean miloTransactionBean = miloTransactionExecutor.queryTransaction(context.getTransId());
                    if(miloTransactionBean != null){
                        if(miloTransactionBean.getPhase() == MiloPhaseEnum.READY.getCode()){
                            throw new MiloException("try阶段还没有执行完毕就收到CONFIRMING指令");
                        }
                        result = initiatorMiloTransactionHandler.confirmPhase(miloTransactionBean);
                    }
                    log.info("============提供者CONFIRMING结束==================context:{}",context);
                } catch (Throwable throwable) {
                    throw throwable;
                }
                return result;
            case CANCELING:
                Object result2 = null;
                try {
                     log.info("============提供者CANCELING开始==================context:{}",context);
                    //不执行目标try方法
                    MiloTransactionBean miloTransactionBean2 = miloTransactionExecutor.queryTransaction(context.getTransId());
                    if(miloTransactionBean2 != null){
                        if(miloTransactionBean2.getPhase() == MiloPhaseEnum.READY.getCode()){
                            throw new MiloException("try阶段还没有执行完毕就收到CANCELING指令");
                        }
                        result2 = initiatorMiloTransactionHandler.cancelPhase(miloTransactionBean2);
                    }
                    log.info("============提供者CANCELING结束==================context:{}",context);
                } catch (Throwable throwable) {
                    throw throwable;
                }
                return result2;
        }
        return null;
    }

}
