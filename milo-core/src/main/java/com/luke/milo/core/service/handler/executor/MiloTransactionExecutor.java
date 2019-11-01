package com.luke.milo.core.service.handler.executor;

import com.luke.milo.common.bean.MiloInvocationBean;
import com.luke.milo.common.bean.MiloParticipantBean;
import com.luke.milo.common.bean.MiloTransactionBean;
import com.luke.milo.common.exception.MiloException;
import com.luke.milo.core.annotation.MiloTCC;
import com.luke.milo.core.dao.MiloLogDaoService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Descrtption MiloTransactionExecutor
 * @Author luke
 * @Date 2019/9/20
 **/
@Slf4j
@Service
public class MiloTransactionExecutor {

    private MiloLogDaoService miloLogDaoService;

    @Autowired
    public MiloTransactionExecutor(MiloLogDaoService miloLogDaoService) {
        this.miloLogDaoService = miloLogDaoService;
    }

    /**
     * 创建MiloTransactionBean
     * @param pjp
     * @return
     */
    public MiloTransactionBean createAndSaveTransaction(String transId,ProceedingJoinPoint pjp,int phase,int role) {
        if(StringUtils.isEmpty(transId)){
            transId = UUID.randomUUID().toString().replaceAll("-", "");
        }
        MiloTransactionBean bean = new MiloTransactionBean(transId);
        bean.setPhase(phase);
        bean.setRole(role);
        bean.setRetryTimes(0);
        bean.setVersion(0);

        Class<?> clazz = pjp.getTarget().getClass();
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        Object[] args = pjp.getArgs();
        MiloTCC miloTCC = method.getAnnotation(MiloTCC.class);
        bean.setTargetClass(clazz.getName());
        bean.setTargetMethod(method.getName());
        String confirmMethodName = miloTCC.confirmMethod();
        String cancelMethodName = miloTCC.cancelMethod();
        MiloInvocationBean confirmInvocationBean = null;
        if (!StringUtils.isEmpty(confirmMethodName)) {
            bean.setConfirmMethod(confirmMethodName);
            confirmInvocationBean = new MiloInvocationBean(clazz, confirmMethodName, method.getParameterTypes(), args);
        }
        MiloInvocationBean cancelInvocationBean = null;
        if (!StringUtils.isEmpty(cancelMethodName)) {
            bean.setCancelMethod(cancelMethodName);
            cancelInvocationBean = new MiloInvocationBean(clazz, cancelMethodName, method.getParameterTypes(), args);
        }
        //添加参与者（发起者也是参与者）
        MiloParticipantBean participantBean = new MiloParticipantBean(bean.getTransId(),confirmInvocationBean,cancelInvocationBean);
        bean.addParticipant(participantBean);
        bean.setCreateTime(new Date());
        bean.setUpdateTime(new Date());
        //存储事务日志
        int saveResult = miloLogDaoService.save(bean);
        if(saveResult == 0){
            throw new MiloException("保存事务日志失败");
        }
        return bean;
    }

    /**
     * 修改事务日志阶段状态
     * @param transId
     * @param phase
     */
    public void updateTransactionPhase(String transId,Integer phase) {
        int updateResult = miloLogDaoService.updatePhase(transId, phase);
        if(updateResult == 0){
            throw new MiloException("更新事务日志phase失败");
        }
    }

    /**
     * 更新事务日志参与者信息
     * @param miloLog
     */
    public void updateUpdateParticipantList(MiloTransactionBean miloLog){
        int updateResult = miloLogDaoService.updateParticipantList(miloLog);
        if(updateResult == 0){
            throw new MiloException("更新事务日志ParticipantList失败");
        }
    }

    /**
     * 删除事务日志，事务日志不删除READY阶段的日志
     * @param transId
     */
    public void removeTransaction(String transId){
        int removeResult = miloLogDaoService.remove(transId);
        if(removeResult == 0){
            throw new MiloException("删除事务日志失败");
        }
    }

    /**
     * 查询事务日志
     * @param transId
     * @return
     */
    public MiloTransactionBean queryTransaction(String transId){
        return miloLogDaoService.query(transId);
    }

    /**
     * 更新设备
     * @param transId
     * @param version
     * @param retryTimes
     * @return
     */
    public int updateTransactionRetryTimes(String transId,Integer version,Integer retryTimes){
        return miloLogDaoService.updateTransactionRetryTimes(transId,version,retryTimes);
    }

    /**
     * 查询update_time小于delayTime的所有事务日志
     * @param delayTime
     * @return
     */
    public List<MiloTransactionBean> queryAllByDelay(Date delayTime){
        return miloLogDaoService.queryAllByDelay(delayTime);
    }

}
