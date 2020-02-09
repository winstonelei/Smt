package com.github.javac.in.memory.javafileobject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import javax.tools.SimpleJavaFileObject;

/**
 * 编译后的字节码的信息描述对象
 */
public class CompiledCode extends SimpleJavaFileObject
{
    /**
     * 字节数组输出流，这就是内存内编译源码的技术关键
     */
    private ByteArrayOutputStream baos = new ByteArrayOutputStream();
    
    public CompiledCode(String className)
        throws Exception
    {
        super(new URI(className), Kind.CLASS);
    }
    
    @Override
    public OutputStream openOutputStream()
        throws IOException
    {
        return baos;
    }
    
    /**
     * 获取编译代码的字节码
     * @author nan.li
     * @return
     */
    public byte[] getByteCode()
    {
        return baos.toByteArray();
    }
}
