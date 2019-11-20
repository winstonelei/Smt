package com.github.fileUtils;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author winstone
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class ReadTest {
    public static void main(String[] args) throws Exception{


        String filePath="D:\\tmp\\ip20190.txt";
        String fileOutLPath ="D:\\tmp\\ipres1.txt";

        List<String> logList = FileUtils.readLines(new File(filePath), Charsets.toCharset("utf-8"));

        List<String> ips = logList.stream().filter(item->item.split(",").length>=1).collect(Collectors.toList());
        List<String> list = new ArrayList<>();
        int count =0;
        for(String ip :ips){
            String[] array = ip.split(",");
            System.out.println(array[0]+"-,-"+array[1]);
            //toIpRange(ip,"","");
            String temp=array[0]+"--,-"+array[1];
            count++;
            if(count==10){
               break;
            }
            list.add(temp);
        }
        output(list,fileOutLPath);
    }


    private static void output(List<String> list, String fileOutLPath) {
        StringBuffer sb = new StringBuffer();
        int count = 0;
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
               // IpHid ipHid = list.get(i);
                String str =list.get(i);
                sb.append(str+ "\r\n");
                if (count > 5000) {
                    AppendFile.method3(fileOutLPath, sb.toString());
                    sb = new StringBuffer();
                    count = 0;
                }
                count++;
            }
            if (sb.toString().contains(",")) {
                AppendFile.test(fileOutLPath, sb.toString());
            }
        }
    }
}
