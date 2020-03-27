package com.github.ioUtil;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author winstone
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class IoUtilTest {


    public static void main(String[] args) {



    }
    private void loadTest() {
        InputStream ipsin = null;
        try {
            ipsin = IoUtilTest.class.getResourceAsStream("country.data");
            List<String> list = IOUtils.readLines(ipsin, Charsets.toCharset("UTF-8"));

            for (int i = 0; i < list.size(); i++) {
                String str = null;
                try {
                    str = list.get(i);
                    String[] strArray = str.split(",");
                    if (strArray.length != 4) {
                        continue;
                    }
                    // AM,Armenia,亚美尼亚,AM.png
                  /*  CountryInfo info = new CountryInfo();
                    info.setEnShort(strArray[0]);
                    info.setEnname(strArray[1]);
                    info.setCnname(strArray[2]);
                    info.setEnicon(strArray[3]);

                    cnmap.put(info.getCnname(), info);
                    enmap.put(info.getEnShort(), info);*/
                } catch (Exception e) {
                    System.out.println(e);
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            IOUtils.closeQuietly(ipsin);
        }
    }

}
