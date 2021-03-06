package com.github.multiQuery;

import sun.nio.ch.ThreadPool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉多线程查询的基类 P=queryOnce方法的参数， R=queryOnce的返回结果
 */
public abstract class MultiThreadQuerySupport<P,R> {


    private int maxThreadsPeryQuery = 15;

    private int queryTimeOut = 10000;

    //参数初始化
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    //核心线程数量大小
    private static final int corePoolSize = Math.max(2, Math.min(CPU_COUNT - 1, 4));
    //线程池最大容纳线程数
    private static final int maximumPoolSize = CPU_COUNT * 2 + 1;
    //线程空闲后的存活时长
    private static final int keepAliveTime = 30;

/*
    private ThreadPoolExecutor pool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime,
            TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
*/

    private ThreadPoolExecutor pool = new ThreadPoolExecutor(maxThreadsPeryQuery, 50, keepAliveTime,
            TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

    private ThreadPoolExecutor createExecutor() {
        return pool;
    }


    public List<R> query(List<P> parms){
        CompletionService<R> completionService= new ExecutorCompletionService<R>(createExecutor());

        //批量提交
        for(P p : parms){
            completionService.submit(new Caller(p));
        }

        List<R> resultList = new ArrayList<>(parms.size());

        for(int i=0;i<parms.size();i++){
            R r = null;
            try {
                Future<R> f = completionService.poll(queryTimeOut,TimeUnit.MILLISECONDS);
                if(null != f){
                    r = f.get();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (null != r) {
                resultList.add(r);
            }
        }
        return resultList;
    }

    public int getQueryTimeout() {
        return queryTimeOut;
    }

    public void setQueryTimeout(int queryTimeOut) {
        this.queryTimeOut = queryTimeOut;
    }

    public int getMaxThreadsPerQuery() {
        return maxThreadsPeryQuery;
    }

    public void setMaxThreadsPerQuery(int maxThreadsPerQuery) {
        this.maxThreadsPeryQuery = maxThreadsPerQuery;
    }

    //查询方法
    protected  abstract R queryOne(P p);


    //真正执行查询的线程
   private class Caller implements  Callable<R>{

      private P p;

      public Caller(P p){
          this.p = p;
      }
      @Override
      public R call() {
          return queryOne(p);
      }
  }


    public void shutdown(){
      pool.shutdown();
    }

}
