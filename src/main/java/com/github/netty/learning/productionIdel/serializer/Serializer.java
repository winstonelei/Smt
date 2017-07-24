package com.github.netty.learning.productionIdel.serializer;


/**
 * @description 序列化工具接口
 */
public interface Serializer {

	/**
	 * 将obj序列化成byte数组
	 * @param obj
	 * @return
	 */
    <T> byte[] writeObject(T obj);

    /**
     * 将byte数组反序列化成class是clazz的obj对象
     * @param bytes
     * @param clazz
     * @return
     */
    <T> T readObject(byte[] bytes, Class<T> clazz);
}
