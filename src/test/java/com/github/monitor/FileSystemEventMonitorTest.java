package com.github.monitor;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileSystemEventMonitorTest {

	private static final String MONITOR_DIR = "monitor-dir";
	  
	public static void main(String[] args) {

	   URL pathString = Thread.currentThread().getContextClassLoader().getResource("F:\\ideaworkspace\\MyJavaUtil\\target\\classes");

		Path path = null;
		try {
			path = Paths.get("F:\\ideaworkspace\\MyJavaUtil\\target\\classes");
		} catch (Exception e) {
			e.printStackTrace();
		}

		FileSystemEventMonitor monitor = new FileSystemEventMonitor(new Path[] { path }, null, false);
		try {
			monitor.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
		monitor.addAction("modifyWatch", new Action<File>() {
			@Override
			public void execute(File obj) {
				try {
					System.out.println(obj.getCanonicalPath() + " modified");
					//MyClassLoader.loadClass();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		Thread monitorThread = new Thread(monitor);

		monitorThread.start();

	/*    monitorThread.interrupt();

		try {
			monitorThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
*/		
	}

}
