package com.github.ip.ip;

import cn.easysb.ip.jip.JIPAddress;
import cn.easysb.ip.jip.JIPAddressUtils;
import com.github.ip.ipv4.IPV4Nets;
import com.github.ip.ipv4.IPV4PoolUtil;
import com.github.ip.ipv4.IPV4Range;
import com.github.ip.ipv4.IPV4Util;
import com.github.ip.ipv6.IPv6Address;
import com.github.ip.ipv6.IPv6AddressRange;
import com.github.ip.ipv6.IPv6Network;
import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.ArrayList;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author winstone
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class IpCustConvert {
    static Logger logger = LoggerFactory.getLogger(IpCustConvert.class.getName());

    public static RangeMap<BigInteger, HouseInfo> CustIpRange(RangeMap<BigInteger, HouseInfo> custRangeMap_ip, HouseInfo houseInfo){
        ArrayList<IPV4Range> ipv4Ranges = new ArrayList<IPV4Range>();
        ArrayList<IPv6AddressRange> ipv6Ranges = new ArrayList<IPv6AddressRange>();
          String  beginIp = houseInfo.getStartIp();
          JIPAddress jipAddress = JIPAddressUtils.toIpObject(beginIp);
          int code=0;
            try{
                 code = jipAddress.getIpType().getCode();
            }catch (Exception e){
                //logger.error("IpCustConvert jipAddress error {} ,Customer name ="+customer.getCustomername()+",ip="+ipr,e);
            }
            IPV4Range Iprange = new IPV4Range();
            String start="";
            String end="";
            switch (code){
                case 1://ipv4
                    start = houseInfo.getStartIp().trim();
                    end = houseInfo.getEndIp().trim();
                    Iprange.setStart(IPV4Util.convertIPS2Long(start));
                    Iprange.setEnd(IPV4Util.convertIPS2Long(end));
                    ipv4Ranges.add(Iprange);
                    break;
                case 3://IPV4段
                    start = houseInfo.getStartIp().split("\\-")[0];
                    end = houseInfo.getEndIp().split("\\-")[1];
                    Iprange.setStart(IPV4Util.convertIPS2Long(start));
                    Iprange.setEnd(IPV4Util.convertIPS2Long(end));
                    ipv4Ranges.add(Iprange);
                    break;
                case 4: //IPV4段带掩码
                    String ip =  houseInfo.getStartIp().split("\\/")[0];
                    String mask = houseInfo.getEndIp().split("\\/")[1];
                    IPV4Nets nets = IPV4PoolUtil.getEndIP(ip, new Integer(mask));
                    start = nets.getStartIP();
                    end = nets.getEndIP();
                    Iprange.setStart(IPV4Util.convertIPS2Long(start) - 1);
                    Iprange.setEnd(IPV4Util.convertIPS2Long(end) + 1);
                    ipv4Ranges.add(Iprange);
                    break;
                case 2://ipv6
                    start = houseInfo.getStartIp().trim();
                    end = houseInfo.getEndIp().trim();
                    IPv6AddressRange iPv6Addresses =new IPv6AddressRange(IPv6Address.fromString(start),IPv6Address.fromString(end));
                    ipv6Ranges.add(iPv6Addresses);
                    break;
                case 5://IPV6段
                    start =  houseInfo.getStartIp().trim().split("\\-")[0];
                    end =  houseInfo.getEndIp().trim().split("\\-")[1];
                    IPv6AddressRange iPv6Addresses1 =new IPv6AddressRange(IPv6Address.fromString(start),IPv6Address.fromString(end));
                    ipv6Ranges.add(iPv6Addresses1);
                    break;
                case 6://IPV6段带掩码
                    ip =  houseInfo.getStartIp().split("\\/")[0];
                    mask =  houseInfo.getEndIp().split("\\/")[1];
                    IPv6Network strangeNetwork = IPv6Network.fromString(ip);
                    IPv6AddressRange ipv6Range =new IPv6AddressRange(strangeNetwork.getFirst(),strangeNetwork.getLast());
                    ipv6Ranges.add(ipv6Range);
                    break;
                default :
                    break;
        }

        if(CollectionUtils.isNotEmpty(ipv4Ranges)){
            for(IPV4Range iprange :ipv4Ranges){
                custRangeMap_ip.put(Range.closed(BigInteger.valueOf(iprange.getStart()), BigInteger.valueOf(iprange.getEnd())), houseInfo);
            }
        }

        if(CollectionUtils.isNotEmpty(ipv6Ranges)){
            for (IPv6AddressRange iprange : ipv6Ranges) {
                custRangeMap_ip.put(Range.closed(iprange.getFirst().toBigInteger(), iprange.getLast().toBigInteger()), houseInfo);
            }
        }
        return custRangeMap_ip;
    }

    public static void main(String[] args) {
/*       String ipr="8080::226:2dff:fefa:210-8080::226:2dff:fefa:220";
       String start = ipr.split("\\-")[0];
       String end = ipr.split("\\-")[1];
       IPv6AddressRange iPv6Addresses1 =new IPv6AddressRange(IPv6Address.fromString(start),IPv6Address.fromString(end));

        System.out.println(iPv6Addresses1);*/
/*        String  ipr="8080::226:2dff:fefa:310/127";
        String  ip = ipr.split("\\/")[0];
        String  mask = ipr.split("\\/")[1];
        IPv6Network strangeNetwork = IPv6Network.fromString(ipr);
        IPv6AddressRange ipv6Range =new IPv6AddressRange(strangeNetwork.getFirst(),strangeNetwork.getLast());
        System.out.println(ipv6Range);*/
        JIPAddress jipAddress = JIPAddressUtils.toIpObject("180.153.28.64-180.153.28.145");
        int code=0;
        try{
            code = jipAddress.getIpType().getCode();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
