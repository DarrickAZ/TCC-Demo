package com.luke.milo.spring.cloud.starter.configuration;

import com.luke.milo.core.bootstrap.MiloTransactionBootstrap;
import com.luke.milo.core.service.MiloBootstrapService;
import com.luke.milo.spring.cloud.starter.config.MiloConfigProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Descrtption MiloAutoConfiguration
 * @Author luke
 * @Date 2019/9/23
 **/
@Slf4j
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan(basePackages = {"com.luke.milo"})
@EnableTransactionManagement
public class MiloAutoConfiguration {

    /**配置项*/
    private final MiloConfigProperties miloConfigProperties;

    @Autowired
    public MiloAutoConfiguration(MiloConfigProperties miloConfigProperties) {
        this.miloConfigProperties = miloConfigProperties;
    }

    @Bean
    @Qualifier("miloTransactionBootstrap")
    public MiloTransactionBootstrap miloTransactionBootstrap(MiloBootstrapService miloBootstrapService){
        log.info("miloConfigProperties:{}",miloConfigProperties);
        MiloTransactionBootstrap miloTransactionBootstrap = new MiloTransactionBootstrap(miloBootstrapService,miloConfigProperties);
        return miloTransactionBootstrap;
    }

    /**
     * 数据源，这里不能把数据源添加到spring容器，会覆盖上层数据源
     * @return
     */
    /*@Bean
    @Qualifier("dataSource")
    public DataSource dataSource(){
        HikariDataSource hikariDataSource = new HikariDataSource();
        MiloDbConfig miloDbConfig = miloConfigProperties.getMiloDbConfig();
        hikariDataSource.setJdbcUrl(miloDbConfig.getUrl());
        hikariDataSource.setDriverClassName(miloDbConfig.getDriverClassName());
        hikariDataSource.setUsername(miloDbConfig.getUsername());
        hikariDataSource.setPassword(miloDbConfig.getPassword());
        hikariDataSource.setMinimumIdle(miloDbConfig.getMinimumIdle());
        hikariDataSource.setMaximumPoolSize(miloDbConfig.getMaximumPoolSize());
        hikariDataSource.setIdleTimeout(miloDbConfig.getIdleTimeout());
        hikariDataSource.setConnectionTimeout(miloDbConfig.getConnectionTimeout());
        hikariDataSource.setConnectionTestQuery(miloDbConfig.getConnectionTestQuery());
        hikariDataSource.setMaxLifetime(miloDbConfig.getMaxLifetime());
        return hikariDataSource;
    }*/

}
