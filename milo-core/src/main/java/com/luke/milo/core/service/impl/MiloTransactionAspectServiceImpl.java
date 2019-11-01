package com.luke.milo.core.service.impl;

import com.luke.milo.common.context.MiloTransactionContext;
import com.luke.milo.core.service.MiloTransactionAspectService;
import com.luke.milo.core.service.MiloTransactionFactoryService;
import com.luke.milo.core.service.handler.MiloTransactionHandler;
import com.luke.milo.core.utils.SpringBeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Descrtption MiloTransactionAspectServiceImpl
 * @Author luke
 * @Date 2019/9/20
 **/
@Service
@Slf4j
@SuppressWarnings("all")
public class MiloTransactionAspectServiceImpl implements MiloTransactionAspectService {

    private final MiloTransactionFactoryService miloTransactionFactoryService;

    @Autowired
    public MiloTransactionAspectServiceImpl(MiloTransactionFactoryService miloTransactionFactoryService) {
        this.miloTransactionFactoryService = miloTransactionFactoryService;
    }

    @Override
    public Object  invoke(MiloTransactionContext context, ProceedingJoinPoint pjp) throws Throwable {
        //根据事务上下文获取相应的处理器
        log.info("========根据事务上下文获取相应的处理器===========context:{}",context);
        Class clazz = miloTransactionFactoryService.factoryOf(context);
        MiloTransactionHandler txHandler = (MiloTransactionHandler)SpringBeanUtils.getInstance().getBean(clazz);
        log.info("========根据事务上下文获取相应的处理器===========txHandler:{}",txHandler);
        return txHandler.handle(context,pjp);
    }

}
