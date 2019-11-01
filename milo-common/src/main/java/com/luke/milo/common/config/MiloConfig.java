package com.luke.milo.common.config;


import lombok.Getter;
import lombok.Setter;

/**
 * @Descrtption MiloConfig
 * @Author luke
 * @Date 2019/9/23
 **/
@Getter
public class MiloConfig {

    private String serializer = "kryo";

    @Setter
    private int retryMax = 3;

    @Setter
    private String modelName;

    @Setter
    private MiloDbConfig miloDbConfig;

    private Long scheduleInitDelay = 180L;

    private Long scheduleDelay = 90L;

}
