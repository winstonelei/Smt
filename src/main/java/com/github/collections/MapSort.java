package com.github.collections;

import java.util.*;
import java.util.Map.Entry;

/**
 * 对map进行排序的工具
 * @version 2017年7月28日
 */
public class MapSort
{
    /**
     * 对某个map进行排序<br>
     * 既可以对键排序，也可以对值排序，全在于你写的entryComparator对象
     * @author nan.li
     * @param map  待排序的map
     * @param entryComparator   排序器
     * @return  排序完返回的map
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Map sort(Map map, Comparator entryComparator)
    {
        List<Entry> arrayList = new ArrayList<>(map.entrySet());
        //对列表排序
        Collections.sort(arrayList, entryComparator);

        //有序的map
        Map sortedMap = new LinkedHashMap<>();
        for (Entry entry : arrayList)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    public static void main(String[] args) {

        Map<String,Integer> map = new HashMap<>();
        map.put("zs",1);
        map.put("lisi",5);
        map.put("zssss",5);
        map.put("lisi2",50);
        map.put("sss",2);
        Map<String,Integer> resultMap = sort(map, new Comparator<Entry<String,Integer>>() {
            @Override
            public int compare(Entry<String,Integer> entry1, Entry<String,Integer> entry2) {
                return entry1.getValue().compareTo(entry2.getValue()) ;
            }
        });
        System.out.println(resultMap);

     }


}
