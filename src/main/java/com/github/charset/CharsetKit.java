package com.github.charset;

import info.monitorenter.cpdetector.io.*;

import java.io.File;
import java.nio.charset.Charset;

/**
 * 字符集工具
 * @author wanglei
 */
public class CharsetKit
{
    /**
     * 获取某个文本文件所采用的字符集
     * @author Administrator
     * @param file
     * @return
     */
    @SuppressWarnings("deprecation")
    public static String getFileCharset(File file)
    {
        if (file == null || !file.exists())
        {
            return null;
        }
        CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
        detector.add(new ParsingDetector(false));
        detector.add(JChardetFacade.getInstance());
        detector.add(ASCIIDetector.getInstance());
        detector.add(UnicodeDetector.getInstance());
        Charset charset = null;
        try
        {
            charset = detector.detectCodepage(file.toURL());
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        if (charset != null)
        {
            return charset.name();
        }
        return null;
    }
    
    /**
     * 获取某个文本文件所采用的字符集
     */
    public static String getFileCharset(String filePath)
    {
        return getFileCharset(new File(filePath));
    }

    public static void main(String[] args) {
        System.out.println(getFileCharset(new File("D:\\b.txt")));
    }

}
