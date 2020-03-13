package com.github.calcite;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author winstone
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 * 基于caclite sql 解析统计数据
 */
public class CalciteTest {

    @Test
    public void testSqlQuery()throws Exception{
        JSONObject tabSchema = new JSONObject();
        tabSchema.put("id", "integer");
        tabSchema.put("name", "varchar");
        tabSchema.put("age", "integer");

        String tableName = "stu";

        JSONArray dataSets = new JSONArray();

        for (int i = 0; i < 5000; i++) {
            JSONObject object = new JSONObject();
            object.put("id", i);
            object.put("name", "aa" + i);
            object.put("age", 10 + i);
            dataSets.add(object);
        }

       // String sql = "select * from \"stu\" where \"id\"=0 and \"age\"=10 limit 10";

        String sql2 = "select count(*) from \"stu\" ";

        List<JSONArray> dts = new ArrayList<>();
        dts.add(dataSets);
        long start = System.currentTimeMillis();
        String rs = JSqlUtils.query(tabSchema, tableName, dts, sql2);
        System.out.println("[Spent] :: " + (System.currentTimeMillis() - start) + "ms");
        System.out.println(rs);
    }
}
