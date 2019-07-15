package com.github.fileUtils;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ����ip������ĳһ��Χ�ڵ�ip
 */

public class IpSegementProcess {

    //ipת����long����
    public static long ipTolong(String ip) {
        char[] c = ip.toCharArray();
        byte[] b = { 0, 0, 0, 0 };
        for (int i = 0, j = 0; i < c.length;) {
            int d = (byte) (c[i] - '0');
            switch (c[i++]) {
                case '.':
                    ++j;
                    break;
                default:
                    if ((d < 0) || (d > 9))
                        return 0;
                    if ((b[j] & 0xff) * 10 + d > 255)
                        return 0;
                    b[j] = (byte) (b[j] * 10 + d);
            }
        }
        return 0x00000000ffffffffl & (b[0] << 24 | (b[1] & 0xff) << 16 | (b[2] & 0xff) << 8 | (b[3] & 0xff));
    }

    static String filePath="D:\\tmp\\spark\\in\\tel.txt";
    static  String fileOutLPath="D:\\tmp\\spark\\in\\telout.txt";

 /*    static String filePath="D:\\tmp\\spark\\in\\tel_1.txt";
    static  String fileOutLPath="D:\\tmp\\spark\\in\\telout_1.txt";*/

    public static void main(String[] args) throws  Exception{
        List<String> logList = FileUtils.readLines(new File(filePath), Charsets.toCharset("UTF-8"));
        List<String> ips = logList.stream().filter(item->item.split(",").length>=2).collect(Collectors.toList());

        int k=0;
        List<IpHid> list = new ArrayList<>();
        for(String str : ips){
            k++;
            System.out.println("process "+k+" ��¼"+str);
            String[] arr = str.split(",");
            String startip=arr[0];
            String endtip=arr[1];
            //String hid =arr[3];
            long start = ipTolong(startip);
            long end = ipTolong(endtip);
            IpHid startHid = new IpHid();
            startHid.setHid("1");
            startHid.setIp(start);
            list.add(startHid);

            while(start<end){
                IpHid ipHid = new IpHid();
                start =start+1L;
                ipHid.setIp(start);
                ipHid.setHid("1");
                list.add(ipHid);
            }
        }

        StringBuffer sb = new StringBuffer();
        int count = 0;
        for(int i=0;i<list.size();i++){
            IpHid ipHid =list.get(i);
            sb.append(ipHid.getIp()+","+ipHid.getHid()+"\r\n");
            if(count>5000){
                AppendFile.method3(fileOutLPath,sb.toString());
                sb=new StringBuffer();
                count=0;
            }
            count++;
        }
        if(sb.toString().contains(",")){
            AppendFile.method3(fileOutLPath,sb.toString());
        }

    }

   /* public static void main(String[] args) throws  Exception{
        List<String> logList = FileUtils.readLines(new File(filePath), Charsets.toCharset("UTF-8"));
        List<String> ips = logList.stream().filter(item->item.split(",").length>3).collect(Collectors.toList());

        int k=0;
        List<IpHid> list = new ArrayList<>();
        for(String str : ips){
            k++;
            System.out.println("process "+k+" ��¼"+str);
            String[] arr = str.split(",");
            String startip=arr[1];
            String endtip=arr[2];
            String hid =arr[3];
            long start = ipTolong(startip);
            long end = ipTolong(endtip);
            IpHid startHid = new IpHid();
            startHid.setHid(hid);
            startHid.setIp(start);
            list.add(startHid);

            while(start<end){
                IpHid ipHid = new IpHid();
                start =start+1L;
                ipHid.setIp(start);
                ipHid.setHid(hid);
                list.add(ipHid);
            }
        }

        StringBuffer sb = new StringBuffer();
        int count = 0;
        for(int i=0;i<list.size();i++){
            IpHid ipHid =list.get(i);
            sb.append(ipHid.getIp()+","+ipHid.getHid()+"\r\n");
            if(count>5000){
                AppendFile.method3(fileOutLPath,sb.toString());
                sb=new StringBuffer();
                count=0;
            }
            count++;
        }
        if(sb.toString().contains(",")){
            AppendFile.method3(fileOutLPath,sb.toString());
        }

    }*/

}
