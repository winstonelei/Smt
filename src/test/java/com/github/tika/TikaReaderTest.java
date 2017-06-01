package com.github.tika;

import com.github.tikaparser.TikaUtil;
import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created by winstone on 2017/5/19.
 */
public class TikaReaderTest {

    public static void main(final String[] args) throws Exception {

        /*String str = TikaUtil.getInstance().getPdfText(new File("F:\\tmp\\webmagic\\abc.pdf")) ;
        System.out.println(str);*/

        String str1 = TikaUtil.getInstance().getWordText(new File("F:\\tmp\\tab.docx")) ;
        System.out.println(str1);
/*        InputStream input=new FileInputStream(new File("F:\\tmp\\yyy.xlsx"));//可以写文件路径，pdf，word，html等
        BodyContentHandler textHandler=new BodyContentHandler();
        Metadata matadata=new Metadata();//Metadata对象保存了作者，标题等元数据
        Parser parser=new AutoDetectParser();//当调用parser，AutoDetectParser会自动估计文档MIME类型，此处输入pdf文件，因此可以使用PDFParser
        ParseContext context=new ParseContext();
        parser.parse(input, textHandler, matadata, context);//执行解析过程
        input.close();
        System.out.println("Title: "+matadata.get(Metadata.TITLE));
        System.out.println("Type: "+matadata.get(Metadata.TYPE));
        System.out.println("Body: "+textHandler.toString());//从textHandler打印正文*/
    }
}
