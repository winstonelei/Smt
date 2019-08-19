package com.github.colllection;

import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.*;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * 集合类测试 Map 相关方法判断是否含有相同key
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class GuavaCollectionTest {
    public static void main(String[] args) {

        Map<String,Integer> map1 = new HashMap<>();
        map1.put("key",1);
        map1.put("key1",2);

        Map<String,Integer> map2 = new HashMap<>();
        map2.put("key",1);
        map2.put("key2",2);

        //去相同集合的数据

        System.out.println("----------------两个map的交集---------------------------");
        MapDifference<String,Integer> difference = Maps.difference(map1,map2);
        Map<String,Integer>  commonMap = difference.entriesInCommon();

        Set<Map.Entry<String,Integer>> entries =   commonMap.entrySet();
        Iterator<Map.Entry<String,Integer>> iterator =  entries.iterator();
        while(iterator.hasNext()){
            Map.Entry<String,Integer>  entry = iterator.next();
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        }

        System.out.println("------------------左面的map中含有的-------------------------");
        Map<String,Integer>  leftMap  = difference.entriesOnlyOnRight();
        Set<Map.Entry<String,Integer>>  leftSet =leftMap.entrySet();
        Iterator<Map.Entry<String,Integer>> iterator1 =  leftSet.iterator();
        while (iterator1.hasNext()){
            Map.Entry<String,Integer>  entry = iterator1.next();
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        }


        System.out.println("------------------右面的map中含有的-------------------------");
        Map<String,Integer>  rightMap  = difference.entriesOnlyOnLeft();
        Set<Map.Entry<String,Integer>>  rightSet =rightMap.entrySet();
        Iterator<Map.Entry<String,Integer>> iterator2 =  leftSet.iterator();
        while (iterator2.hasNext()){
            Map.Entry<String,Integer>  entry = iterator2.next();
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        }


        Set<String> oneSet = Sets.newHashSet("chen","lei","java");
        Set<String> twoSet = Sets.newHashSet("chen","lei","hadoop");
        Sets.SetView<String> diffSetHandle=Sets.difference(oneSet, twoSet);//是得到左边中不同或者特有的元素，若无，则返回长度为0的集合
        Iterator iter=diffSetHandle.iterator();
        while(iter.hasNext()){
            System.out.println("Set的不同元素："+iter.next().toString());
        }

        System.out.println("------------- Set中相同的元素：-----------------------");
        Sets.SetView<String> commonSet=Sets.intersection(oneSet, twoSet);
        Set<String> commonImmutable=commonSet.immutableCopy();//返回一个不可变的2个Set中共同元素集合的Set拷贝
        Iterator<String> iter2 = commonImmutable.iterator();
        while(iter2.hasNext()){
            String strValue = iter2.next();
            System.out.println(strValue);
        }


    }
}
