package com.github.remoteJar;

import com.github.javac.in.memory.ClassKit;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.CharEncoding;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 远程jar包执行工具类<br>
 * 可充分应用于分布式jar加载执行
 * @author nan.li
 * @version 2017年7月6日
 */
public class RemoteJarKit
{
    /**
     * 主类名称的配置信息
     */
    private static final String MAIN_CLASS_NAME_CONFIG = "Main";
    
    /**
     * 加载远程jar包中的类
     * @author nan.li
     * @param urlName  http://192.168.2.17:8080/test.jar<br>
     *                 file://c:/test.jar
     * @param classFullPathName  com.lnwazg.api.Task
     * @return
     */
    public static Class<?> loadRemoteClass(String urlName, String classFullPathName)
    {
        URLClassLoader urlClassLoader = null;
        try
        {
            URL url = new URL(urlName);
            //JVM 及 Dalvik 对类唯一的识别是 ClassLoader id + PackageName + ClassName，所以一个运行程序中是有可能存在两个包名和类名完全一致的类的。
            //并且如果这两个”类”不是由一个 ClassLoader 加载，是无法将一个类的实例强转为另外一个类的，这就是 ClassLoader隔离。
            urlClassLoader = new URLClassLoader(new URL[] {url}, Thread.currentThread().getContextClassLoader());
            //每个classLoader加载出来的类都是独一无二的，都是互不相等的！
            
            //预加载jar包中的所有class，以确保加载运行主类的时候不会出错（尽可能多地将所有可能的依赖类都加载掉）
            loadJarAllClasses(urlName, urlClassLoader);
            
            return urlClassLoader.loadClass(classFullPathName);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            StreamUtils.close(urlClassLoader);
        }
        return null;
    }
    
    /**
     * 加载远程的资源文件的内容
     * @author nan.li
     * @param urlName
     * @param resourceFullPathName
     * @return
     */
    public static String loadRemoteResourceContent(String urlName, String resourceFullPathName)
    {
        URLClassLoader urlClassLoader = null;
        JarFile jarFile = null;
        try
        {
            URL url = new URL(urlName);
            //JVM 及 Dalvik 对类唯一的识别是 ClassLoader id + PackageName + ClassName，所以一个运行程序中是有可能存在两个包名和类名完全一致的类的。
            //并且如果这两个”类”不是由一个 ClassLoader 加载，是无法将一个类的实例强转为另外一个类的，这就是 ClassLoader隔离。
            urlClassLoader = new URLClassLoader(new URL[] {url}, Thread.currentThread().getContextClassLoader());
            //每个classLoader加载出来的类都是独一无二的，都是互不相等的！
            
            //准备加载远程jar中的资源文件信息
            if (!urlName.startsWith("jar:"))
            {
                urlName = String.format("jar:%s!/", urlName);
            }
            //通过jarFile和JarEntry得到所有的类 
            URLConnection urlConnection = null;
            url = new URL(urlName);
            urlConnection = url.openConnection();
            if (urlConnection instanceof JarURLConnection)
            {
                JarURLConnection jarURLConnection = (JarURLConnection)urlConnection;
                jarURLConnection.setUseCaches(true);
                jarFile = jarURLConnection.getJarFile();
                //返回zip文件条目的枚举 
                Enumeration<JarEntry> enumFiles = jarFile.entries();
                JarEntry entry = null;
                //测试此枚举是否包含更多的元素 
                while (enumFiles.hasMoreElements())
                {
                    //压缩包里面的文件项
                    entry = (JarEntry)enumFiles.nextElement();
                    //文件项的全路径
                    String entryName = entry.getName();
                    //                    Logs.d("entryName is: " + entryName);
                    if (resourceFullPathName.equals(entryName))
                    {
                        System.out.println("Find property file OK!");
                        //找到了！
                        return IOUtils.toString(urlClassLoader.getResourceAsStream(entryName), CharEncoding.UTF_8);
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            //资源回收
            StreamUtils.close(jarFile);
            StreamUtils.close(urlClassLoader);
        }
        return null;
    }
    
    /**
     * 加载远程的资源文件的内容为属性表
     * @author nan.li
     * @param urlName
     * @param resourceFullPathName
     * @return
     */
    public static Map<String, String> loadRemotePropertyMap(String urlName, String resourceFullPathName)
    {
        URLClassLoader urlClassLoader = null;
        JarFile jarFile = null;
        try
        {
            URL url = new URL(urlName);
            //JVM 及 Dalvik 对类唯一的识别是 ClassLoader id + PackageName + ClassName，所以一个运行程序中是有可能存在两个包名和类名完全一致的类的。
            //并且如果这两个”类”不是由一个 ClassLoader 加载，是无法将一个类的实例强转为另外一个类的，这就是 ClassLoader隔离。
            urlClassLoader = new URLClassLoader(new URL[] {url}, Thread.currentThread().getContextClassLoader());
            //每个classLoader加载出来的类都是独一无二的，都是互不相等的！
            
            //准备加载远程jar中的资源文件信息
            if (!urlName.startsWith("jar:"))
            {
                urlName = String.format("jar:%s!/", urlName);
            }
            //通过jarFile和JarEntry得到所有的类 
            URLConnection urlConnection = null;
            url = new URL(urlName);
            urlConnection = url.openConnection();
            if (urlConnection instanceof JarURLConnection)
            {
                JarURLConnection jarURLConnection = (JarURLConnection)urlConnection;
                jarURLConnection.setUseCaches(true);
                jarFile = jarURLConnection.getJarFile();
                //返回zip文件条目的枚举 
                Enumeration<JarEntry> enumFiles = jarFile.entries();
                JarEntry entry = null;
                //测试此枚举是否包含更多的元素 
                while (enumFiles.hasMoreElements())
                {
                    //压缩包里面的文件项
                    entry = (JarEntry)enumFiles.nextElement();
                    //文件项的全路径
                    String entryName = entry.getName();
                    //                    Logs.d("entryName is: " + entryName);
                    if (resourceFullPathName.equals(entryName))
                    {
                        System.out.println("Find property file OK!");
                        //找到了！
                        return load(urlClassLoader.getResourceAsStream(entryName), CharEncoding.UTF_8);
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            //资源回收
            StreamUtils.close(jarFile);
            StreamUtils.close(urlClassLoader);
        }
        return null;
    }


    public static Map<String, String> load(InputStream inputStream, String encoding)
    {
        Map<String, String> resMap = new LinkedHashMap<String, String>();//让读取出来的map保持插入顺序显示
        if (inputStream == null)
        {
            System.out.println("inputStream为空！无法加载配置数据信息");
            return resMap;
        }
        try
        {
            List<String> list = IOUtils.readLines(inputStream, encoding);
            int annoNo = 0;
            for (String line : list)
            {
                String lineTrim = org.apache.commons.lang.StringUtils.trim(line);
                if (org.apache.commons.lang.StringUtils.isBlank(lineTrim))
                {
                    continue;
                }
                /*if (isAnno(lineTrim))
                {
                    resMap.put("#" + annoNo++, lineTrim);
                }
                else
                {*/
                    //从第一个等于号开始作为分隔符
                    int equalIndex = org.apache.commons.lang.StringUtils.indexOf(lineTrim, "=");
                    if (equalIndex != -1)
                    {
                        String key = org.apache.commons.lang.StringUtils.substring(lineTrim, 0, equalIndex);
                        String value = org.apache.commons.lang.StringUtils.substring(lineTrim, equalIndex + "=".length());
                        resMap.put(key, value);
                    }
                    //其他情况，则全部都是非法配置，直接忽略即可
                /*}*/
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            StreamUtils.close(inputStream);
        }
        return resMap;
    }
    /**
     * 加载jar包中的所有的Class<br>
     * 避免当调用jar包里的入口类的时候，出现找不到依赖类的问题
     */
    public static void loadJarAllClasses(String urlName, URLClassLoader urlClassLoader)
    {
        //        Logs.i(String.format("预加载Jar包: %s 中的所有的类", urlName));
        //jar:http://www.jcg.com/bar/baz.jar!/
        //jar:file:/c:/Users/1.jar!/
        if (!urlName.startsWith("jar:"))
        {
            urlName = String.format("jar:%s!/", urlName);
        }
        //通过jarFile和JarEntry得到所有的类 
        URLConnection urlConnection = null;
        JarFile jarFile = null;
        try
        {
            URL url = new URL(urlName);
            //jarFile = new JarFile(url.getFile());//这种只适用于本地文件加载，不适用于网络文件加载
            urlConnection = url.openConnection();
            if (urlConnection instanceof JarURLConnection)
            {
                JarURLConnection jarURLConnection = (JarURLConnection)urlConnection;
                jarURLConnection.setUseCaches(true);
                jarFile = jarURLConnection.getJarFile();
                //返回zip文件条目的枚举 
                Enumeration<JarEntry> enumFiles = jarFile.entries();
                JarEntry entry = null;
                //测试此枚举是否包含更多的元素 
                while (enumFiles.hasMoreElements())
                {
                    //压缩包里面的文件项
                    entry = (JarEntry)enumFiles.nextElement();
                    //文件项的全路径
                    String entryName = entry.getName();
                    //                    Logs.i("================entry name is :" + entryName);
                    
                    //entry name is :
                    //META-INF/MANIFEST.MF
                    //.project
                    //.classpath
                    
                    //com/lnwazg/api/Task.class
                    //com/lnwazg/api/Task$Dep3.class
                    //com/lnwazg/api/Dep.class
                    //com/lnwazg/api/Dep2.class
                    
                    //只加载class文件
                    if (entryName.endsWith(".class"))
                    {
                        //com/lnwazg/api/Task.class
                        //com/lnwazg/api/Task$Dep3.class
                        //com/lnwazg/api/Dep.class
                        //com/lnwazg/api/Dep2.class
                        //去掉后缀.class 
                        String className = entryName.substring(0, entryName.length() - ".class".length()).replace("/", ".");
                        //                        Logs.i("正在加载远程Jar包中的类:" + className);
                        urlClassLoader.loadClass(className);
                    }
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        finally
        {
            //资源回收
            StreamUtils.close(jarFile);
        }
    }
    
    /**
     * 加载远程jar包中的类并实例化成对象
     * @author nan.li
     * @param urlName  http://192.168.2.17:8080/test.jar<br>
     *                 file://c:/test.jar
     * @param classFullPathName  com.lnwazg.api.Task
     * @return
     */
    public static Object loadRemoteObject(String urlName, String classFullPathName)
    {
        Class<?> loadRemoteClass = loadRemoteClass(urlName, classFullPathName);
        if (loadRemoteClass != null)
        {
            try
            {
                return loadRemoteClass.newInstance();
            }
            catch (InstantiationException e)
            {
                e.printStackTrace();
            }
            catch (IllegalAccessException e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    /**
     * 加载远程jar包中的类并实例化成对象，然后调用具体的方法
     * @author nan.li
     * @param urlName  http://192.168.2.17:8080/test.jar<br>
     *                 file://c:/test.jar
     * @param classFullPathName  com.lnwazg.api.Task
     * @param methodName 被调用的方法名
     * @return
     */
    public static Object invokeRemoteObject(String urlName, String classFullPathName, String methodName)
    {
        Object remoteObj = loadRemoteObject(urlName, classFullPathName);
        if (remoteObj != null)
        {
            System.out.println(String.format("正在执行远程类:%s的方法:%s", classFullPathName, methodName));
            return (Object)ClassKit.invokeMethod(remoteObj, methodName);
        }
        else
        {
            return null;
        }
    }
    
    /**
     * 加载远程jar包中的类并实例化成对象，然后调用具体的方法
     * @author nan.li
     * @param urlName  http://192.168.2.17:8080/test.jar<br>
     *                 file://c:/test.jar
     * @param classFullPathName  com.lnwazg.api.Task
     * @param methodName 被调用的方法名
     * @param parameterTypes 参数类型数组
     * @param args  参数数组
     * @return
     */
    public static Object invokeRemoteObject(String urlName, String classFullPathName, String methodName, Class<?>[] parameterTypes, Object... args)
    {
        Object remoteObj = loadRemoteObject(urlName, classFullPathName);
        if (remoteObj != null)
        {
            System.out.println(String.format("正在执行远程类:%s的方法:%s", classFullPathName, methodName));
            return (Object) ClassKit.invokeMethod(remoteObj, methodName, parameterTypes, args);
        }
        else
        {
            return null;
        }
    }
    
    /**
     * 根据配置文件，加载远程jar包中的类并实例化成对象，然后调用具体的方法
     * @param urlName
     * @param propertyFileFullPathName
     * @param methodName
     */
    public static Object invokeRemoteObjectByPropertyFile(String urlName, String propertyFileFullPathName, String methodName)
    {
        //jar包中的属性表配置文件
        Map<String, String> propMap = RemoteJarKit.loadRemotePropertyMap(urlName, propertyFileFullPathName);
        if (propMap == null || propMap.isEmpty())
        {
            System.out.println("远程jar包中的入口类配置文件不存在或者信息为空！因此无法执行远程jar包任务！");
            throw new RuntimeException("远程jar包中的入口类配置文件不存在或者信息为空！因此无法执行远程jar包任务！");
        }
        String mainClassFullPath = propMap.get(MAIN_CLASS_NAME_CONFIG);
        if (StringUtils.isEmpty(mainClassFullPath))
        {
            System.out.println("远程jar包中的入口类配置文件中缺少主类配置信息！因此无法执行远程jar包任务！");
            throw new RuntimeException("远程jar包中的入口类配置文件中缺少主类配置信息！因此无法执行远程jar包任务！");
        }
        return RemoteJarKit.invokeRemoteObject(urlName, mainClassFullPath, methodName);
    }
    
    /**
     * 根据配置文件，加载远程jar包中的类并实例化成对象，然后调用具体的方法
     * @author nan.li
     * @param urlName
     * @param propertyFileFullPathName
     * @param methodName
     * @param parameterTypes
     * @param args
     */
    public static Object invokeRemoteObjectByPropertyFile(String urlName, String propertyFileFullPathName, String methodName, Class<?>[] parameterTypes, Object... args)
    {
        //jar包中的属性表配置文件
        Map<String, String> propMap = RemoteJarKit.loadRemotePropertyMap(urlName, propertyFileFullPathName);
        if (propMap == null || propMap.isEmpty())
        {
            System.out.println("远程jar包中的入口类配置文件不存在或者信息为空！因此无法执行远程jar包任务！");
            throw new RuntimeException("远程jar包中的入口类配置文件不存在或者信息为空！因此无法执行远程jar包任务！");
        }
        String mainClassFullPath = propMap.get(MAIN_CLASS_NAME_CONFIG);
        if (StringUtils.isEmpty(mainClassFullPath))
        {
            System.out.println("远程jar包中的入口类配置文件中缺少主类配置信息！因此无法执行远程jar包任务！");
            throw new RuntimeException("远程jar包中的入口类配置文件中缺少主类配置信息！因此无法执行远程jar包任务！");
        }
        return RemoteJarKit.invokeRemoteObject(urlName, mainClassFullPath, methodName, parameterTypes, args);
    }
    
    public static void main(String[] args)
    {
        //        System.out.println((Object)RemoteJarKit.invokeRemoteObject("file:\\c:\\1.jar", "com.lnwazg.api.Task", "getTaskDescription"));
        //        System.out.println((Object)RemoteJarKit.invokeRemoteObject("file:\\c:\\1.jar", "com.lnwazg.api.Task", "execute"));
        //        RemoteJarKit.loadRemoteClass("file:\\c:\\2.jar", "com.lnwazg.api.Dep");
        System.out.println((Object)RemoteJarKit.invokeRemoteObject("file:\\D:\\Documents\\002.jar", "com.lnwazg.Task002", "getTaskDescription"));
        //        System.out.println((Object)RemoteJarKit.invokeRemoteObject("file:\\c:\\2.jar", "com.lnwazg.api.Task", "execute"));
    }
    
}
