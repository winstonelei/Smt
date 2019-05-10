package com.github.fileStorage;

import org.junit.Test;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author wangl
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class BucketTest {

    @Test
    public void  writeFileTest(){
        try {
            AtomicInteger serial = new AtomicInteger();
            LocalStringBucket bucket = new LocalStringBucketManager();
            bucket.initialize("cat", new Date(), 0);
            for (int i = 0; i < 100; i++) {
                try {
                    String id = "" + serial.incrementAndGet();
                    String value = "value:" + id;
                    boolean success = bucket.storeById(id, value);
                } catch (Throwable e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void readFileTest() throws Exception {
        LocalStringBucket bucket = new LocalStringBucketManager();
        bucket.initialize("cat", new Date(), 0);
        AtomicInteger serial = new AtomicInteger();
        for (int i = 0; i < 100; i++) {
            String id = null;
            try {
                id = "" + serial.incrementAndGet();
                String target = bucket.findById(id);
                System.out.println(target);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

    }
}
