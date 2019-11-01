package com.luke.cloud.stock;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Descrtption Stock App
 * @Author luke
 * @Date 2019/9/25
 **/
@SpringBootApplication
@EnableEurekaClient
@EnableHystrix
@EnableTransactionManagement
@MapperScan(basePackages = {"com.luke.cloud.stock.dao"})
public class StockApp {

    public static void main(String[] args) {
        SpringApplication.run(StockApp.class, args);
    }

}
