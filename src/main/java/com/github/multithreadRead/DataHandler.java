package com.github.multithreadRead;

import java.sql.PreparedStatement;

/**
 * 数据处理器
 * @author winston
 * @date 2019/4/28 下午7:40
 */
public class DataHandler {

    private DBConnector dbConnector;

    private PreparedStatement preparedStatement;

    //插入数据sql
    private static final String INSERT_DATA_QL = "insert into web_request_multiple(time,src_ip,request_url,dest_ip,dest_port,method,user_agent,connection,server,status,protocol) values(?,?,?,?,?,?,?,?,?,?,?)";

    //计数器，和maxBatch搭配使用，当counter达到maxBatch时写入数据库
    private int counter = 0;

    //处理的总行数
    private long totalLines = 0;

    //批量插入, 每200条写入一次
    private int maxBatch = 200;


    public DataHandler() {
       /* try {
            this.dbConnector = new DBConnector();
            preparedStatement = dbConnector.openConnection().prepareStatement(INSERT_DATA_QL);
        }catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    /**
     * 逐行批量将数据写入Mysql
     * @param content 一行内容
     * @param isLastLine 是否是最后一行
     */
    public void handle(String content, boolean isLastLine) throws Exception {
        totalLines++;
        counter++;
        System.out.println(content);
        //填充sql参数 todo 传进来的content不是一行数据，说明根据换行符\r\n提取行相关逻辑仍然有缺陷
        String fields[] = content.split(",");
        System.out.println("fileds: " +  fields.length);
      /*  for (int i = 1; i <= fields.length; i++) {
            //特殊处理int和long类型的字段
            if (i == 1) { //time 时间戳
                preparedStatement.setObject(i, Long.parseLong(fields[i-1]));
            }else if (i == 5) { //destPort 端口号
                preparedStatement.setObject(i, Integer.parseInt(fields[i-1]));
            }else {
                preparedStatement.setObject(i, fields[i-1]);
            }
        }

        //批量添加
        preparedStatement.addBatch();
        if (counter == maxBatch) {
            preparedStatement.executeBatch();
            preparedStatement.clearBatch();
            counter = 0;
        }

        //最后一行收尾
        if (isLastLine && counter > 0) {
            preparedStatement.executeBatch();
            preparedStatement.clearBatch();
            preparedStatement.close();
            dbConnector.closeConnection();
        }*/

        System.out.println(Thread.currentThread().getName() + " parse total line is:"+ totalLines);

    }
}
