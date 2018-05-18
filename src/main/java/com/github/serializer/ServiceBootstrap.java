package com.github.serializer;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * serviceBootStrap
 * 实现java SPI实现 加载序列化器
 *
 */
public class ServiceBootstrap {

    public static <S> S loadFirst(Class<S> clazz) {
        final ServiceLoader<S> loader = loadAll(clazz);
        final Iterator<S> iterator = loader.iterator();
        if (!iterator.hasNext()) {
            throw new IllegalStateException(String.format(
                    "No implementation defined in /META-INF/services/%s, please check whether the file exists and has the right implementation class!",
                    clazz.getName()));
        }
        return iterator.next();
    }

    public static <S> ServiceLoader<S> loadAll(Class<S> clazz) {
        return ServiceLoader.load(clazz);
    }
}
