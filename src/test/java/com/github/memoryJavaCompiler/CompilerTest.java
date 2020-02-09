package com.github.memoryJavaCompiler;

import com.github.javac.in.memory.InMemoryJavaCompiler;
import org.junit.Test;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author winstone
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class CompilerTest {


    @Test
    public void testCompile()throws  Exception{
        StringBuffer sourceCode = new StringBuffer();

        sourceCode.append("package org.mdkt;\n");
        sourceCode.append("public class HelloClass {\n");

        //方法一
        sourceCode.append("   public String hello() { return \"hello\"; }");
        //方法二
        sourceCode.append("   public String helloParam(String aaa) { return \"hello\" + aaa ; }");

        sourceCode.append("}");

        //        Class<?> helloClass = compile("org.mdkt.HelloClass", sourceCode.toString());
        //        Object object = ClassKit.newInstance(helloClass);
        //        System.out.println((Object)ClassKit.invokeMethod(object, "hello"));
        System.out.println(InMemoryJavaCompiler.compileSourceCodeStrAndInvokeMethod("org.mdkt.HelloClass", sourceCode.toString(), "hello"));
        System.out.println(InMemoryJavaCompiler.compileSourceCodeStrAndInvokeMethod("org.mdkt.HelloClass", sourceCode.toString(), "helloParam", "bbbccc"));

        InMemoryJavaCompiler.compileSourceCodeFileAndInvokeMethod("com.lnwazg.kit.reflect.TestMain", "D:\\TestMain.java", "print");
    }
}
