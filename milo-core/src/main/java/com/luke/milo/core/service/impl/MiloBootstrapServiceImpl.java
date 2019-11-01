package com.luke.milo.core.service.impl;

import com.luke.milo.common.config.MiloConfig;
import com.luke.milo.core.dao.MiloLogDaoService;
import com.luke.milo.core.service.MiloBootstrapService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Descrtption MiloBootstrapServiceImpl
 * @Author luke
 * @Date 2019/9/23
 **/
@Slf4j
@Service
@SuppressWarnings("all")
public class MiloBootstrapServiceImpl implements MiloBootstrapService {

    private MiloLogDaoService miloLogDaoService;

    @Autowired
    public MiloBootstrapServiceImpl(MiloLogDaoService miloLogDaoService) {
        this.miloLogDaoService = miloLogDaoService;
    }

    @Override
    public void initMiloTccTransaction(MiloConfig miloConfig) {
        try {
            log.info("Milo TCC framework init start");
            //初始化连接池，创建数据库表
            miloLogDaoService.init(miloConfig);
            log.info("Milo TCC framework init success");
        } catch (Exception e) {
            log.info("Milo TCC framework init exception:{}",e);
        }
    }

}
