package com.luke.cloud.order.test;

import cn.hutool.json.JSONUtil;
import com.luke.cloud.order.request.PaymentOrderReq;
import okhttp3.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @Descrtption Task
 * @Author luke
 * @Date 2019/10/8
 **/
public class Task implements Runnable{

    private final CountDownLatch countDownLatch;

    private final PaymentOrderReq req;

    private final String name;

    public Task(CountDownLatch countDownLatch,PaymentOrderReq req,String name) {
        this.countDownLatch = countDownLatch;
        this.req = req;
        this.name = name;
    }

    @Override
    public void run() {
        try{
            //System.out.println("task:"+name+" ready");
            String jsonStr = JSONUtil.toJsonStr(req);
            OkHttpClient client = new OkHttpClient.Builder()
                    .readTimeout(600, TimeUnit.SECONDS)
                    .build();
            RequestBody body = RequestBody.create(MediaType.get("application/json"),jsonStr);
            Request request = new Request.Builder()
                    .url("http://localhost:8762/order/payment")
                    .post(body)
                    .build();
            Call call = client.newCall(request);
            countDownLatch.await();
            Response response = call.execute();
            String bodyStr = response.body().string();
            if(!bodyStr.equals("200")){
                System.out.println("task:"+name+" finished,body:"+bodyStr);
            }
        }catch (Exception e){
            System.out.println("task:"+name+" fail, exception:"+e.getMessage());
        }
    }

}
