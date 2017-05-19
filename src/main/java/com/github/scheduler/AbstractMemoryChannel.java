package com.github.scheduler;

import java.util.concurrent.TimeUnit;

/**
 * Created by winstone on 2016/12/6.
 */
abstract class AbstractMemoryChannel implements Channel{

    protected TimeUnit timeUnit ;
    protected long expire ;

    public AbstractMemoryChannel(){
        timeUnit=TimeUnit.SECONDS;
        expire=10;
    }
}