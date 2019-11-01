package com.luke.milo.core.threadlocal;

import com.luke.milo.common.bean.MiloTransactionBean;
import com.luke.milo.common.context.MiloTransactionContext;

/**
 * @Descrtption 存储事务上线文在线程本地（和线程绑定）
 * @Author luke
 * @Date 2019/9/18
 **/
public class MiloTransactionContextThreadLocal {

    /**
     * thread local
     */
    private static final ThreadLocal<MiloTransactionContext> CONTEXT_THREAD_LOCAL = new ThreadLocal<>();

    private static final ThreadLocal<MiloTransactionBean> BEAN_THREAD_LOCAL = new ThreadLocal<>();

    private static final MiloTransactionContextThreadLocal TRANSACTION_CONTEXT_THREAD_LOCAL = new MiloTransactionContextThreadLocal();

    private MiloTransactionContextThreadLocal() {

    }

    public static MiloTransactionContextThreadLocal getInstance() {
        return TRANSACTION_CONTEXT_THREAD_LOCAL;
    }

    public void setContext(final MiloTransactionContext context){
        CONTEXT_THREAD_LOCAL.set(context);
    }

    public void setBean(final MiloTransactionBean bean){
        BEAN_THREAD_LOCAL.set(bean);
    }

    public MiloTransactionContext getContext(){
        return CONTEXT_THREAD_LOCAL.get();
    }

    public MiloTransactionBean getBean(){
        return BEAN_THREAD_LOCAL.get();
    }

    public void removeContext(){
        CONTEXT_THREAD_LOCAL.remove();
    }

    public void removeBean(){
        BEAN_THREAD_LOCAL.remove();
    }

}
