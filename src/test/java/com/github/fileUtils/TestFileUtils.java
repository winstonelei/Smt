package com.github.fileUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import com.github.fileUtils.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author 17090718
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class TestFileUtils {

    @Test
    public void testFileMerge(){
       List<File> listFiles = new ArrayList<>();
       File aFile = new File("D:\\tmp\\winstone\\aa\\a.txt");
       File bFile = new File("D:\\tmp\\winstone\\aa\\b.txt");
       listFiles.add(aFile);
       listFiles.add(bFile);
       File outFile= new File("D:\\tmp\\winstone\\aa\\c.txt");
       FileUtil.mergeFiles(outFile,listFiles);
    }

    public static String readJsonFile(String fileName) {
        String jsonStr = "";
        try {
            File jsonFile = new File(fileName);
            FileReader fileReader = new FileReader(jsonFile);
            Reader reader = new InputStreamReader(new FileInputStream(jsonFile),"utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Test
    public void testReadLine(){
        try {
            String temp =  readJsonFile("D:\\temp\\1.json");
            JSONObject jobj = JSON.parseObject(temp);
            JSONObject jobj2 =   (JSONObject)jobj.get("hits");
            JSONArray jsonArray =  jobj2.getJSONArray("hits");
            List<Alarm> list = new ArrayList<>();
            for(int i=0;i<jsonArray.size();i++){
                JSONObject key = (JSONObject)jsonArray.get(i);
                JSONObject test =  (JSONObject)key.get("_source");

                String alarmId = test.getString("alarmId");
                String alarmIp = test.getString("alarmIp");
                String alarmType = test.getString("alarmType");
                String alarmSourceIp = test.getString("alarmSourceIp");
                String alarmDuration = test.getString("alarmDuration");
                Long alarmBeginTime =test.getLong("alarmBeginTime");
                Long alarmEndTime =test.getLong("alarmEndTime");

                Alarm alarm = new Alarm();
                alarm.setAlarmId(alarmId);
                alarm.setAlarmIp(alarmIp);
                alarm.setAlarmType(alarmType);
                alarm.setAlarmSourceIp(alarmSourceIp);
                alarm.setAlarmDuration(alarmDuration);
                alarm.setAlarmBeginTime(alarmBeginTime);
                alarm.setAlarmEndTime(alarmEndTime);

                list.add(alarm);
                System.out.println(test);
            }

            System.out.println(list);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }


}
