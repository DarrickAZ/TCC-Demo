package com.luke.milo.common.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

/**
 * @Descrtption MiloInvocationBean
 * @Author luke
 * @Date 2019/9/19
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MiloInvocationBean implements Serializable {

    private static final long serialVersionUID = -6710601723234590145L;

    private Class targetClass;

    private String methodName;

    private Class[] parameterTypes;

    private Object[] args;

}
