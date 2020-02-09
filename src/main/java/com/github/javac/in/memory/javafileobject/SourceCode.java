package com.github.javac.in.memory.javafileobject;

import java.io.IOException;
import java.net.URI;

import javax.tools.SimpleJavaFileObject;

/**
 * Java源代码对象(*.java)
 */
public class SourceCode extends SimpleJavaFileObject
{
    /**
     * Java源代码
     */
    private String contents = null;
    
    public SourceCode(String className, String contents)
        throws Exception
    {
        super(URI.create("string://" + className.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
        this.contents = contents;
    }
    
    /**
     * {@inheritDoc}
     * 获取源码的内容
     */
    public CharSequence getCharContent(boolean ignoreEncodingErrors)
        throws IOException
    {
        return contents;
    }
}
