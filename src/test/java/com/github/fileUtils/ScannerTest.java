package com.github.fileUtils;

import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author scannerTest
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class ScannerTest {


    @Test
    public void testScanner(){
        ScannerTest scannerTest = new ScannerTest();
        File file = new File("D:\\tmp\\bak");
        List<String> resultList = scannerTest.findCloseBuckets(file);
        System.out.println(resultList);
    }


    public List<String> findCloseBuckets(final File m_baseDir) {
        final Set<String> paths = new HashSet<String>();
        Scanners.forDir().scan(m_baseDir, new Scanners.FileMatcher() {
            @Override
            public Scanners.IMatcher.Direction matches(File base, String path) {
                if (new File(base, path).isFile()) {
                        int index = path.indexOf(".xml");
                        if (index == -1) {
                            paths.add(path);
                        } else {
                            paths.add(path.substring(0, index));
                    }
                }
                return Scanners.IMatcher.Direction.DOWN;
            }
        });
        return new ArrayList<String>(paths);
    }
}
