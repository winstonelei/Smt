package com.github.fileUtils;

import cn.easysb.ip.jip.IPFormatType;
import cn.easysb.ip.jip.JIPAddress;
import cn.easysb.ip.jip.JIPAddressUtils;
import cn.easysb.ip.jip.advanced.JIPAddressCombiner;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *  IP地址合并 大批量
 * @author winstone
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class IpMerge {
    public static void main(String[] args)throws Exception {
        String fileOutLPath="D:\\tmp\\spark\\etloutday\\sipDestIp\\hid=1029\\segement\\t6.txt";
        String filePath = "D:\\tmp\\spark\\etloutday\\sipDestIp\\hid=1029\\segement\\ZH.txt";//JW_0408
        LineIterator it = FileUtils.lineIterator(new File(filePath),"UTF-8");
        List<IpMessage> list = new ArrayList<>();
        try{
            while(it.hasNext()) {
                String logStr = it.nextLine();
                String[] splited = logStr.split(" ");
                IpMessage location = new IpMessage();
                if(splited[0]!=null && !"".equals(splited[0])){
                    location.setBeginIp(splited[0].trim());
                    location.setEndIp(splited[1].trim());
                    location.setLocation(splited[3].trim());
                }
                list.add(location);
                if (list.size() >= 30000) {

                    Map<String,List<IpMessage>> map = list.stream().collect(Collectors.groupingBy(IpMessage::getLocation));
                    List<JIPAddress> allResult = new ArrayList<>();
                    for(String key:map.keySet()){
                        List<IpMessage> tempList = map.get(key);
                        List<JIPAddress> ipAddresList = new ArrayList<>();
                        for(IpMessage ipMessage : tempList){
                            if(ipMessage!=null && !"".equals(ipMessage)){
                                JIPAddress jipAddress = JIPAddressUtils.toIpObject(ipMessage.getBeginIp()+"-"+ipMessage.getEndIp());
                                if(jipAddress==null || "".equals(jipAddress)){
                                    continue;
                                }
                                jipAddress.setData(ipMessage.getLocation());
                                ipAddresList.add(jipAddress);
                            }

                        }
                        List<JIPAddress> result = JIPAddressCombiner.combine(ipAddresList);
                        allResult.addAll(result);
                    }

                    File f = new File(fileOutLPath);
                    OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(f,true),"UTF-8");
                    BufferedWriter writer=new BufferedWriter(write);
                    for(JIPAddress jipAddress:allResult){
                        IpMessage ipMessage = new IpMessage();
                        String tempIpSeg = JIPAddressUtils.toIpString(jipAddress, IPFormatType.SEGMENT_FULL_FIRST);
                        String[] beginAndEnds =tempIpSeg.split("-");
                        ipMessage.setBeginIp(beginAndEnds[0]);
                        ipMessage.setEndIp(beginAndEnds[1]);
                        ipMessage.setLocation(String.valueOf(jipAddress.getData()));

                        String sb =beginAndEnds[0]+" "+beginAndEnds[1]+" "+String.valueOf(jipAddress.getData()+"\r\n");

                        writer.write(sb);
                    }
                    writer.close();
                    list.clear();
                    allResult.clear();

                }
            }
            }catch (Exception e){
            System.out.println(e);
            } finally{
                LineIterator.closeQuietly(it);
            }


            if(list.size()>0){
                Map<String,List<IpMessage>> map = list.stream().collect(Collectors.groupingBy(IpMessage::getLocation));
                List<JIPAddress> allResult = new ArrayList<>();
                for(String key:map.keySet()){
                    List<IpMessage> tempList = map.get(key);
                    List<JIPAddress> ipAddresList = new ArrayList<>();
                    for(IpMessage ipMessage : tempList){
                        if(ipMessage!=null && !"".equals(ipMessage)){
                            JIPAddress jipAddress = JIPAddressUtils.toIpObject(ipMessage.getBeginIp()+"-"+ipMessage.getEndIp());
                            if(jipAddress==null || "".equals(jipAddress)){
                                continue;
                            }
                            jipAddress.setData(ipMessage.getLocation());
                            ipAddresList.add(jipAddress);
                        }

                    }
                    List<JIPAddress> result = JIPAddressCombiner.combine(ipAddresList);
                    allResult.addAll(result);
                }

                File f = new File(fileOutLPath);
                OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(f,true),"UTF-8");
                BufferedWriter writer=new BufferedWriter(write);
                for(JIPAddress jipAddress:allResult){
                    IpMessage ipMessage = new IpMessage();
                    String tempIpSeg = JIPAddressUtils.toIpString(jipAddress, IPFormatType.SEGMENT_FULL_FIRST);
                    String[] beginAndEnds =tempIpSeg.split("-");
                    ipMessage.setBeginIp(beginAndEnds[0]);
                    ipMessage.setEndIp(beginAndEnds[1]);
                    ipMessage.setLocation(String.valueOf(jipAddress.getData()));

                    String sb =beginAndEnds[0]+" "+beginAndEnds[1]+" "+String.valueOf(jipAddress.getData()+"\r\n");
                    writer.write(sb);
                }
                writer.close();
                list.clear();
                allResult.clear();
            }
    }
}
