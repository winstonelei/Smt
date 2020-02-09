package com.github.javac.in.memory.javafilemanager;

import com.github.javac.in.memory.classloader.DynamicClassLoader;
import com.github.javac.in.memory.javafileobject.CompiledCode;

import java.io.IOException;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;


/**
 * 扩展的Java源码文件管理器
 */
public class ExtendedStandardJavaFileManager extends ForwardingJavaFileManager<JavaFileManager>
{
    private CompiledCode compiledCode;
    
    private DynamicClassLoader dynamicClassLoader;
    
    /**
     * Creates a new instance of ForwardingJavaFileManager.
     *
     * @param fileManager delegate to this file manager
     * @param dynamicClassLoader
     */
    public ExtendedStandardJavaFileManager(JavaFileManager fileManager, CompiledCode compiledCode, DynamicClassLoader dynamicClassLoader)
    {
        super(fileManager);
        this.compiledCode = compiledCode;
        this.dynamicClassLoader = dynamicClassLoader;
        this.dynamicClassLoader.putCodeToMap(compiledCode);
    }
    
    @Override
    public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling)
        throws IOException
    {
        return compiledCode;
    }
    
    @Override
    public ClassLoader getClassLoader(Location location)
    {
        return dynamicClassLoader;
    }
}
