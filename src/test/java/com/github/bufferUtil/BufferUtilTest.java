package com.github.bufferUtil;

import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteBuffer;

/**
 * BufferUtil单元测试
 *
 */
public class BufferUtilTest {

	@Test
	public void copyTest() {
		byte[] bytes = "AAABBB".getBytes();
		ByteBuffer buffer = ByteBuffer.wrap(bytes);

		ByteBuffer buffer2 = BufferUtil.copy(buffer, ByteBuffer.allocate(5));
		Assert.assertEquals("AAABB", StrUtil.utf8Str(buffer2));
	}

	@Test
	public void readBytesTest() {
		byte[] bytes = "AAABBB".getBytes();
		ByteBuffer buffer = ByteBuffer.wrap(bytes);

		byte[] bs = BufferUtil.readBytes(buffer, 5);
	    Assert.assertEquals("AAABB", StrUtil.utf8Str(bs));
	}

	@Test
	public void readBytes2Test() {
		byte[] bytes = "AAABBB".getBytes();
		ByteBuffer buffer = ByteBuffer.wrap(bytes);

		byte[] bs = BufferUtil.readBytes(buffer, 5);
		Assert.assertEquals("AAABB", StrUtil.utf8Str(bs));
		System.out.println(StrUtil.utf8Str(bs));
	}

	@Test
	public void readLineTest() {
		String text = "aa\r\nbbb\ncc";
		ByteBuffer buffer = ByteBuffer.wrap(text.getBytes());

		// 第一行
		String line = BufferUtil.readLine(buffer, CharsetUtil.CHARSET_UTF_8);
		Assert.assertEquals("aa", line);

		// 第二行
		line = BufferUtil.readLine(buffer, CharsetUtil.CHARSET_UTF_8);
		Assert.assertEquals("bbb", line);

		// 第三行因为没有行结束标志，因此返回null
		line = BufferUtil.readLine(buffer, CharsetUtil.CHARSET_UTF_8);
		Assert.assertNull(line);

		// 读取剩余部分
		Assert.assertEquals("cc", StrUtil.utf8Str(BufferUtil.readBytes(buffer)));
	}

	@Test
	public void readIntTest() {
		ByteBuffer buf = ByteBuffer.allocate(1024);
/*		buf.put((byte)123);
		buf.put((byte)2);
		buf.put((byte)3);
		buf.put((byte)4);*/
        buf.putInt(123);
		buf.flip();
/*		System.out.println(buf.get());
		System.out.println(buf.get());
		System.out.println(buf.get());
		System.out.println(buf.get());*/

		int temp = BufferUtil.parseInt(buf,4);
		System.out.println(temp);

/*		int temp2 = BufferUtil.parseInt(buf,1);
		System.out.println(temp2);
		int temp3 = BufferUtil.parseInt(buf,1);
		System.out.println(temp3);
		int temp4 = BufferUtil.parseInt(buf,1);
		System.out.println(temp4);*/
	}


	@Test
	public void readLongTest() {
		ByteBuffer buf = ByteBuffer.allocate(1024);
		buf.putLong(123L);

		buf.flip();
/*		System.out.println(buf.get());
		System.out.println(buf.get());
		System.out.println(buf.get());
		System.out.println(buf.get());*/

		Long temp = BufferUtil.parseLong(buf,8);
		System.out.println(temp);

	}

	@Test
	public void  byteTest(){
		ByteBuffer buf = ByteBuffer.allocate(1024);
		buf.putInt(12);
		buf.putLong(34);

		buf.flip();

		byte[] dst = new byte[buf.limit()];
		buf.get(dst);

		//通过字节获取int类型数据
		System.out.println(ByteUtil.getNumber(dst,0,4));

		//通过字节获取long类型数据
		System.out.println(ByteUtil.getNumber(dst,4,8));


	}

}
