package com.github.page;

import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author winstone
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class PageUtil {
    /**
     * 通用分页工具类
     *  内存分页工具类
     * @param data
     * @param pageSize
     * @param pageNum
     * @param <T>
     * @return
     */
    public static <T> Page<T> pagination(final List<T> data, final int pageSize, final int pageNum) {
        if (CollectionUtils.isEmpty(data)) {
            return new Page<>(1, 0, new ArrayList<>());
        }
        List<List<T>> lists = Lists.partition(data, pageSize);
        int localPageNum = pageNum;
        if (localPageNum >= lists.size()) {
            localPageNum = lists.size() - 1;
        }
        return new Page<>(localPageNum, data.size(), lists.get(localPageNum));
    }

    public static void main(String[] args) {
        List<String> tempList = new ArrayList<>();
        for(int i=0;i<100;i++){
            tempList.add("a"+i);
        }
        PageUtil pageUtil = new PageUtil();
        Page<String>  result = pageUtil.pagination(tempList,10,2);
        System.out.println(result);
    }
}
