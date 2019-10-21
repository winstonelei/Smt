package com.github.batch;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *  批量处理工具类
 * @author winstone
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class BatchUtils {


    /**
     * 批量处理切分的时候，返回第index个List
     */
    private static <E> List<E> getBatchList(Integer index, int batchSize, Collection<E> collection) {
        List<E> list = null;
        if (collection instanceof List) {
            list = (List<E>) collection;
        } else {
            list = new ArrayList<E>(collection);
        }

        if (index == list.size() / batchSize) {
            return list.subList(index * batchSize, list.size());
        } else {
            return list.subList(index * batchSize, (index + 1) * batchSize);
        }
    }

    public static <E> void batchExecute(int totalSize, int batchSize, Collection<E> collection, Executor<E> executor) {

        for (int i = 0; i <= totalSize / batchSize; i++) {
            List<E> list = BatchUtils.getBatchList(i, batchSize, collection);

            if (CollectionUtils.isNotEmpty(list)) {
                if (!executor.execute(list)) {
                    break;
                }
            }
        }
    }

    public interface Executor<E> {

        // 返回是否需要继续
        boolean execute(List<E> list);
    }

    /**
     * 按指定大小，分隔集合，将集合按规定个数分为多个部分
     *
     * @param source
     * @param len：集合的长度
     * @return
     */
    public static <T>  List<List<T>> subWithLen(List<T> source, int len) {
        if (source == null || source.size() == 0 || len < 1) {
            return null;
        }

        List<List<T>> result = new ArrayList<List<T>>();
        int count = (source.size() + len - 1) / len;
        for (int i = 0; i < count; i++) {
            List<T> value = null;
            if ((i + 1) * len < source.size()) {
                value = source.subList(i * len, (i + 1) * len);
            } else {
                value = source.subList(i * len, source.size());
            }
            result.add(value);
        }
        return result;
    }



    /**
     * 将一个list均分成n个list
     *
     * @param source
     * @param n : 小集合的个数
     * @return
     */
    public static  <T> List<List<T>> subWithNum(List<T> source, int n) {
        List<List<T>> result = new ArrayList<List<T>>();
        int remaider = source.size() % n; // (先计算出余数)
        int number = source.size() / n; // 然后是商
        int offset = 0;// 偏移量
        for (int i = 0; i < n; i++) {
            List<T> value = null;
            if (remaider > 0) {
                value = source.subList(i * number + offset, (i + 1) * number + offset + 1);
                remaider--;
                offset++;
            } else {
                value = source.subList(i * number + offset, (i + 1) * number + offset);
            }
            result.add(value);
        }
        return result;
    }

    public static void main(String[] args) {

        List<String> source = new ArrayList<>();
        for(int i=0;i<1000;i++){
            source.add("abcc");
        }
        List<List<String>> subList = subWithNum(source,10);
        for(int i=0;i<subList.size();i++){
            System.out.println(subList.get(i));
        }

    }
}
