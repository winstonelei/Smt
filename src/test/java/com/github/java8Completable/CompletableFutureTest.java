package com.github.java8Completable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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

    public static void main(String[] args) {
        //将两个CompletableFuture建立联系,通常，我们会有多个需要独立运行但又有所依赖的的任务。比如先等用于的订单处理完毕然后才发送邮件通知客户。
        //test1();

        //错误处理,为了能获取任务线程内发生的异常，你需要使用
        //CompletableFuture 的completeExceptionally方法将导致CompletableFuture 内发生问题的异常抛出。
       //test2();

        //构建等待全部完成任务和部分执行任务
        //test3();

        /*
        你需要将两个完
        全不相干的 CompletableFuture 对象的结果整合起来，而且你也不希望等到第一个任务完全结
        束才开始第二项任务。
        */
        //test4();


        //响应 CompletableFuture 的 completion 事件,CompletableFuture 通过 thenAccept 方法提供了这一功能，它接收
        //CompletableFuture 执行完毕后的返回值做参数。
        test5();


    }


    public static void test1(){
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

    public static void test2(){
        CompletableFuture<String> completableFuture=new CompletableFuture();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //模拟执行耗时任务
                    System.out.println("task doing...");
                    try {
                        Thread.sleep(3000);
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
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    public static void test3(){

        CompletableFuture<String> completableFuture1 =  CompletableFuture.supplyAsync(()->{
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


    public  static  void test4(){
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


    public static void test5(){
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
                        Thread.sleep(3000);
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

}
