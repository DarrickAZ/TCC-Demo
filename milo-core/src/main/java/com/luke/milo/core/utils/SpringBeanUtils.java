package com.luke.milo.core.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @Descrtption SpringBeanUtils
 * @Author luke
 * @Date 2019/9/20
 **/
public class SpringBeanUtils {

    private static final SpringBeanUtils INSTANCE = new SpringBeanUtils();

    private ConfigurableApplicationContext cfgContext;

    private SpringBeanUtils() {
        if (INSTANCE != null) {
            throw new Error("error");
        }
    }

    /**
     * get SpringBeanUtils.
     *
     * @return SpringBeanUtils
     */
    public static SpringBeanUtils getInstance() {
        return INSTANCE;
    }

    /**
     * acquire spring bean.
     *
     * @param type type
     * @param <T>  class
     * @return bean
     */
    public <T> T getBean(final Class<T> type) {
        T bean;
        try {
            bean = cfgContext.getBean(type);
        } catch (BeansException e) {
            bean = getByName(type);
        }
        return bean;
    }

    private <T> T getByName(Class<T> type) {
        T bean;
        String className = type.getSimpleName();
        bean = cfgContext.getBean(firstLowercase(firstDelete(className)), type);
        return bean;
    }

    private String firstLowercase(String target) {
        char[] targetChar = target.toCharArray();
        targetChar[0] += 32;
        return String.valueOf(targetChar);
    }

    private static String firstDelete(String target) {
        return target.substring(1, target.length());
    }


    /**
     * register bean in spring ioc.
     *
     * @param beanName bean name
     * @param obj      bean
     */
    public void registerBean(final String beanName, final Object obj) {
        cfgContext.getBeanFactory().registerSingleton(beanName, obj);
    }

    /**
     * set application context.
     *
     * @param cfgContext application context
     */
    public void setCfgContext(final ConfigurableApplicationContext cfgContext) {
        this.cfgContext = cfgContext;
    }

}
