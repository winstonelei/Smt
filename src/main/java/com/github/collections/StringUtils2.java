package com.github.collections;

/**
 */
public class StringUtils2 {

    public static final String COMMA = ",";

    /**
     * 拼接字符串的简便方法
     *
     * @param items
     * @return
     */
    public static String append(Object... items) {
        StringBuffer buffer = new StringBuffer();
        for (Object item : items) {
            if (item != null) {
                buffer.append(item.toString());
            }
        }
        return buffer.toString();
    }
}
