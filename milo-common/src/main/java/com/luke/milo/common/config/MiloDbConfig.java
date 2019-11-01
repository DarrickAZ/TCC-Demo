package com.luke.milo.common.config;

import lombok.Data;

/**
 * @Descrtption MiloDbConfig
 * @Author luke
 * @Date 2019/9/23
 **/
@Data
public class MiloDbConfig {

    private String driverClassName = "com.mysql.jdbc.Driver";

    private String type = "com.zaxxer.hikari.HikariDataSource";

    private String url;

    private String username;

    private String password;

    private int minimumIdle;

    private int maximumPoolSize;

    private long idleTimeout;

    private long connectionTimeout;

    private String connectionTestQuery;

    private long maxLifetime;

}
