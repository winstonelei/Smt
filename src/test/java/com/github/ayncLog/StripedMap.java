package com.github.ayncLog;

/**
 * 锁分段
 * @author wanglei
 *
 */
public class StripedMap {
    //同步策略：就是对数组进行分段上锁，n个节点用n%LOCKS锁保护
    private static final int N_LOCKS = 16;
    private final Node[] buckets;
    private final Object[] locks;
    
    private static class Node
    {
        private String name;
        private Node next;
        private String key;
        private String value;
        public String getValue() {
            return value;
        }
        public void setValue(String value) {
            this.value = value;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public Node getNext() {
            return next;
        }
        public void setNext(Node next) {
            this.next = next;
        }
        public String getKey() {
            return key;
        }
        public void setKey(String key) {
            this.key = key;
        }
        
    }
    
    public StripedMap(int numBuckets)
    {
        buckets = new Node[numBuckets];
        //创建对应hash的锁
        locks = new Object[N_LOCKS];
        for(int i = 0; i < N_LOCKS; ++ i)
        {
            locks[i] = new Object();
        }
    }
    
    private final int hash(Object key)
    {
        //取绝对值
        return Math.abs(key.hashCode() % buckets.length);
    }
    
    //get和clear
    public Object get(Object key)
    {
        int hash = hash(key);
        synchronized(locks[hash % N_LOCKS])
        {
            //分段上锁
            for(Node m = buckets[hash]; m != null; m = m.next)
            {
                if(m.key.equals(key))
                    return m.value;
            }
        }
        
        return null;
    }
    
    /**
     * 清除所有的数据，但是没有要求说要同时获取全部的锁的话，可以进行这样的释放操作
     */
    public void clear()
    {
        for(int i = 0; i < buckets.length; ++i)
        {
            synchronized(locks[i % N_LOCKS])
            {
                buckets[i] = null;
            }
        }
    }
}