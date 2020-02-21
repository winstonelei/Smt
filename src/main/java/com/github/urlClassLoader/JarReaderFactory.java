package com.github.urlClassLoader;

import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.Set;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author winstone
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class JarReaderFactory {

    public static void main(String[] args) {

        DataReader dataReader = getDataReader();

    }


    public static DataReader getDataReader() {
        try {
            String pluginName = "";//config.getJob().getContent().get(0).getReader().getName();
            String pluginClassName = PluginUtil.getPluginClassName(pluginName);
            Set<URL> urlList = PluginUtil.getJarFileDirPath(pluginName, "");

            return ClassLoaderManager.newInstance(urlList, cl -> {
                Class<?> clazz = cl.loadClass(pluginClassName);
             /*   Constructor constructor = clazz.getConstructor(DataTransferConfig.class, StreamExecutionEnvironment.class);
                return (DataReader)constructor.newInstance(config, env);*/
                Constructor constructor = clazz.getConstructor();
                return (DataReader)constructor.newInstance();
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
