package com.github.thread;

public class ConnThreadLocal {

	public static ThreadLocal<String> th = new ThreadLocal<String>();

	public void setTh(String value){
		th.set(value);
	}
	public void getTh(){
		System.out.println(Thread.currentThread().getName() + ":" + this.th.get());
	}
	
	public static void main(String[] args) throws InterruptedException {

		final String requestMessage ="sayHello";

		final ConnThreadLocal ct = new ConnThreadLocal();
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				ct.setTh("张三");
				ct.getTh();
				System.out.println(Thread.currentThread().getName() + ":" + requestMessage);
			}
		}, "t1");


		Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					ct.getTh();
					System.out.println(Thread.currentThread().getName() + ":" + requestMessage);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}, "t2");
		
		t1.start();
		t2.start();
	}
	
}
