package com.luke.milo.core.rpc;

import com.google.gson.Gson;
import com.luke.milo.common.context.MiloTransactionContext;
import org.springframework.util.StringUtils;
import java.util.Objects;

/**
 * @Descrtption  The type RpcMediator. 在发起者发起远程调用时，在Http报文的Header传递
 *  * 键为MILO_TRANSACTION_CONTEXT，值为MiloTransactionContext的头信息
 * @Author luke
 * @Date 2019/9/26
 **/
public class RpcMediator {

    private static final RpcMediator RPC_MEDIATOR = new RpcMediator();

    private final String MILO_TRANSACTION_CONTEXT = "MILO_TRANSACTION_CONTEXT";

    private final Gson GSON = new Gson();

    public static RpcMediator getInstance() {
        return RPC_MEDIATOR;
    }

    public void transmit(RpcTransmit rpcTransmit, MiloTransactionContext context){
        if(Objects.nonNull(context)){
            rpcTransmit.transmit(MILO_TRANSACTION_CONTEXT,GSON.toJson(context));
        }
    }

    public MiloTransactionContext acquire(RpcAcquire rpcAcquire){
        MiloTransactionContext context = null;
        String contextStr = rpcAcquire.acquire(MILO_TRANSACTION_CONTEXT);
        if(!StringUtils.isEmpty(contextStr)){
            context = GSON.fromJson(contextStr,MiloTransactionContext.class);
        }
        return context;
    }

}
