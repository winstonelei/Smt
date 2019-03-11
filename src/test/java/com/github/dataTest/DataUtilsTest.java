package com.github.dataTest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.dataUtils.DataUtil;
import org.junit.Test;

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
public class DataUtilsTest {

    @Test
    public void  sortTest(){
        TestWork testWork = new TestWork();
        testWork.setId(1);
        testWork.setName("zs");
        testWork.setOrderNumber(1000);

        TestWork testWork1 = new TestWork();
        testWork1.setId(2);
        testWork1.setName("lisi");
        testWork1.setOrderNumber(2000);

        TestWork testWork3 = new TestWork();
        testWork3.setId(3);
        testWork3.setName("ww");
        testWork3.setOrderNumber(3000);
        List<TestWork> iqlExcutions = new ArrayList<>();//iqlExcutionRepository.findByIqlLike(vo.getSearch());
        iqlExcutions.add(testWork);
        iqlExcutions.add(testWork1);
        iqlExcutions.add(testWork3);

        JSONArray rows = new JSONArray();
        JSONObject res = new JSONObject();
        for (TestWork e : iqlExcutions) {
            rows.add(e.toJSON());
        }
        DataUtil.sort(rows, "orderNumber", "desc");
        res.put("total", rows.size());
        res.put("rows", DataUtil.pageFormat(rows, 0,2));
        System.out.println(res);
    }
}
