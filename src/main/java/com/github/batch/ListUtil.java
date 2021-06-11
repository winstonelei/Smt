package com.github.batch;

import java.util.*;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author winstone
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class ListUtil<T> {

    private static  ListUtil instance = new ListUtil();

    public static ListUtil getInstance(){
        return instance;
    }


    public List<T> removeAll(List<T> source, List<T> destination) {
        List<T> result = new LinkedList<T>();
        Set<T> destinationSet = new HashSet<T>(destination);
        for(T t : source) {
            if (!destinationSet.contains(t)) {
                result.add(t);
            }
        }
        return result;
    }

    //切分原始集合数据
    public <T> List<List<T>> splitSourceFiles(final List<T> sourceList, int adviceNumber) {
        List<List<T>> splitedList = new ArrayList<List<T>>();
        int averageLength = sourceList.size() / adviceNumber;
        averageLength = averageLength == 0 ? 1 : averageLength;

        for (int begin = 0, end = 0; begin < sourceList.size(); begin = end) {
            end = begin + averageLength;
            if (end > sourceList.size()) {
                end = sourceList.size();
            }
            splitedList.add(sourceList.subList(begin, end));
        }
        return splitedList;
    }

    public static void main(String[] args) {
        ListUtil<String> listUtil = new ListUtil<>();
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        list.add("e");
        list.add("f");
        list.add("e");

        List<List<String>> resultList =  listUtil.splitSourceFiles(list,2);
        System.out.println(resultList);



    }
}
