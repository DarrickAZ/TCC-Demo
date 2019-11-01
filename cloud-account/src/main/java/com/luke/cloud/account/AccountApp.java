package com.luke.cloud.account;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Descrtption AccountApp
 * @Author luke
 * @Date 2019/9/23
 **/
@SpringBootApplication
@EnableEurekaClient
@EnableHystrix
@EnableTransactionManagement
@MapperScan(basePackages = {"com.luke.cloud.account.dao"})
public class AccountApp {

    public static void main(String[] args) {
        SpringApplication.run(AccountApp.class,args);
    }

}
