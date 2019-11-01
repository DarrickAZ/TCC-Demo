package com.luke.milo.springcloud.aspectj;

import com.luke.milo.core.aspect.AbstractMiloTransactionAspect;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * @Descrtption SpringcloudTransactionAspect
 * @Author luke
 * @Date 2019/9/20
 **/
@Component
@Aspect
public class SpringCloudMiloTransactionAspect extends AbstractMiloTransactionAspect implements Ordered {

    @Autowired
    public SpringCloudMiloTransactionAspect(SpringCloudMiloTransactionAspectHandler miloTransactionAspectHandler) {
        super.setMiloTransactionAspect(miloTransactionAspectHandler);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

}
