package com.luke.cloud.order.test;

import com.luke.cloud.order.request.PaymentOrderReq;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @Descrtption 并发测试分布式事务框架Milo
 * @Author luke
 * @Date 2019/10/8
 **/
public class TCCTest {

    private static final int CIRCLE = 10;

    /**顾客人数*/
    private static final int CUSTOMER_THREAD = 100;

    public static void main(String[] args) {
        try {
            //初始账号金额
            BigDecimal lukeInitBalance = new BigDecimal(85212.94);
            BigDecimal jackInitBalance = new BigDecimal(124154.53);
            List<Map<String,Object>> list = new ArrayList<>();
            for(int j = 0; j < CIRCLE; j++){
                CountDownLatch countDownLatch = new CountDownLatch(1);
                for(int i = 0; i < CUSTOMER_THREAD; i++){
                    PaymentOrderReq req = new PaymentOrderReq();
                    int account = (int)(Math.random()*2)+1;//账号
                    int product = (int)(Math.random()*3)+1;//商品
                    int count = (int)(Math.random()*10)+1;//数量
                    if(account == 1){
                        Map<String,Object> lukeMap = new HashMap<>();
                        lukeMap.put("name","luke");
                        if(product == 1){
                            lukeMap.put("productName","apple");
                        }else if(product == 2){
                            lukeMap.put("productName","book");
                        }else{
                            lukeMap.put("productName","phone");
                        }
                        lukeMap.put("count",count);
                        req.setUsername("luke");
                        req.setCount(count);
                        req.setProductName(lukeMap.get("productName").toString());
                        list.add(lukeMap);
                    }else{
                        Map<String,Object> jackMap = new HashMap<>();
                        jackMap.put("name","jack");
                        if(product == 1){
                            jackMap.put("productName","apple");
                        }else if(product == 2){
                            jackMap.put("productName","book");
                        }else{
                            jackMap.put("productName","phone");
                        }
                        jackMap.put("count",count);
                        req.setUsername("jack");
                        req.setCount(count);
                        req.setProductName(jackMap.get("productName").toString());
                        list.add(jackMap);
                    }
                    new Thread(new Task(countDownLatch,req,i+"")).start();
                }
                countDownLatch.countDown();//并发调用
                TimeUnit.SECONDS.sleep(5);
            }

            //统计结果
            System.out.println("*******************************");
            /*for(Map<String,Object> map : list){
                System.out.println("map:"+map);
            }*/
            System.out.println("*******************************");
            int lukeMapSize = 0;
            int jackMapSize = 0;
            for(Map<String,Object> map : list){
                if(map.get("name").toString().equals("luke")){
                    lukeMapSize += 1;
                }else{
                    jackMapSize += 1;
                }
            }
            System.out.println("luke订单数量："+lukeMapSize);
            System.out.println("jack订单数量："+jackMapSize);
            System.out.println("===============================");
            int appleTotalStock = 0;
            int bookTotalStock = 0;
            int phoneTotalStock = 0;
            BigDecimal lukeAmout = new BigDecimal(0.0);
            BigDecimal jackAmout = new BigDecimal(0.0);
            for(Map<String,Object> map : list){
                if(map.get("productName").toString().equals("apple")){//商品apple
                    int tempCount = (int)map.get("count");
                    appleTotalStock += (int)map.get("count");//累加购买数量
                    BigDecimal tempAmout = new BigDecimal(tempCount*4.35);
                    if(map.get("name").toString().equals("luke")){
                        lukeAmout = lukeAmout.add(tempAmout);
                        //System.out.println("luke-apple-count-tempAmout:"+tempCount+","+tempAmout.doubleValue());
                    }else{
                        jackAmout = jackAmout.add(tempAmout);
                        //System.out.println("jack-apple-count-tempAmout:"+tempCount+","+tempAmout.doubleValue());
                    }
                }else if (map.get("productName").toString().equals("book")){//商品book
                    int tempCount = (int)map.get("count");
                    bookTotalStock += (int)map.get("count");//累加购买数量
                    BigDecimal tempAmout = new BigDecimal(tempCount*7.78);
                    if(map.get("name").toString().equals("luke")){
                        lukeAmout = lukeAmout.add(tempAmout);
                        //System.out.println("luke-book--count-tempAmout:"+tempCount+","+tempAmout.doubleValue());
                    }else{
                        jackAmout = jackAmout.add(tempAmout);
                        //System.out.println("jack-book--count-tempAmout:"+tempCount+","+tempAmout.doubleValue());
                    }
                }else{//商品phone
                    int tempCount = (int)map.get("count");
                    phoneTotalStock += (int)map.get("count");//累加购买数量
                    BigDecimal tempAmout = new BigDecimal(tempCount*2.13);
                    if(map.get("name").toString().equals("luke")){
                        lukeAmout = lukeAmout.add(tempAmout);
                        //System.out.println("luke-phone--count-tempAmout:"+tempCount+","+tempAmout.doubleValue());
                    }else{
                        jackAmout = jackAmout.add(tempAmout);
                        //System.out.println("jack-phone--count-tempAmout:"+tempCount+","+tempAmout.doubleValue());
                    }
                }
            }
            System.out.println("apple减去库存："+appleTotalStock);
            System.out.println("book减去库存："+bookTotalStock);
            System.out.println("phone减去库存："+phoneTotalStock);
            System.out.println("===============================");
            System.out.println("luke订单总金额："+lukeAmout.doubleValue());
            System.out.println("jack订单总金额："+jackAmout.doubleValue());
            System.out.println("===============================");
            BigDecimal lukeBalance = lukeInitBalance.subtract(lukeAmout);
            BigDecimal jackBalance = jackInitBalance.subtract(jackAmout);
            System.out.println("luke账号剩余金额："+ lukeBalance.doubleValue());
            System.out.println("jack账号剩余金额："+ jackBalance.doubleValue());
            System.out.println("===============================");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

}
