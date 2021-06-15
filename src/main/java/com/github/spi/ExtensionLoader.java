package com.github.spi;

import java.util.Objects;
import java.util.ServiceLoader;
import java.util.stream.StreamSupport;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author winstone
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class ExtensionLoader<T>  {

    private Class<T> type;

    private ExtensionLoader(final Class<T> type) {
        this.type = type;
    }

    private static <T> boolean withExtensionAnnotation(final Class<T> type) {
        return type.isAnnotationPresent(MySPI.class);
    }

    /**
     * Gets extension loader.
     *
     * @param <T>  the type parameter
     * @param type the type
     * @return the extension loader
     */
    public static <T> ExtensionLoader<T> getExtensionLoader(final Class<T> type) {
        if (type == null) {
            throw new MythException("type == null");
        }
        if (!type.isInterface()) {
            throw new MythException("Extension type(" + type + ") not interface!");
        }
        if (!withExtensionAnnotation(type)) {
            throw new MythException("type" + type.getName() + "not exist");
        }
        return new ExtensionLoader<>(type);
    }

    /**
     * Gets activate extension.
     *
     * @param value the value
     * @return the activate extension
     */
    public T getActivateExtension(final String value) {
        ServiceLoader<T> loader = ServiceBootstrap.loadAll(type);
        return StreamSupport.stream(loader.spliterator(), false)
                .filter(e -> Objects.equals(e.getClass()
                        .getAnnotation(MySPI.class).value(), value))
                .findFirst().orElseThrow(() -> new MythException("Please check your configuration"));
    }

}
