package com.github.monitor;


/**
 * Monitor watches a specific event and executes the specified actions when the
 * event is triggered
 */
public interface Monitor<T> extends Runnable {

    void init() throws Exception;

    void destroy();

    void addAction(String actionName, Action<? super T> action);

}
