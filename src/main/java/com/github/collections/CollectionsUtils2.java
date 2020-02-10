package com.github.collections;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import static com.github.collections.StringUtils2.COMMA;

/**
 * 集合工具类.
 */
public class CollectionsUtils2 {

    /**
     * 判定集合是否为空.如果集合为null或isEmpty(),则返回true,否则返回false.
     *
     * @param coll
     * @return
     */
    public static boolean isEmpty(Collection<? extends Object> coll) {
        if (coll == null || coll.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判定Map是否为空.如Map为null或size()==0,则返回true,否则返回false.
     *
     * @param map
     * @return
     */
    public static boolean isEmpty(Map<? extends Object, ? extends Object> map) {
        if (map == null || map.entrySet().size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 将集合转化为以逗号相隔的字符串,如: "banana,apple,pear"
     *
     * @param coll
     * @return
     */
    public static String toString(Collection<? extends Object> coll) {
        return toString(coll, COMMA);
    }

    /**
     * 将数组转化为逗号相隔的字符串,如: "banana,apple,pear"
     *
     * @param array
     * @return
     */
    public static String toString(Object[] array) {
        return toString(Arrays.asList(array));
    }

    /**
     * 将Map转为为字符串,如: name=zhangke,age=20,address=hangzhou.
     *
     * @param map
     * @return
     */
    public static String toString(Map<? extends Object, ? extends Object> map) {
        if (isEmpty(map)) {
            return "";
        }

        StringBuffer buffer = new StringBuffer("");
        map.entrySet().forEach(entry -> {
            buffer.append(entry.getKey()).append("=").append(entry.getValue()).append(",");
        });
        buffer.deleteCharAt(buffer.length() - 1).append("");

        return buffer.toString();
    }

    /**
     * 将集合转化为由separator指定的分隔符分隔的字符串
     *
     * @param coll
     * @param separator
     * @return
     */
    public static String toString(Collection<? extends Object> coll, String separator) {
        if (isEmpty(coll)) {
            return "";
        }

        StringBuffer buffer = new StringBuffer("");
        coll.forEach(item -> {
            buffer.append(item.toString()).append(separator);
        });
        buffer.deleteCharAt(buffer.length() - 1);

        return buffer.toString();
    }

    /**
     * 将数组转化为由separator指定的分隔符分隔的字符串
     *
     * @param array
     * @param separator
     * @return
     */
    public static String toString(Object[] array, String separator) {
        return toString(Arrays.asList(array), separator);
    }

}
