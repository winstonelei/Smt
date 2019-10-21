package com.github.bitmap;

import org.junit.Test;
import org.roaringbitmap.RoaringBitmap;

import java.util.function.Consumer;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author winstone
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class BitMapTest {

    @Test
    public void testAdd(){
     //向rr中添加1、2、3、1000四个数字
        RoaringBitmap rr = RoaringBitmap.bitmapOf(1,2,3,1000);
        //创建RoaringBitmap rr2
        RoaringBitmap rr2 = new RoaringBitmap();
        //向rr2中添加10000-12000共2000个数字
        rr2.add(10000L,12000L);
        //返回第3个数字是1000，第0个数字是1，第1个数字是2，则第3个数字是1000
        System.out.println(rr.select(3));
        //返回value = 2 时的索引为 1。value = 1 时，索引是 0 ，value=3的索引为2
        rr.rank(2);
        //判断是否包含1000
        rr.contains(1000); // will return true
        //判断是否包含7
        rr.contains(7); // will return false

        RoaringBitmap andMap = RoaringBitmap.and(rr,rr2);
        //两个RoaringBitmap进行or操作，数值进行合并，合并后产生新的RoaringBitmap叫rror
        RoaringBitmap rror = RoaringBitmap.or(rr, rr2);
        //rr与rr2进行位运算，并将值赋值给rr
        rr.or(rr2);
        //判断rror与rr是否相等，显然是相等的
        boolean equals = rror.equals(rr);
        if(!equals) throw new RuntimeException("bug");
        // 查看rr中存储了多少个值，1,2,3,1000和10000-12000，共2004个数字
        long cardinality = rr.getLongCardinality();
        System.out.println(cardinality);
        //遍历rr中的value
        for(int i : rr) {
            System.out.println(i);
        }
        //这种方式的遍历比上面的方式更快
        rr.forEach((Consumer<? super Integer>) i -> {
            System.out.println(i.intValue());
        });


    }
}
