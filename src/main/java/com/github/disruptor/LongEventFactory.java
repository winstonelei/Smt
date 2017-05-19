package com.github.disruptor;

import com.lmax.disruptor.EventFactory;
// 需要让disruptor为我们创建事件，我们同时还声明了一个EventFactory来实例化Event对象。
public class LongEventFactory implements EventFactory { 

    @Override 
    public Object newInstance() { 
        return new LongEvent(); 
    } 
}

/**
 *
 *  ./start-thriftserver.sh --master spark://spark135:7077
 *
 * !connect jdbc:hive2://spark135:10000
 *
 *
 export SPARK_HOME=/usr/local/spark-1.6.1
 export SPARK_CLASSPATH=$SPARK_HOME/lib/datanucleus-api-jdo-3.2.6.jar:$SPARK_HOME/lib/datanucleus-core-3.2.10.jar:$SPARK_HOME/lib/datanucleus-rdbms-3.2.9.jar:
 *
 */