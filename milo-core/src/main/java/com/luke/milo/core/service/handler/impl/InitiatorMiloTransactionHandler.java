package com.luke.milo.core.service.handler.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.luke.milo.common.bean.MiloInvocationBean;
import com.luke.milo.common.bean.MiloParticipantBean;
import com.luke.milo.common.bean.MiloTransactionBean;
import com.luke.milo.common.context.MiloTransactionContext;
import com.luke.milo.common.enums.MiloPhaseEnum;
import com.luke.milo.common.enums.MiloRoleEnum;
import com.luke.milo.core.service.handler.MiloTransactionHandler;
import com.luke.milo.core.service.handler.executor.MiloTransactionExecutor;
import com.luke.milo.core.threadlocal.MiloTransactionContextThreadLocal;
import com.luke.milo.core.utils.SpringBeanUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.reflect.MethodUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

/**
 * @Descrtption InitiatorMiloTransactionHandler
 * 拦截事务发起者方法处理器
 * @Author luke
 * @Date 2019/9/20
 **/
@Slf4j
@Service
@SuppressWarnings("all")
public class InitiatorMiloTransactionHandler implements MiloTransactionHandler {

    @Getter
    private final MiloTransactionExecutor miloTransactionExecutor;

    @Autowired
    public InitiatorMiloTransactionHandler(MiloTransactionExecutor miloTransactionExecutor) {
        this.miloTransactionExecutor = miloTransactionExecutor;
    }

    @Override
    public Object handle(MiloTransactionContext context,ProceedingJoinPoint pjp) throws Throwable {
        Object returnValue;
        try {
            try {
                returnValue = tryPhase(pjp);
                //执行confirm阶段
                final MiloTransactionBean transactionBean = MiloTransactionContextThreadLocal.getInstance().getBean();
                confirmPhase(transactionBean);//confirmPhase这里不采取在子线程执行，因为在高并发下会偶尔出现问题，
                // 例如测试样例中会出现部分找不到订单号的情况（用子线程try和confirm就不在一个事务里面，就是两个事务，confirm就可能读取不到订单，因为try的事务还未提交完成），
                //所以这里的try和confirm在一个事务内执行，而且避免过多的上下文切换，cancelPhase同理
                // 当然如果想在子线程执行，那么就对confirm方法有更高要求，例如本例就要求找不到订单号时要抛出异常，交给定时任务去处理即可。
            } catch (Throwable throwable) {
                //执行cancel阶段
                MiloTransactionBean transactionBean = MiloTransactionContextThreadLocal.getInstance().getBean();
                cancelPhase(transactionBean);
                throw throwable;
            }
        }finally {
            MiloTransactionContextThreadLocal instance = MiloTransactionContextThreadLocal.getInstance();
            instance.removeContext();
            instance.removeBean();
        }
        return returnValue;
    }


    /**
     * 保证TCC发起者的“事务日志READY --> 发起者业务（包括远程调用其他服务） --> 事务日志TRYING”同步
     * @param pjp
     * @return
     * @throws Throwable
     */
    public Object tryPhase(ProceedingJoinPoint pjp) throws Throwable{
        log.info("====================tryPhase start======================");
        Object returnValue;
        //新建事务日志，保存到数据库状态为READY
        MiloTransactionBean transactionBean = miloTransactionExecutor.createAndSaveTransaction(null,pjp,
                MiloPhaseEnum.READY.getCode(),MiloRoleEnum.INITIATOR.getCode());
        //创建事务上下文
        MiloTransactionContext transactionContext = new MiloTransactionContext();
        transactionContext.setTransId(transactionBean.getTransId());
        transactionContext.setPhase(MiloPhaseEnum.TRYING.getCode());
        transactionContext.setRole(MiloRoleEnum.INITIATOR.getCode());
        //事务日志和事务上下文都和当前线程绑定
        MiloTransactionContextThreadLocal instance = MiloTransactionContextThreadLocal.getInstance();
        instance.setBean(transactionBean);
        instance.setContext(transactionContext);
        //调用事务发起者的事务try阶段方法
        returnValue = pjp.proceed();
        //try阶段完成,修改事务日志状态为TRYING
        transactionBean.setPhase(MiloPhaseEnum.TRYING.getCode());
        miloTransactionExecutor.updateTransactionPhase(transactionBean.getTransId(),MiloPhaseEnum.TRYING.getCode());
        log.info("====================tryPhase end======================");
        return returnValue;
    }

    /**
     * 执行confirm阶段操作
     * @param transactionBean
     */
    public Object confirmPhase(MiloTransactionBean transactionBean) {
        log.info("====================confirmPhase start======================");
        Object confirmResult = null;//远程调用confirm的时候需要有结果返回，否则会走到熔断
        transactionBean.setPhase(MiloPhaseEnum.CONFIRMING.getCode());
        miloTransactionExecutor.updateTransactionPhase(transactionBean.getTransId(),MiloPhaseEnum.CONFIRMING.getCode());
        List<MiloParticipantBean> participantBeanList = transactionBean.getParticipantBeanList();
        boolean confirmPhaseResult = true;
        if(CollectionUtil.isNotEmpty(participantBeanList)) {
            List<Object> successResult = new ArrayList<>();
            List<MiloParticipantBean> failResult = new ArrayList<>();
            for (MiloParticipantBean participantBean : participantBeanList) {
                try {
                    Object result = reflectExecute(participantBean.getTransId(), participantBean,
                            MiloPhaseEnum.CONFIRMING.getCode(),participantBean.getConfirmInvocation());
                    successResult.add(result);
                } catch (Exception e) {
                    //执行失败的参与者
                    confirmPhaseResult = false;
                    failResult.add(participantBean);
                    e.printStackTrace();
                } finally {
                    MiloTransactionContextThreadLocal.getInstance().removeContext();
                }
            }
            //处理confirm阶段结果：删除事务日志或者更新事务日志
            handleConfirmOrCancelPhaseResult(confirmPhaseResult, transactionBean, failResult);
            if(successResult.size() > 0){
                confirmResult = successResult.get(0);
            }
        }
        log.info("====================confirmPhase end======================");
        return confirmResult;
    }

    /**
     * 处理confirm阶段结果
     * @param confirmPhaseResult
     * @param transactionBean
     * @param failResult
     */
    private void handleConfirmOrCancelPhaseResult(boolean confirmPhaseResult, MiloTransactionBean transactionBean, List<MiloParticipantBean> failResult) {
        if(confirmPhaseResult){
            miloTransactionExecutor.removeTransaction(transactionBean.getTransId());
        }else{
            transactionBean.setParticipantBeanList(failResult);
            miloTransactionExecutor.updateUpdateParticipantList(transactionBean);
        }
    }

    /**
     * 执行cancel阶段操作
     * @param transactionBean
     */
    public Object cancelPhase(MiloTransactionBean transactionBean) {
        log.info("====================cancelPhase start======================");
        Object cancelResult = null;//远程调用cancel的时候需要有结果返回，否则会走到熔断
        transactionBean.setPhase(MiloPhaseEnum.CANCELING.getCode());
        miloTransactionExecutor.updateTransactionPhase(transactionBean.getTransId(),MiloPhaseEnum.CANCELING.getCode());
        List<MiloParticipantBean> participantBeanList = transactionBean.getParticipantBeanList();
        boolean cancelPhaseResult = true;
        if(CollectionUtil.isNotEmpty(participantBeanList)) {
            List<Object> successResult = new ArrayList<>();
            List<MiloParticipantBean> failResult = new ArrayList<>();
            for (MiloParticipantBean participantBean : participantBeanList) {
                try {
                    Object result = reflectExecute(participantBean.getTransId(), participantBean,
                            MiloPhaseEnum.CANCELING.getCode(),participantBean.getCancelInvocation());
                    successResult.add(result);
                } catch (Exception e) {
                    //执行失败的参与者
                    cancelPhaseResult = false;
                    failResult.add(participantBean);
                    log.info("执行cancelPhase参与者异常,e:{}",e);
                } finally {
                    MiloTransactionContextThreadLocal.getInstance().removeContext();
                }
            }
            //处理cancel阶段结果：删除事务日志或者更新事务日志
            handleConfirmOrCancelPhaseResult(cancelPhaseResult, transactionBean, failResult);
            if(successResult.size() > 0){
                cancelResult = successResult.get(0);
            }
        }
        log.info("====================cancelPhase end======================");
        return cancelResult;
    }

    /**
     * 通过反射执行对应方法
     * @param transId
     * @param participantBean
     * @param invocationBean
     * @return
     * @throws Exception
     */
    private Object reflectExecute(String transId, MiloParticipantBean participantBean, int phase,MiloInvocationBean invocationBean) throws Exception {
        if(participantBean != null && invocationBean != null){
            //构建事务上下文，如果confirm阶段执行的是本地confirm方法将不传递，
            // 如果是执行远程的目标方法（实际上是try方法），那么将传递事务上下文
            MiloTransactionContext context = new MiloTransactionContext();
            context.setTransId(transId);
            context.setPhase(phase);
            context.setRole(MiloRoleEnum.CONSUMER.getCode());
            MiloTransactionContextThreadLocal.getInstance().setContext(context);
            //执行目标方法
            final Class clazz = invocationBean.getTargetClass();
            final String method = invocationBean.getMethodName();
            final Object[] args = invocationBean.getArgs();
            final Class[] parameterTypes = invocationBean.getParameterTypes();
            final Object bean = SpringBeanUtils.getInstance().getBean(clazz);
            return MethodUtils.invokeMethod(bean, method, args, parameterTypes);
        }
        return null;
    }

}
