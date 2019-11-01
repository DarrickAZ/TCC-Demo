package com.luke.milo.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Descrtption TCC注解
 * 作用的方法返回值必须父类是Object，基本类型需使用包装类型（TCC框架有空值null值的情况）
 * @Author luke
 * @Date 2019/9/18
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MiloTCC {

    /**
     * 指明TCC的confirm方法，具备幂等性
     * @return string
     */
    String confirmMethod() default "";

    /**
     * 指明TCC的cancel方法，具备幂等性
     * @return
     */
    String cancelMethod() default "";

}
