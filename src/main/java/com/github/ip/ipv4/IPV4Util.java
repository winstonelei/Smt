package com.github.ip.ipv4;

import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;
import java.util.ArrayList;

public abstract class IPV4Util {

    /**
     * 转化数字IP地址到string
     * 
     * @param addr
     * @return
     */
    static public final String convertIPS2String(long longIp) {
        StringBuffer sb = new StringBuffer("");
        // 直接右移24位
        sb.append(String.valueOf((longIp >>> 24)));
        sb.append(".");
        // 将高8位置0，然后右移16位
        sb.append(String.valueOf((longIp & 0x00FFFFFF) >>> 16));
        sb.append(".");
        // 将高16位置0，然后右移8位
        sb.append(String.valueOf((longIp & 0x0000FFFF) >>> 8));
        sb.append(".");
        // 将高24位置0
        sb.append(String.valueOf((longIp & 0x000000FF)));
        return sb.toString();
    }

    public static final long convertIPS2Long(String ipaddress) {
        long[] ip = new long[4];
        int i = 0;
        for (String ipStr : ipaddress.split("\\.")) {
            ip[i] = Long.parseLong(ipStr);
            i++;
        }
        return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
    }

    public static ArrayList<IPV4Range> recognizeIPV4Range(String cellvalue) {
        ArrayList<IPV4Range> Ipranges = new ArrayList<IPV4Range>();
        if (StringUtils.isBlank(cellvalue)){
            return Ipranges;
        }
        cellvalue = cellvalue.replaceAll("\\s+-\\s+", "-");
        String[] ipranges;
        if (cellvalue.contains(",")) {
            ipranges = cellvalue.split("\\,");
        } else {
            ipranges = cellvalue.split("\\s+");
        }
        for (String ipr : ipranges) {
            ipr = ipr.trim();
            if (ipr.contains("/")) {
                // 掩码的
                String ip = ipr.split("\\/")[0];
                String mask = ipr.split("\\/")[1];
                IPV4Nets nets = IPV4PoolUtil.getEndIP(ip, new Integer(mask));
                String start = nets.getStartIP();
                String end = nets.getEndIP();
                IPV4Range Iprange = new IPV4Range();
                Iprange.setStart(IPV4Util.convertIPS2Long(start) - 1);
                Iprange.setEnd(IPV4Util.convertIPS2Long(end) + 1);
                Ipranges.add(Iprange);
            } else if (ipr.contains("-")) {
                // 范围的
                String start = ipr.split("\\-")[0];
                String end = ipr.split("\\-")[1];
                IPV4Range Iprange = new IPV4Range();
                Iprange.setStart(IPV4Util.convertIPS2Long(start));
                Iprange.setEnd(IPV4Util.convertIPS2Long(end));
                Ipranges.add(Iprange);
            } else if (ipr.split("\\.").length==4){
                // 单个ip
                String start = ipr.trim();
                String end = ipr.trim();
                IPV4Range Iprange = new IPV4Range();
                Iprange.setStart(IPV4Util.convertIPS2Long(start));
                Iprange.setEnd(IPV4Util.convertIPS2Long(end));
                Ipranges.add(Iprange);
            }
        }
        return Ipranges;
    }

    public static void main(String[] args) {
        String ip = "192.168.1.1";
        BigInteger a = new BigInteger("42540766429944781121676641069932943915") ;//BigInteger.valueOf("42540766429944781121676641069932943915");

        System.out.println(convertIPS2Long(ip));
        System.out.println(convertIPS2String(convertIPS2Long(ip)));
        System.out.println(convertIPS2String(convertIPS2Long(ip)));
    }
}
