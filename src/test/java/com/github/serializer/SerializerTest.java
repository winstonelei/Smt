package com.github.serializer;

import java.util.ServiceLoader;
import java.util.stream.StreamSupport;

/**
 * 序列化测试类
 */
public class SerializerTest {

    public static void main(String[] args) {
        final ServiceLoader<ObjectSerializer> objectSerializers = ServiceBootstrap.loadAll(ObjectSerializer.class);
        StreamSupport.stream(objectSerializers.spliterator(), false)
                .forEach(objectSerializer -> System.out.println(objectSerializer.toString()));
    }
}
