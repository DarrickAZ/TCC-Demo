package com.luke.milo.spring.cloud.starter.config;

import com.luke.milo.common.config.MiloConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 接收依赖milo starter项目的boot配置项
 * @Descrtption MiloConfigProperties
 * @Author luke
 * @Date 2019/9/23
 **/
@Component("miloConfig")
@ConfigurationProperties(prefix = "milo-tcc",
        ignoreInvalidFields = true)
public class MiloConfigProperties extends MiloConfig {

}
