package com.github.javac.in.memory.classloader;

import com.github.javac.in.memory.javafileobject.CompiledCode;

import java.util.HashMap;
import java.util.Map;


/**
 * 动态类加载器
 */
public class DynamicClassLoader extends ClassLoader
{
    /**
     * 类全路径名-字节码描述对象  映射表
     */
    private Map<String, CompiledCode> customCompiledCode = new HashMap<>();
    
    public DynamicClassLoader(ClassLoader parent)
    {
        super(parent);
    }
    
    /**
     * 将字节码信息放入本地映射表里
     * @author nan.li
     * @param cc
     */
    public void putCodeToMap(CompiledCode cc)
    {
        customCompiledCode.put(cc.getName(), cc);
    }
    
    /**
     * {@inheritDoc}
     * 从本地字节码信息库中查询并加载字节码信息
     */
    @Override
    protected Class<?> findClass(String name)
        throws ClassNotFoundException
    {
        CompiledCode cc = customCompiledCode.get(name);
        if (cc == null)
        {
            //双亲委派加载
            return super.findClass(name);
        }
        //找到了，获取其字节码数组信息
        byte[] byteCode = cc.getByteCode();
        //根据字节码信息，定义Class对象
        return defineClass(name, byteCode, 0, byteCode.length);
    }
}
