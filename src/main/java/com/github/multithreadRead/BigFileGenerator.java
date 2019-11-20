package com.github.multithreadRead;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * 大文件生成器
 *
 * @author winstone
 * @date 2019/4/28 下午5:44
 */
public class BigFileGenerator {

    public static void main(String args[]) throws Exception{
        //   String filepath = "D:\\slview\\nms\\data\\uddata\\bak\\test.csv";
        BigFileGenerator.createBigFile("D:\\slview\\nms\\data\\uddata\\bak\\test.csv", 1024 * 1024L * 100);
    }

    /**
     * 创建大文件
     *
     * @param path 文件路径
     * @param size 文件大小
     * @throws IOException
     */
    public static void createBigFile(String path, Long size) throws IOException {
        File file = new File(path);

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));

        long start = System.currentTimeMillis();

        //写标题
        String title = "time,src_ip,request_url,dest_ip,dest_port,method,user_agent,connection,server,status,protocol\r\n";
        bufferedWriter.write(title);

        //小于size的情况下逐行写入
        long total = title.getBytes().length;
        long line = 0;
        while (total < size) {
            line++;

            //构造数据行并写入文件
            String tmp = formateData();
            bufferedWriter.write(tmp);

            //更新已写入字节数
            total += tmp.getBytes().length;
        }

        long end = System.currentTimeMillis();

        bufferedWriter.flush();
        bufferedWriter.close();

        System.out.println("total line: " + line);
        System.out.println("cost: " + (end - start) / 1000);
    }


    /**
     * 格式化字符串
     * 1 与title字段一一对应
     * 2 数据项之间用逗号相隔
     * 3 数据行以\r\n结尾
     *
     * @return
     */
    public static String formateData() {

        StringBuffer buffer = new StringBuffer();
        buffer.append(System.currentTimeMillis()).append(",");
        buffer.append(getSrcIp()).append(",");
        buffer.append(getRequestUrl()).append(",");
        buffer.append(getDestIp()).append(",");
        buffer.append(getPort()).append(",");
        buffer.append(getMethodType()).append(",");
        buffer.append(getUseragent()).append(",");
        buffer.append(getConnection()).append(",");
        buffer.append(getServer()).append(",");
        buffer.append(getStatus()).append(",");
        buffer.append(getProtocolType()).append("\r\n");

        return buffer.toString();
    }

    private static String getSrcIp() {
        String[] ips = {"192.168.19.10", "192.168.19.11", "192.168.19.12", "192.168.19.13", "192.168.19.14", "192.168.19.15", "192.168.19.16", "192.168.19.17", "192.168.19.18", "192.168.19.19", "192.168.19.20",
                "192.168.10.10", "192.168.10.11", "192.168.10.12", "192.168.10.13", "192.168.10.14", "192.168.10.15", "192.168.10.16", "192.168.10.17", "192.168.10.18", "192.168.10.19", "192.168.10.20"
        };
        Integer index = Math.abs(new Random().nextInt());
        return ips[index % ips.length];
    }

    private static String getRequestUrl() {
        String appAccount[] = {"/log", "/logout", "/getUsers", "/countUsers", "/list", "/status", "/IPP", "/Jabber", "/Netflix"};
        Integer index = Math.abs(new Random().nextInt());
        return appAccount[index % appAccount.length];
    }

    private static String getDestIp() {
        String[] ips = {"192.168.11.10", "192.168.11.11", "192.168.11.12", "192.168.11.13", "192.168.11.14", "192.168.11.15", "192.168.11.16", "192.168.11.17", "192.168.11.18", "192.168.11.19", "192.168.11.20",
                "192.168.15.10", "192.168.15.11", "192.168.15.12", "192.168.15.13", "192.168.15.14", "192.168.15.15", "192.168.15.16", "192.168.15.17", "192.168.15.18", "192.168.15.19", "192.168.15.20"
        };

        Integer index = Math.abs(new Random().nextInt());
        return ips[index % ips.length];
    }

    private static int getPort() {
        return new Random().nextInt(65535);
    }

    private static String getMethodType() {
        String methodTypes[] = {"trace", "put", "delete", "get", "post"};
        Integer index = Math.abs(new Random().nextInt());
        return methodTypes[index % methodTypes.length];

    }

    private static String getUseragent() {
        String userAgents[] = {"Firefox", "Edge", "windows", "chrome", "Wechat"};
        Integer index = Math.abs(new Random().nextInt());
        return userAgents[index % userAgents.length];

    }

    private static String getConnection() {
        String connections[] = {"no-catched", "keep-alived"};
        Integer index = Math.abs(new Random().nextInt());
        return connections[index % connections.length];
    }

    private static String getServer() {
        String servers[] = {"Mac", "Windows", "Linux", "Android"};
        Integer index = Math.abs(new Random().nextInt());
        return servers[index % servers.length];
    }

    private static String getStatus() {
        String status[] = {"0", "1"};
        Integer index = Math.abs(new Random().nextInt());
        return status[index % status.length];
    }

    private static String getProtocolType() {
        String protocolTypes[] = {"FTP", "Telnet", "http", "Telnet", "AFP", "ICMP", "IPP", "Jabber", "Netflix", "PPTP", "QQ", "Quake", "SAP", "SNMP", "SSH", "SSL", "Teredo"};
        Integer index = Math.abs(new Random().nextInt());
        return protocolTypes[index % protocolTypes.length];
    }


}
