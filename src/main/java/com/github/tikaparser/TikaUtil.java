package com.github.tikaparser;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.microsoft.ooxml.OOXMLParser;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created by winstone on 2017/5/19.
 * 实现pdf和word解析
 */
public class TikaUtil {

    private  static  TikaUtil tikaUtil = new TikaUtil();

    public static TikaUtil getInstance(){
        if(null == tikaUtil){
            tikaUtil = new TikaUtil();
        }
        return tikaUtil;
    }

    /**
     * pdf
     * @param file
     * @return
     */
    public  String getPdfText(File file){
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
    try{
        FileInputStream inputstream = new FileInputStream(file);
        ParseContext pcontext = new ParseContext();
        //parsing the document using PDF parser
        PDFParser pdfparser = new PDFParser();
        pdfparser.parse(inputstream, handler, metadata,pcontext);

        //getting the content of the document
        //System.out.println("Contents of the PDF :" + handler.toString());

        //getting metadata of the document
        System.out.println("Metadata of the PDF:");
        String[] metadataNames = metadata.names();

        for(String name : metadataNames) {
            System.out.println(name+ " : " + metadata.get(name));
        }
    }catch (Exception e){
        e.printStackTrace();
    }
       return handler.toString();
    }

    /**
     * 获取word文档
     * @param file
     * @return
     */
    public String getWordText(File file){
       ContentHandler handler = new BodyContentHandler();
    try {
        Parser parser = new OOXMLParser();
        Metadata metadata = new Metadata();
        metadata.set(Metadata.RESOURCE_NAME_KEY, file.getName());
        InputStream is = new FileInputStream(file);
        ParseContext context = new ParseContext();
        context.set(Parser.class, parser);
        //2、执行parser的parse()方法。
        parser.parse(is, handler, metadata, context);
        for (String name : metadata.names()) {
            System.out.println(name + ":" + metadata.get(name));
        }
        System.out.println(handler.toString());
    }catch(Exception e){
        e.printStackTrace();
    }
        return handler.toString();
}

}
