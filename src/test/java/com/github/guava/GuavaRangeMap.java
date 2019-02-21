package com.github.guava;

import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;
import org.junit.Test;

import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author 17090718
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class GuavaRangeMap {

    @Test
    public void testRangeMap(){
        RangeMap<Integer, String> test = TreeRangeMap.create();
        test.put(Range.closed(1, 2), "winstone");
        test.put(Range.closed(10, 12), "Charlotte");

        for(Map.Entry<Range<Integer>,String> entry : test.asMapOfRanges().entrySet()){
            System.out.println(entry.getKey()+"\t"+entry.getValue());
        }

        System.out.println(test.get(2));
        System.out.println(test.get(3));
        System.out.println(test.get(10));

    }
}
