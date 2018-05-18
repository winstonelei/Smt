package com.github.serializer;

/*
  序列化接口
 */
public interface ObjectSerializer{


    /**
     * 对象序列化为字节数组
     * @param object
     * @return
     * @throws Exception
     */
    byte[]  serialize(Object object) throws  Exception;


    /**
     * 将字节数组反序列化对象
     * @param param
     * @param t
     * @param <T>
     * @return
     * @throws Exception
     */
   <T> T deserialize(byte[] param,Class<T> t)throws Exception;

}
