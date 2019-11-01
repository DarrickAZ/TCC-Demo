package com.luke.milo.core.service;

import com.luke.milo.common.config.MiloConfig;

@FunctionalInterface
public interface MiloBootstrapService {

    void initMiloTccTransaction(MiloConfig miloConfig);

}
