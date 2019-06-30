/*
 * Copyright (c) 2011-2018, Meituan Dianping. All Rights Reserved.
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.messageFileStorage;

import junit.framework.Assert;
import org.junit.Test;
import org.xerial.snappy.SnappyOutputStream;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MessageBlockTest {

	private String baseDir = "target/bucket/hdfs/dump/test";

	@Test
	public void testReadAndWrite() throws IOException {
		File dataFile = new File(baseDir);

		dataFile.delete();

		MessageBlockWriter write = new MessageBlockWriter(dataFile);
		MessageBlockReader reader = new MessageBlockReader(dataFile);

		String data1 = "This is test data1";

		ByteArrayOutputStream baos = new ByteArrayOutputStream(4);
		DataOutputStream out = new DataOutputStream(new SnappyOutputStream(baos));

		out.writeInt(data1.getBytes().length);
		out.writeBytes(data1);
		out.close();

		byte[] results = baos.toByteArray();

		MessageBlock block = new MessageBlock(baseDir);
		block.setData(results);
		block.addIndex(1, 0);

		write.writeBlock(block);

		String data2 =  new String(reader.readMessage(1));
		Assert.assertEquals(data1, new String(reader.readMessage(1)));
	}

	@Test
	public void testBufferOrder() throws IOException {
		System.out.println("当前系统order="+ByteOrder.nativeOrder());
		ByteBuffer buffer = ByteBuffer.allocate(20);

// 获取默认的byte顺序
		ByteOrder order = buffer.order(); //
		System.out.println("当前order="+order);

		buffer.putShort(0, (short)1);
		buffer.get(0);
		System.out.println("此时取出0:"+buffer.get(0));
		buffer.get(1);
		System.out.println("此时取出1:"+buffer.get(1));

		buffer.order(ByteOrder.LITTLE_ENDIAN);
		System.out.println("当前order="+buffer.order());

		buffer.putShort(0, (short)1);
		buffer.get(0);
		System.out.println("此时取出1:"+buffer.get(0));
		buffer.get(1);
		System.out.println("此时取出0:"+buffer.get(1));


	}

}
