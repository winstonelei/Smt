package com.github.javac.in.memory;

import java.io.File;
import java.util.Arrays;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;

import com.github.javac.in.memory.classloader.DynamicClassLoader;
import com.github.javac.in.memory.javafilemanager.ExtendedStandardJavaFileManager;
import com.github.javac.in.memory.javafileobject.CompiledCode;
import com.github.javac.in.memory.javafileobject.SourceCode;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;


/**
 * 内存内Java编译器
 */
public class InMemoryJavaCompiler
{
    /**
    * javac编译器
    */
    private static JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
    
    /**
     * 将源码编译成指定的Class类，编译的时候需要指定类的全路径
     * @author nan.li
     * @param className
     * @param sourceCodeInText
     * @return
     * @throws Exception
     */
    public static Class<?> compileSourceCodeStr(String className, String sourceCodeInText)
        throws Exception
    {
        //源码对象
        SourceCode sourceCode = new SourceCode(className, sourceCodeInText);
        //编译后的字节码对象
        CompiledCode compiledCode = new CompiledCode(className);
        //源码对象列表
        Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(sourceCode);
        //动态类加载器
        DynamicClassLoader dynamicClassLoader = new DynamicClassLoader(ClassLoader.getSystemClassLoader());
        //扩展的Java文件管理器
        ExtendedStandardJavaFileManager fileManager = new ExtendedStandardJavaFileManager(javac.getStandardFileManager(null, null, null), compiledCode, dynamicClassLoader);
        //定义一个编译任务
        JavaCompiler.CompilationTask task = javac.getTask(null, fileManager, null, null, null, compilationUnits);
        //执行该编译任务
        boolean result = task.call();
      //  Logs.i(String.format("%s的源码编译%s！", className, (result ? "成功" : "失败")));
        System.out.println(String.format("%s的源码编译%s！", className, (result ? "成功" : "失败")));
        //类加载
        return dynamicClassLoader.loadClass(className);
    }
    
    /**
     * 从文件编译
     * @author nan.li
     * @param className
     * @param sourceCodeFile
     * @return
     * @throws Exception
     */
    public static Class<?> compileSourceCodeFile(String className, String sourceCodeFile)
        throws Exception
    {
        return compileSourceCodeFile(className, new File(sourceCodeFile));
    }
    
    /**
     * 从文件编译
     * @author nan.li
     * @param className
     * @param sourceCodeFile
     * @return
     * @throws Exception
     */
    public static Class<?> compileSourceCodeFile(String className, File sourceCodeFile)
        throws Exception
    {
        String sourceCodeInText = FileUtils.readFileToString(sourceCodeFile, CharEncoding.UTF_8);
        if (StringUtils.isNotEmpty(sourceCodeInText))
        {
            return compileSourceCodeStr(className, sourceCodeInText);
        }
        return null;
    }
    
    /**
     * 动态编译源码并执行方法
     * @author nan.li
     * @param className
     * @param sourceCodeInText
     * @param methodName
     * @return
     * @throws Exception
     */
    public static Object compileSourceCodeStrAndInvokeMethod(String className, String sourceCodeInText, String methodName)
        throws Exception
    {
        Class<?> clazz = compileSourceCodeStr(className, sourceCodeInText);
        if (clazz != null)
        {
            Object object = ClassKit.newInstance(clazz);
            return (Object)ClassKit.invokeMethod(object, methodName);
        }
        return null;
    }
    
    /**
     * 动态编译源码并执行方法
     * @author nan.li
     * @param className
     * @param sourceCodeInText
     * @param methodName
     * @return
     * @throws Exception
     */
    public static Object compileSourceCodeStrAndInvokeMethod(String className, String sourceCodeInText, String methodName, Object... args)
        throws Exception
    {
        Class<?> clazz = compileSourceCodeStr(className, sourceCodeInText);
        if (clazz != null)
        {
            Object object = ClassKit.newInstance(clazz);
            return (Object)ClassKit.invokeMethod(object, methodName, args);
        }
        return null;
    }
    
    /**
     * 热编译代码并执行
     * @author nan.li
     * @param className
     * @param sourceCodeFile
     * @param methodName
     * @return
     * @throws Exception
     */
    public static Object compileSourceCodeFileAndInvokeMethod(String className, String sourceCodeFile, String methodName)
        throws Exception
    {
        Class<?> clazz = compileSourceCodeFile(className, sourceCodeFile);
        if (clazz != null)
        {
            Object object = ClassKit.newInstance(clazz);
            return (Object)ClassKit.invokeMethod(object, methodName);
        }
        return null;
    }
    
    /**
     * 热编译代码并执行
     * @author nan.li
     * @param className
     * @param sourceCodeFile
     * @param methodName
     * @param args
     * @return
     * @throws Exception
     */
    public static Object compileSourceCodeFileAndInvokeMethod(String className, String sourceCodeFile, String methodName, Object... args)
        throws Exception
    {
        Class<?> clazz = compileSourceCodeFile(className, sourceCodeFile);
        if (clazz != null)
        {
            Object object = ClassKit.newInstance(clazz);
            return (Object)ClassKit.invokeMethod(object, methodName, args);
        }
        return null;
    }
    
    /**
     * 热编译代码并执行
     * @author nan.li
     * @param className
     * @param sourceCodeFile
     * @param methodName
     * @return
     * @throws Exception
     */
    public static Object compileSourceCodeFileAndInvokeMethod(String className, File sourceCodeFile, String methodName)
        throws Exception
    {
        Class<?> clazz = compileSourceCodeFile(className, sourceCodeFile);
        if (clazz != null)
        {
            Object object = ClassKit.newInstance(clazz);
            return (Object)ClassKit.invokeMethod(object, methodName);
        }
        return null;
    }
    
    /**
     * 热编译代码并执行
     * @author nan.li
     * @param className
     * @param sourceCodeFile
     * @param methodName
     * @param args
     * @return
     * @throws Exception
     */
    public static Object compileSourceCodeFileAndInvokeMethod(String className, File sourceCodeFile, String methodName, Object... args)
        throws Exception
    {
        Class<?> clazz = compileSourceCodeFile(className, sourceCodeFile);
        if (clazz != null)
        {
            Object object = ClassKit.newInstance(clazz);
            return (Object)ClassKit.invokeMethod(object, methodName, args);
        }
        return null;
    }
    
}
