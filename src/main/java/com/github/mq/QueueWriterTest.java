package com.github.mq;

import java.io.File;

public class QueueWriterTest {
	
	public static void main(String[] args) throws Exception { 
		Index index = new Index(new File("D:/tmp/MyMQ"));

		QueueWriter w = new QueueWriter(index);
		for(int i=0; i<100;i++){
			DiskMessage message = new DiskMessage();
			message.body = new String("hello"+i).getBytes();
			w.write(message);
		}


		QueueReader r = new QueueReader(index, "hab");
		while(true){
			DiskMessage data = r.read();
			if(data == null) break;
			System.out.println(new String(data.body));
		}
		w.close();
		r.close();
		index.close();
	}
	
}
