package com.github.thread.masterworker;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Worker implements Runnable ,Closeable{

	//private ConcurrentLinkedQueue<Task> workQueue;
	private BlockingQueue<Task> workQueue;

	public BlockingQueue<Object> getWorkQueues() {
		return workQueues;
	}

	public void setWorkQueues(BlockingQueue<Object> workQueues) {
		this.workQueues = workQueues;
	}

	private BlockingQueue<Object> workQueues;

	private ConcurrentHashMap<String, Object> resultMap;

	private volatile  boolean flag = true;

/*	public void setWorkQueue(ConcurrentLinkedQueue<Task> workQueue) {
		this.workQueue = workQueue;
	}*/

   public void setWorkQueue(BlockingQueue workQueue){
   	this.workQueue = workQueue;
   }

	public void setResultMap(ConcurrentHashMap<String, Object> resultMap) {
		this.resultMap = resultMap;
	}


	@Override
	public void run() {
		while(flag){
			try{
	/*			Task input = this.workQueue.poll(1000,TimeUnit.MILLISECONDS);
				if(input == null) break;
				Object output = handle(input);
				this.resultMap.put(Integer.toString(input.getId()), output);*/

			   Object obj = this.workQueues.poll(1000, TimeUnit.MILLISECONDS);
			   if(obj == null) break;
			   if(obj != null){
					handleList(obj);
				}
			/*	List<Task> list = this.workQueues.take();
			   */
			}catch (Exception e){
				e.printStackTrace();
			}
		}
	}

	private Object handle(Task input) {
		Object output = null;
		try {
			//处理任务的耗时。。 比如说进行操作数据库。。。
		//	Thread.sleep(500);
			output = input.getPrice();
			AtomicInteger ai = new AtomicInteger();
			ai.incrementAndGet();
			List<Task> list = new ArrayList<>();
			list.add(input);
			//if(ai.get() % 3000 == 0){
			System.out.println(""+input.getMessage());
			//}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output;
	}

	private Object handleList(Object inputs) {
		Object output = null;
		try {
			//处理任务的耗时。。 比如说进行操作数据库。。。
			//	Thread.sleep(500);
			Tasks tasks = (Tasks)inputs;
			System.out.println("获取到的结果大小 ="+tasks.getTasks().size());

	/*		if(tasks.getTasks().size()>0){
				for(Task task :tasks.getTasks()){
					if(null != task){
						this.resultMap.put(Integer.toString(task.getId()), task);
						System.out.println(task.getMessage());
					}
				}
				*//*tasks.getTasks().stream().forEach(task -> {
					this.resultMap.put(Integer.toString(task.getId()), task);
				});*//*
			}*/

	/*		AtomicInteger ai = new AtomicInteger();
			ai.incrementAndGet();
			List<Task> list = new ArrayList<>();
			list.add(input);
			//if(ai.get() % 3000 == 0){
			System.out.println(""+input.getMessage());
			//}*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output;
	}

	@Override
	public void close() throws IOException {
		flag = false;
	}
}
