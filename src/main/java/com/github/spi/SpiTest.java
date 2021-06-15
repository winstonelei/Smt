package com.github.spi;

import com.github.serializer.ObjectSerializer;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author winstone
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class SpiTest {
    public static void main(String[] args) {
        //spi serialize
        final ObjectSerializer serializer = ExtensionLoader.getExtensionLoader(ObjectSerializer.class)
                .getActivateExtension("kryo");

        System.out.println(serializer);
        //注册到spring 容器中
        //SpringBeanUtils.getInstance().registerBean(ObjectSerializer.class.getName(), serializer);


    }
}
