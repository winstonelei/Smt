package com.github.scheduler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author 17090718
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class MapTest {
    public static void main(String[] args) {
        Map<String,String> map1 =new HashMap<>();
        map1.put("zs","121");
        map1.put("lisi","232");

        Map<String,String> map2 =new HashMap<>();
        map2.put("zs","666");

       // map1.putAll(map2);
        map2.putAll(map1);

        Set<Map.Entry<String,String>> set = map2.entrySet();
        Iterator iterator = set.iterator();

        while (iterator.hasNext()){
          Map.Entry<String,String> entry = (Map.Entry<String, String>) iterator.next();
          System.out.println(entry.getKey() + "=" + entry.getValue());
        }


    }
}
