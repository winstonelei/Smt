package com.github.thread.masterworker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

	public static void main(String[] args) throws Exception{

		Master master = new Master(new Worker(), 10);
		master.execute();
	//	Thread.sleep(500);
		long start = System.currentTimeMillis();
		Random r = new Random();
        List<Task> tasks = new ArrayList<>();
		File file = new File("F:\\var\\log\\a.txt");
		BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
		String s = null;
		AtomicInteger ai = new AtomicInteger();
		Tasks tasks1 = new Tasks();
		StringBuilder sb = new StringBuilder(500000);
		while((s = br.readLine())!=null){//使用readLine方法，一次读一行
			ai.incrementAndGet();
			Task task = new Task();
			task.setId(ai.get());
			task.setMessage(s);
			tasks1.getTasks().add(task);
			if(ai.get() % 10000 ==0){
				master.submitLists(tasks1);
				System.out.println("正常提交"+ai.get());
				System.out.println("list大小"+ tasks1.getTasks().size());
			//	tasks1.getTasks().clear();
				tasks1.setTasks(new ArrayList<>());
			}
		}

		if(tasks1.getTasks().size()>=1){
			master.submitLists(tasks1);
			System.out.println("异常提交"+ai.get());
			System.out.println("list大小"+ tasks1.getTasks().size());
		}
		/*for(int i = 1; i <= 100; i++){
			Task t = new Task();
			t.setId(i);
			t.setPrice(r.nextInt(1000));
			t.setMessage("msg"+i);
			master.submit(t);
		}*/
		while(true){
			if(master.isComplete()){
				long end = System.currentTimeMillis() - start;
			//	int priceResult = master.getResult();
				//System.out.println("最终结果：" + priceResult + ", 执行时间：" + end);
				System.out.println("执行时间：" + end);
				break;
			}
		}
		
	}
}
