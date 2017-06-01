package com.github.monitor;

/**
 * Action is the abstraction of doable things in Diviner
 * 
 * @author winstone
 * 
 */
public interface Action<T> {

    void execute(T obj);

}
