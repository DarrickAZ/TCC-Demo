package com.luke.milo.core.schedule;

import com.luke.milo.common.bean.MiloTransactionBean;
import com.luke.milo.common.config.MiloConfig;
import com.luke.milo.common.enums.MiloPhaseEnum;
import com.luke.milo.common.enums.MiloRoleEnum;
import com.luke.milo.core.service.MiloTransactionScheduleService;
import com.luke.milo.core.service.handler.executor.MiloTransactionExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Descrtption Milo事务日志处理定时器
 * ApplicationReadyEvent Event published as late as conceivably possible to indicate that the application is ready to service requests.
 * The source of the event is the SpringApplication itself,
 * but beware of modifying its internal state since all initialization steps will have been completed by then.
 * @Author luke
 * @Date 2019/10/11
 **/
@Slf4j
@Component
@SuppressWarnings("all")
public class MiloTransactionLogHandleScheduled implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private MiloConfig miloConfig;

    @Autowired
    private MiloTransactionExecutor miloTransactionExecutor;

    @Autowired
    private MiloTransactionScheduleService miloTransactionScheduleService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        log.info("==============初始化miloSchedule start==================");
        ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1,(Runnable runnable) -> {
            Thread thread = new Thread(runnable,"MiloTransactionLogHandleScheduled-Thread");
            thread.setDaemon(true);
            return thread;
        });
        startSchedule(scheduledExecutorService);
        log.info("==============初始化miloSchedule end==================");
    }

    /**
     * 处理事务日志
     * 第180秒开始执行 处理第90秒前的事务日志（未超最大处理次数，超过人工介入）
     * 第270秒执行 处理第180秒前的事务日志（未超最大处理次数，超过人工介入）
     * @param scheduledExecutorService
     */
    private void startSchedule(ScheduledExecutorService scheduledExecutorService){
        scheduledExecutorService.scheduleWithFixedDelay(()->{
            log.info("==============miloSchedule start====================");
            try {
                Date delayDate = new Date(System.currentTimeMillis() - miloConfig.getScheduleDelay() * 1000);
                List<MiloTransactionBean> miloTransactionList = miloTransactionExecutor.queryAllByDelay(delayDate);
                for(MiloTransactionBean miloTransaction : miloTransactionList){
                    if (miloTransaction.getRetryTimes() > miloConfig.getRetryMax()) {
                        log.error("处理事务日志次数超过最大重试次数，需要人工介入处理，miloTransaction:{}",miloTransaction);
                        continue;
                    }
                    Integer role = miloTransaction.getRole();
                    Integer phase = miloTransaction.getPhase();
                    if(role == MiloRoleEnum.INITIATOR.getCode()){//发起者
                        if(phase == MiloPhaseEnum.READY.getCode() || phase == MiloPhaseEnum.TRYING.getCode() || phase == MiloPhaseEnum.CANCELING.getCode()){
                            //执行cancel流程
                            miloTransactionScheduleService.cancelPhase(miloTransaction);
                        }else if(phase == MiloPhaseEnum.CONFIRMING.getCode()){
                            //执行confirm流程
                            miloTransactionScheduleService.confirmPhase(miloTransaction);
                        }else{
                            log.error("未知事务日志，miloTransaction：{}",miloTransaction);
                        }
                    }else if(role == MiloRoleEnum.CONSUMER.getCode()){//消费者
                        log.error("未知事务日志，miloTransaction：{}",miloTransaction);
                    }else if(role == MiloRoleEnum.PROVIDER.getCode()){//提供者
                        if(phase == MiloPhaseEnum.READY.getCode() || phase == MiloPhaseEnum.TRYING.getCode() || phase == MiloPhaseEnum.CANCELING.getCode()){
                            //执行cancel流程
                            miloTransactionScheduleService.cancelPhase(miloTransaction);
                        }else if(phase == MiloPhaseEnum.CONFIRMING.getCode()){
                            //执行confirm流程
                            miloTransactionScheduleService.confirmPhase(miloTransaction);
                        }else{
                            log.error("未知事务日志，miloTransaction：{}",miloTransaction);
                        }
                    }else{
                        log.error("未知事务日志，miloTransaction：{}",miloTransaction);
                    }
                }
            } catch (Exception e) {
                log.error("miloSchedule error:{}",e);
            }
            log.info("==============miloSchedule end====================");
        },miloConfig.getScheduleInitDelay(),miloConfig.getScheduleDelay(), TimeUnit.SECONDS);

    }

}
