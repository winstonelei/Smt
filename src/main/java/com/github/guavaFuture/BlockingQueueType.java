package com.github.guavaFuture;

/**
 * Created by winstone on 2017/6/20.
 */
public enum BlockingQueueType {
    LINKED_BLOCKING_QUEUE("LinkedBlockingQueue"),
    ARRAY_BLOCKING_QUEUE("ArrayBlockingQueue"),
    SYNCHROUNS_QUEUE("SynchronousQueue");

    private String value;

    private BlockingQueueType(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }

    public static BlockingQueueType fromString(String value){
        for(BlockingQueueType type : BlockingQueueType.values()){
            if(type.getValue().equalsIgnoreCase(value)){
                return type;
            }
        }
        throw new IllegalArgumentException("Mismatched type with value=" + value);
    }


    public String toString(){return  value;}

}
