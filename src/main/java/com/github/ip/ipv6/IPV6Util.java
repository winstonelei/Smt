package com.github.ip.ipv6;

import java.math.BigInteger;
import java.util.ArrayList;

public class IPV6Util {

    /**
     * 转化数字IP地址到string
     * 
     * @return
     */
    static public final String convertIPS2String(BigInteger ipv6integer) {
        return IPv6Address.fromBigInteger(ipv6integer).toLongString();
    }

    public static final BigInteger convertIPS2BigInteger(String ipv6address) {
        return IPv6Address.fromString(ipv6address).toBigInteger();
    }

    public static ArrayList<IPv6AddressRange> recognizeIPV6Range(String cellvalue) {
        cellvalue = cellvalue.replaceAll("\\s+-\\s+", "-");
        String[] ipranges;
        if (cellvalue.contains(",")) {
            ipranges = cellvalue.split("\\,");
        } else {
            ipranges = cellvalue.split("\\s+");
        }
        ArrayList<IPv6AddressRange> ipv4Ranges = new ArrayList<IPv6AddressRange>();
        ArrayList<IPv6AddressRange> ipv6Ranges = new ArrayList<IPv6AddressRange>();
        for (String ipr : ipranges) {
            if (ipr.contains("/")) {
                // 掩码的
                String ip = ipr.split("\\/")[0];
                String mask = ipr.split("\\/")[1];
                IPv6Network strangeNetwork = IPv6Network.fromString(ipr);
                IPv6AddressRange Iprange =new IPv6AddressRange(strangeNetwork.getFirst(),strangeNetwork.getLast());
                ipv4Ranges.add(Iprange);
            } else if (ipr.contains("-")) {
                // 范围的
                String start = ipr.split("\\-")[0];
                String end = ipr.split("\\-")[1];
                IPv6AddressRange Iprange =new IPv6AddressRange(IPv6Address.fromString(start),IPv6Address.fromString(end));
                ipv4Ranges.add(Iprange);
            } else if (ipr.contains(":")){
                // 单个ip
                String start = ipr.trim();
                String end = ipr.trim();
                IPv6AddressRange Iprange =new IPv6AddressRange(IPv6Address.fromString(start),IPv6Address.fromString(end));
                ipv4Ranges.add(Iprange);
            }
        }
        return ipv4Ranges;
    }

    public static void main(String[] args) {

        //convertIPS2String();
        System.out.println(IPV6Util.convertIPS2BigInteger("2001:0db8:3c4d:0015:0000:0000:1a2f:1a2b"));
        BigInteger bigInteger = new BigInteger("42540766429944781121676641069932943915");
        System.out.println(convertIPS2String(bigInteger));
    }
}
