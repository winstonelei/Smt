package com.github.thread.masterworker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Tasks implements Serializable{

	private int id;
	private List<Task> tasks = new ArrayList<>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
}
