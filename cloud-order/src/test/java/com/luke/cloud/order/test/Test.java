package com.luke.cloud.order.test;

import java.math.BigDecimal;

/**
 * @Descrtption test
 * @Author luke
 * @Date 2019/10/8
 **/
public class Test {

    public static void main(String[] args) {
        BigDecimal a = new BigDecimal(4.2);
        BigDecimal b = new BigDecimal(4.4);
        a = a.add(b);
        System.out.println(a);
    }

}
