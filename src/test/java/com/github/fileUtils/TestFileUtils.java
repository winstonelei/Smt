package com.github.fileUtils;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import com.github.fileUtils.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author 17090718
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class TestFileUtils {

    @Test
    public void testFileMerge(){
       List<File> listFiles = new ArrayList<>();
       File aFile = new File("D:\\tmp\\winstone\\aa\\a.txt");
       File bFile = new File("D:\\tmp\\winstone\\aa\\b.txt");
       listFiles.add(aFile);
       listFiles.add(bFile);
       File outFile= new File("D:\\tmp\\winstone\\aa\\c.txt");
       FileUtil.mergeFiles(outFile,listFiles);
    }

    @Test
    public void testReadLine(){
        try {
            System.out.println(FileUtils.readLines(new File("D:\\tmp\\winstone\\aa\\a.txt"), Charsets.toCharset("UTF-8")));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }


}
