package com.luke.cloud.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Descrtption Order App
 * @Author luke
 * @Date 2019/9/23
 **/
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = {"com.luke.cloud.order.inter"})
@EnableHystrix
@EnableTransactionManagement
@MapperScan(basePackages = {"com.luke.cloud.order.dao"})
public class OrderApp {
    public static void main( String[] args ){
        SpringApplication.run(OrderApp.class,args);
    }
}
