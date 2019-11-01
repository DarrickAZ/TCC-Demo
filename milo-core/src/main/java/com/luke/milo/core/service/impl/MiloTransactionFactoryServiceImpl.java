package com.luke.milo.core.service.impl;

import com.luke.milo.common.context.MiloTransactionContext;
import com.luke.milo.common.enums.MiloRoleEnum;
import com.luke.milo.common.exception.MiloException;
import com.luke.milo.core.service.MiloTransactionFactoryService;
import com.luke.milo.core.service.handler.impl.ConsumerMiloTransactionHandler;
import com.luke.milo.core.service.handler.impl.InitiatorMiloTransactionHandler;
import com.luke.milo.core.service.handler.impl.ProviderMiloTransactionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Objects;

/**
 * @Descrtption MiloTransactionFactoryServiceImpl
 * @Author luke
 * @Date 2019/9/20
 **/
@Service
@SuppressWarnings("all")
@Slf4j
public class MiloTransactionFactoryServiceImpl implements MiloTransactionFactoryService {

    @Override
    public Class factoryOf(MiloTransactionContext context) throws Throwable {
        if(Objects.isNull(context)){
            //当事务上下文为空时，说明拦截的是事务发起者的方法
            log.info("===============事务发起者方法================");
            return InitiatorMiloTransactionHandler.class;
        }else{
            if (context.getRole() == MiloRoleEnum.CONSUMER.getCode()){
                log.info("===============事务消费者方法================");
                return ConsumerMiloTransactionHandler.class;
            }else if(context.getRole() == MiloRoleEnum.PROVIDER.getCode()){
                log.info("===============事务提供者方法================");
                return ProviderMiloTransactionHandler.class;
            }
        }
        throw new MiloException("找不到事务处理类");
    }

}
