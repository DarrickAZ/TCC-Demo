package com.luke.milo.common.serializer;

import com.luke.milo.common.exception.MiloException;

/**
 * @Descrtption 序列化器
 * @Author luke
 * @Date 2019/9/24
 **/
public interface ObjectSerializer {

    byte[] serialize(Object obj) throws MiloException;


    <T> T deSerialize(byte[] param, Class<T> clazz) throws MiloException;

}
