package com.luke.milo.core.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.luke.milo.common.bean.MiloInvocationBean;
import com.luke.milo.common.bean.MiloParticipantBean;
import com.luke.milo.common.bean.MiloTransactionBean;
import com.luke.milo.common.context.MiloTransactionContext;
import com.luke.milo.common.enums.MiloPhaseEnum;
import com.luke.milo.common.enums.MiloRoleEnum;
import com.luke.milo.core.service.MiloTransactionScheduleService;
import com.luke.milo.core.service.handler.executor.MiloTransactionExecutor;
import com.luke.milo.core.threadlocal.MiloTransactionContextThreadLocal;
import com.luke.milo.core.utils.SpringBeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.reflect.MethodUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

/**
 * @Descrtption 配合MiloTransactionLogHandleScheduled，处理事务日志
 * @Author luke
 * @Date 2019/10/11
 **/
@Slf4j
@Service
@SuppressWarnings("all")
public class MiloTransactionScheduleServiceImpl implements MiloTransactionScheduleService {

    private final MiloTransactionExecutor miloTransactionExecutor;

    @Autowired
    public MiloTransactionScheduleServiceImpl(MiloTransactionExecutor miloTransactionExecutor) {
        this.miloTransactionExecutor = miloTransactionExecutor;
    }

    @Override
    public void confirmPhase(MiloTransactionBean transactionBean) {
        transactionBean.setRetryTimes(transactionBean.getRetryTimes()+1);
        int rows = miloTransactionExecutor.updateTransactionRetryTimes(transactionBean.getTransId(), transactionBean.getVersion(), transactionBean.getRetryTimes());
        if(rows == 0){//处理多服务时并发定时任务的问题
            return;
        }
        log.info("====================miloSchedule confirmPhase start======================");
        List<MiloParticipantBean> participantBeanList = transactionBean.getParticipantBeanList();
        boolean confirmPhaseResult = true;
        if(CollectionUtil.isNotEmpty(participantBeanList)) {
            List<MiloParticipantBean> failResult = new ArrayList<>();
            for (MiloParticipantBean participantBean : participantBeanList) {
                try {
                    reflectExecute(participantBean.getTransId(), participantBean,
                            MiloPhaseEnum.CONFIRMING.getCode(),participantBean.getConfirmInvocation());
                } catch (Exception e) {
                    //执行失败的参与者
                    confirmPhaseResult = false;
                    failResult.add(participantBean);
                    log.info("执行confirmPhase参与者异常,e:{}",e);
                } finally {
                    MiloTransactionContextThreadLocal.getInstance().removeContext();
                }
            }
            //处理confirm阶段结果：删除事务日志或者更新事务日志
            handleConfirmOrCancelPhaseResult(confirmPhaseResult, transactionBean, failResult);
        }
        log.info("====================miloSchedule confirmPhase end======================");
    }

    @Override
    public void cancelPhase(MiloTransactionBean transactionBean) {
        transactionBean.setRetryTimes(transactionBean.getRetryTimes()+1);
        int rows = miloTransactionExecutor.updateTransactionRetryTimes(transactionBean.getTransId(), transactionBean.getVersion(), transactionBean.getRetryTimes());
        if(rows == 0){//处理多服务时并发定时任务的问题
            return;
        }
        log.info("====================cancelPhase start======================");
        List<MiloParticipantBean> participantBeanList = transactionBean.getParticipantBeanList();
        boolean cancelPhaseResult = true;
        if(CollectionUtil.isNotEmpty(participantBeanList)) {
            List<MiloParticipantBean> failResult = new ArrayList<>();
            for (MiloParticipantBean participantBean : participantBeanList) {
                try {
                    reflectExecute(participantBean.getTransId(), participantBean,
                            MiloPhaseEnum.CANCELING.getCode(),participantBean.getCancelInvocation());
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
        }
        log.info("====================cancelPhase end======================");
    }

    private void reflectExecute(String transId, MiloParticipantBean participantBean, int phase, MiloInvocationBean invocationBean) throws Exception {
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
            MethodUtils.invokeMethod(bean, method, args, parameterTypes);
        }
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
            miloTransactionExecutor.updateUpdateParticipantList(transactionBean);//更新重试次数
        }
    }


}
