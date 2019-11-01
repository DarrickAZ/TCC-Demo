package com.luke.milo.core.bootstrap;

import com.luke.milo.common.config.MiloConfig;
import com.luke.milo.core.service.MiloBootstrapService;
import com.luke.milo.core.utils.SpringBeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 启动类
 * @Descrtption MiloTransactionBootstrap
 * @Author luke
 * @Date 2019/9/23
 **/
@SuppressWarnings("all")
public class MiloTransactionBootstrap implements ApplicationContextAware{

    private MiloBootstrapService miloBootstrapService;

    private MiloConfig miloConfig;

    @Autowired
    public MiloTransactionBootstrap(MiloBootstrapService miloBootstrapService,MiloConfig miloConfig) {
        this.miloBootstrapService = miloBootstrapService;
        this.miloConfig = miloConfig;
    }

    /**注入上下文*/
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringBeanUtils.getInstance().setCfgContext((ConfigurableApplicationContext)applicationContext);
        //初始化Milo
        miloBootstrapService.initMiloTccTransaction(miloConfig);
    }

}
