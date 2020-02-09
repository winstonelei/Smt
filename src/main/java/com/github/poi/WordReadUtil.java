package com.github.poi;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by winstone on 2017/2/5.
 */
public class WordReadUtil {

    private static Logger logger = LoggerFactory
            .getLogger(WordReadUtil.class);

    public String getFileContent(String filePath) {

        String result = "";

        if (filePath.endsWith(".txt")) {
            result = getHdfsTextContent(filePath);
        }

        if (filePath.endsWith(".doc")) {
            result = getHdfsWord2003Content(filePath);
        }

        if (filePath.endsWith(".docx")) {
            result = getHdfsWord2007Content(filePath);
        }

        return result;
    }

    private String getHdfsTextContent(String filePath) {

        InputStream ins;

        StringBuffer sb = new StringBuffer();
        try {
            ins = getFileInputString(filePath);

            BufferedReader br = new BufferedReader(new InputStreamReader(ins));

            int len = 0;
            String line = null;

            while ((line = br.readLine()) != null) {

                if (len != 0) {
                    sb.append("\r\n" + line);
                } else {
                    sb.append(line);
                }
                len++;
            }
            ins.close();
            br.close();
        } catch (Exception e) {
            logger.error("getHdfsTextContent error");
        }

        return sb.toString();
    }

    private String getHdfsWord2003Content(String filePath) {

        InputStream ins;

        HWPFDocument hdt = null;

        String result = "";

        try {
            ins = getFileInputString(filePath);

           // hdt = new HWPFDocument(ins);

            WordExtractor extractor = new WordExtractor(ins);
            //输出word文档所有的文本

           // Range range = hdt.getRange();

            //result = range.text();
            result = extractor.getText();

            ins.close();
        } catch (Exception e1) {
            logger.error("getHdfsWord2003Content error");
        }

        return result;
    }

    private String getHdfsWord2007Content(String filePath) {

        InputStream ins;

        XWPFDocument xwpf;

        String result = "";

        try {
          /*  ins = getFileInputString(filePath);

            xwpf = new XWPFDocument(ins);

            POIXMLTextExtractor extractor = new XWPFWordExtractor(xwpf);

            result = extractor.getText();

            ins.close();*/
        } catch (Exception e) {
            logger.error("getHdfsWord2007Content error");
        }
        return result;
    }


    private InputStream getFileInputString(String filePath) throws IOException,
            URISyntaxException {

        InputStream ins;

        ins = new FileInputStream(filePath);
       /* if (filePath.startsWith("har:")) {
            HDFSUtils utils = HDFSUtils.getInstance();

            Configuration conf = utils.getConfiguration();

            HarFileSystem fs = new HarFileSystem();
            fs.initialize(new URI(getFileFullPath(filePath)), conf);

            ins = fs.open(new Path(getFileFullName(filePath)), 1024);
        } else {

            ins = HDFSystem.getDFSInputStream(filePath);
        }*/
        return ins;
    }


    public void testWrite()throws  Exception{
        String templatePath = "F:\\tmp\\template.doc";
        InputStream is = new FileInputStream(templatePath);
        HWPFDocument doc = new HWPFDocument(is);
        Range range = doc.getRange();
        //把range范围内的${reportDate}替换为当前的日期
        range.replaceText("${reportDate}", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        range.replaceText("${appleAmt}", "100.00");
        range.replaceText("${bananaAmt}", "200.00");
        range.replaceText("${totalAmt}", "300.00");
        OutputStream os = new FileOutputStream("F:\\tmp\\write.doc");
        //把doc输出到输出流中
        doc.write(os);
        this.closeStream(os);
        this.closeStream(is);
    }

    /**
     * 关闭输入流
     * @param is
     */
    private void closeStream(InputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭输出流
     * @param os
     */
    private void closeStream(OutputStream os) {
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args){
        WordReadUtil readUtil = new WordReadUtil();
        //String doc = readUtil.getHdfsWord2007Content("F:\\tmp\\test1.docx");
        String doc = readUtil.getHdfsWord2003Content("F:\\tmp\\test.doc");
        System.out.println(doc.trim());
        try {
          //  readUtil.testWrite();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
