package com.github.java8Completable;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author 17090718
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 *
 */
public class CompletableFutureTest {

    //将两个CompletableFuture建立联系,通常，我们会有多个需要独立运行但又有所依赖的的任务。比如先等用于的订单处理完毕然后才发送邮件通知客户。
    @Test
    public void test1(){
        CompletableFuture<String> completableFuture1= CompletableFuture.supplyAsync(()->{
            //模拟执行耗时任务
            System.out.println("task1 doing...");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //返回结果
            return "result1";
        });

        //等第一个任务完成后，将任务结果传给参数result，执行后面的任务并返回一个代表任务的completableFuture
        CompletableFuture<String> completableFuture2= completableFuture1.thenCompose(result->CompletableFuture.supplyAsync(()->{
            //模拟执行耗时任务
            System.out.println("task2 doing...");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //返回结果
            System.out.println("第一个返回结果"+result);
            return "result2";
        }));

        try {
            System.out.println(completableFuture2.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    //错误处理,为了能获取任务线程内发生的异常，你需要使用
    //CompletableFuture 的completeExceptionally方法将导致CompletableFuture 内发生问题的异常抛出。
    @Test
    public void test2(){
        CompletableFuture<String> completableFuture=new CompletableFuture();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //模拟执行耗时任务
                    System.out.println("task doing...");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    throw new RuntimeException("抛异常了");
                }catch (Exception e) {
                    //告诉completableFuture任务发生异常了
                    completableFuture.completeExceptionally(e);
                }
            }
        }).start();
        //获取任务结果，如果没有完成会一直阻塞等待
        String result= null;
        try {
            result = completableFuture.get();
            System.out.println("计算结果:"+result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //构建等待全部完成任务和部分执行任务
    @Test
    public  void test3(){
        CompletableFuture<String> completableFuture1 = CompletableFuture.supplyAsync(()->{
            System.out.println("task 1 doing...");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "result1";
        });

        CompletableFuture<String> completableFuture2 =   CompletableFuture.supplyAsync(()->{
            //模拟执行耗时任务
            System.out.println("task2 doing...");
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //返回结果
            return "result2";
        });

        CompletableFuture<Object> anyResult = CompletableFuture.anyOf(completableFuture1,completableFuture2);

        try {
            System.out.println("首先完成的任务的结果=" + anyResult.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

       CompletableFuture<Void> allResult = CompletableFuture.allOf(completableFuture1,completableFuture2);
       //阻塞等待任务完成

        allResult.join();
        System.out.println("所有任务完成的结果");

    }

    /*
      你需要将两个完
      全不相干的 CompletableFuture 对象的结果整合起来，而且你也不希望等到第一个任务完全结
      束才开始第二项任务。
      */
    @Test
    public  void test4(){
        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            //模拟执行耗时任务
            System.out.println("task1 doing...");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //返回结果
            return 100;
        });

        //将第一个任务与第二个任务组合一起执行，都执行完成后，将两个任务的结果合并
        CompletableFuture<Integer> completableFuture2 = completableFuture1.thenCombine(
                //第二个任务
                CompletableFuture.supplyAsync(() -> {
                    //模拟执行耗时任务
                    System.out.println("task2 doing...");
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //返回结果
                    return 2000;
                }),
                //合并函数
                (result1, result2) -> result1 + result2);

        try {
            System.out.println(completableFuture2.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    //响应 CompletableFuture 的 completion 事件,CompletableFuture 通过 thenAccept 方法提供了这一功能，它接收
    //CompletableFuture 执行完毕后的返回值做参数。
    @Test
    public  void test5(){
        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            //模拟执行耗时任务
            System.out.println("task1 doing...");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //返回结果
            return 100;
        });

        //注册完成事件
        completableFuture1.thenAccept(result->System.out.println("task1 done,result:"+result));

        CompletableFuture<Integer> completableFuture2=
                //第二个任务
                CompletableFuture.supplyAsync(() -> {
                    //模拟执行耗时任务
                    System.out.println("task2 doing...");
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //返回结果
                    return 2000;
                });

        //注册完成事件
        completableFuture2.thenAccept(result->System.out.println("task2 done,result:"+result));

        //将第一个任务与第二个任务组合一起执行，都执行完成后，将两个任务的结果合并
        CompletableFuture<Integer> completableFuture3 = completableFuture1.thenCombine(completableFuture2,
                //合并函数
                (result1, result2) -> {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return result1 + result2;
                });

        try {
            System.out.println(completableFuture3.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    // 全流式处理转换成CompletableFuture[]+组装成一个无返回值CompletableFuture，join等待执行完毕。返回结果whenComplete获取
    @Test
    public void  test6(){
        Long start = System.currentTimeMillis();
        // 结果集
        List<String> list = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<Integer> taskList = Arrays.asList(2, 1, 3, 4, 5, 6, 7, 8, 9, 10);
        // 全流式处理转换成CompletableFuture[]+组装成一个无返回值CompletableFuture，join等待执行完毕。返回结果whenComplete获取
        CompletableFuture[] cfs = taskList.stream()
                .map(integer -> CompletableFuture.supplyAsync(() -> calc(integer), executorService)
                        .thenApply(h->Integer.toString(h))
                        .whenComplete((s, e) -> {
                            try {
                                System.out.println("任务"+s+"完成!result="+s+"，异常 e="+e+","+new Date());
                                list.add(s);
                            }catch (Exception e1){
                                System.out.println(e1);
                            }
                        })
                ).toArray(CompletableFuture[]::new);
        // 封装后无返回值，必须自己whenComplete()获取
        CompletableFuture.allOf(cfs).join();
        System.out.println("list="+list+",耗时="+(System.currentTimeMillis()-start));
    }

    public static Integer calc(Integer i) {
        try {
            if (i == 1) {
                Thread.sleep(3000);//任务1耗时3秒
            } else if (i == 5) {
                Thread.sleep(5000);//任务5耗时5秒
                throw new NumberFormatException("抛异常了");
            } else {
                Thread.sleep(1000);//其它任务耗时1秒
            }
            /*System.out.println("task线程：" + Thread.currentThread().getName()
                    + "任务i=" + i + ",完成！+" + new Date());*/
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        return i;
    }

}
