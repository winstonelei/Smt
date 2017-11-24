package com.github.thread.masterworker;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Worker implements Runnable {

	private ConcurrentLinkedQueue<Task> workQueue;
	private ConcurrentHashMap<String, Object> resultMap;
	
	public void setWorkQueue(ConcurrentLinkedQueue<Task> workQueue) {
		this.workQueue = workQueue;
	}

	public void setResultMap(ConcurrentHashMap<String, Object> resultMap) {
		this.resultMap = resultMap;
	}
	
	@Override
	public void run() {
		while(true){
			Task input = this.workQueue.poll();
			if(input == null) break;
			Object output = handle(input);
			this.resultMap.put(Integer.toString(input.getId()), output);
		}
	}

	private Object handle(Task input) {
		Object output = null;
		try {
			//处理任务的耗时。。 比如说进行操作数据库。。。
		//	Thread.sleep(500);
			output = input.getPrice();
			System.out.println(input.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output;
	}



}
