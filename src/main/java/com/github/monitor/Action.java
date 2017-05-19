package com.github.monitor;

/**
 * Action is the abstraction of doable things in Diviner
 * 
 * @author h00293766
 * 
 */
public interface Action<T> {

    void execute(T obj);

}
