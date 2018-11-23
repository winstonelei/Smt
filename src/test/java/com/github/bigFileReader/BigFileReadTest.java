package com.github.bigFileReader;

import com.github.multithreadRead.BigFileReader;
import com.github.multithreadRead.IHandle;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author 17090718
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class BigFileReadTest {

    public static void main(String[] args) {
        BigFileReader.Builder builder = new BigFileReader.Builder("D:\\tmp\\reliability.txt", new IHandle() {
            @Override
            public void handle(String line) {
                System.out.println(line);
            }
        });
        builder.withTreahdSize(10)
                .withCharset("gbk")
                .withBufferSize(1024 * 1024);
        BigFileReader bigFileReader = builder.build();
        bigFileReader.start();

    }
}
