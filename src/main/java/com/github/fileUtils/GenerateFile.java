package com.github.fileUtils;

import cn.easysb.ip.jip.IPFormatType;
import cn.easysb.ip.jip.JIPAddress;
import cn.easysb.ip.jip.JIPAddressUtils;
import com.github.ip.ipv4.IPV4Util;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
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
public class GenerateFile {
    private static List<IPrange> toIpRange(String cellvalue,String province,String supplier) {
        cellvalue = cellvalue.replaceAll("\\s+-\\s+", "-");
        String[] ipranges;
        if (cellvalue.contains(",")) {
            ipranges = cellvalue.split("\\,");
        } else {
            ipranges = cellvalue.split("\\s+");
        }
        ArrayList<IPrange> Ipranges = new ArrayList<IPrange>();
        for (String ipr : ipranges) {
            ipr = ipr.trim();
            if (ipr.contains("/")) {
                // 掩码的
                String ip = ipr.split("\\/")[0];
                String mask = ipr.split("\\/")[1];
                Nets nets = IPPoolUtil.getEndIP(ip, new Integer(mask));
                String start = nets.getStartIP();
                String end = nets.getEndIP();
                IPrange Iprange = new IPrange();
                Iprange.setStart(Util.convertIPS2Long(start) - 1);
                Iprange.setEnd(Util.convertIPS2Long(end) + 1);
                Iprange.setProvince(province);
                Iprange.setSupplier(supplier);
                Ipranges.add(Iprange);
            } else if (ipr.contains("-")) {
                // 范围的
                String start = ipr.split("\\-")[0];
                String end = ipr.split("\\-")[1];
                IPrange Iprange = new IPrange();
                Iprange.setStart(Util.convertIPS2Long(start));
                Iprange.setEnd(Util.convertIPS2Long(end));
                Iprange.setProvince(province);
                Iprange.setSupplier(supplier);
                Ipranges.add(Iprange);
            } else {
                // 单个ip
                String start = ipr.trim();
                String end = ipr.trim();
                IPrange Iprange = new IPrange();
                Iprange.setStart(Util.convertIPS2Long(start));
                Iprange.setEnd(Util.convertIPS2Long(end));
                Iprange.setProvince(province);
                Iprange.setSupplier(supplier);
                Ipranges.add(Iprange);
            }
        }
        return Ipranges;
    }

    public static void main(String[] args) throws  Exception{

        String filePath="D:\\tmp\\ip20190.txt";
        String fileOutLPath="D:\\tmp\\cleanip.txt";
        List<String> logList = FileUtils.readLines(new File(filePath), Charsets.toCharset("UTF-8"));
        List<String> ips = logList.stream().filter(item->item.split(",").length>=1).collect(Collectors.toList());

        List<IPrange> rangList = new ArrayList<>();
        for(String ip :ips){
            List<IPrange>  rangs=toIpRange(ip,"","");
            rangList.add(rangs.get(0));
        }
        List<IpHid> ipHids = new ArrayList<>();
        for(IPrange iPrange : rangList){
            long start = iPrange.getStart();
            long end = iPrange.getEnd();
            IpHid startHid = new IpHid();
            startHid.setIpStr(IPV4Util.convertIPS2String(start));
            ipHids.add(startHid);

            while(start<end){
                IpHid ipHid = new IpHid();
                start =start+1L;
                ipHid.setIpStr(IPV4Util.convertIPS2String(start));
                ipHids.add(ipHid);
            }
        }
        System.out.println(ipHids);
        File f = new File(fileOutLPath);
        OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(f,true),"UTF-8");
        BufferedWriter writer=new BufferedWriter(write);
        for(IpHid iphid:ipHids){
            String sb =iphid.getIpStr()+"\r\n";
            writer.write(sb);
        }
        writer.close();


        List<String> cleanList = FileUtils.readLines(new File(fileOutLPath), Charsets.toCharset("UTF-8"));
        List<String> cleanIp = cleanList.stream().filter(item->item.split(",").length>=1).collect(Collectors.toList());

        System.out.println(cleanIp);
    }
}
