package com.github.nettyBuffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;
import java.util.Iterator;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author winstone
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class NettyBufferTest {

    @Test
    public void test1(){
        ByteBuf buffer = Unpooled.buffer(10);

        for (int i = 0; i < 10; ++i) {
            buffer.writeByte(i);
        }

//        for (int i = 0; i < buffer.capacity(); ++i) {
//            System.out.println(buffer.getByte(i));
//        }

         //netty buffer 的get方法获取数据，索引不会改变
        for(int i = 0; i < buffer.capacity(); ++i) {
            System.out.println(buffer.getByte(i));
        }

        //netty buffer 的get方法获取数据，索引会改变
        for(int i = 0; i < buffer.capacity(); ++i) {
            System.out.println(buffer.readByte());
        }
    }

    @Test
    public void test2(){
        ByteBuf byteBuf = Unpooled.copiedBuffer("张hello world", Charset.forName("utf-8"));

        //bytebuf 有读索引和写索引，一个中文占用三个字节
        if (byteBuf.hasArray()) {
            byte[] content = byteBuf.array();
            System.out.println(new String(content, Charset.forName("utf-8")));

            System.out.println(byteBuf);

            System.out.println(byteBuf.arrayOffset());
            System.out.println(byteBuf.readerIndex());
            System.out.println(byteBuf.writerIndex());
            System.out.println(byteBuf.capacity());

            int length = byteBuf.readableBytes();

            System.out.println(length);

            //从bytebuf的可读索引中读取字节数据
            for(int i = 0; i < byteBuf.readableBytes(); ++i) {
                System.out.println((char)byteBuf.getByte(i));
            }

            //读取字节缓冲区中索引从0到4的索引数据
            System.out.println(byteBuf.getCharSequence(0, 4, Charset.forName("utf-8")));
            //读取字节缓冲区中索引从4到6的索引数据
            System.out.println(byteBuf.getCharSequence(4, 6, Charset.forName("utf-8")));
        }

    }




    @Test
    public void test3(){
        CompositeByteBuf compositeByteBuf = Unpooled.compositeBuffer();

        ByteBuf heapBuf = Unpooled.buffer(10);
        ByteBuf directBuf = Unpooled.directBuffer(8);

        compositeByteBuf.addComponents(heapBuf, directBuf);
//        compositeByteBuf.removeComponent(0);

        Iterator<ByteBuf> iter = compositeByteBuf.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next());
        }

        compositeByteBuf.forEach(System.out::println);
    }


}
