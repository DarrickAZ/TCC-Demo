package com.luke.milo.core.service;

import com.luke.milo.common.context.MiloTransactionContext;

/**
 * @Descrtption MiloTransactionFactoryService
 * @Author luke
 * @Date 2019/9/20
 **/
@FunctionalInterface
public interface MiloTransactionFactoryService {

    /**
     * 获取对应的角色处理器
     * @param transactionContext
     * @return
     * @throws Throwable
     */
    Class factoryOf(MiloTransactionContext transactionContext) throws Throwable;

}
