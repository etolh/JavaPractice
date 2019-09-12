package com.liah.concurrent;

import java.util.concurrent.*;

/**
 * Callable + Future模式
 * Executor线程池提交Callable任务，返回Future子类对象，由子类对象获取值
 * 缺点：只能由线程池提交任务，不能通过自己新建线程运行任务，Thread只能执行runnable任务
 *
 * Callable + FutureTask模式
 * FutureTask实现了RunnableFuture接口，RunnableFuture继承Runnable和Future接口
 * 因此FutureTask既可以作为Runnable任务，又可以作为Future得到Callable结果。
 *
 * FutureTask构造函数接收具体Callable任务对象，通过Thread或Executor提交上去后，直接通过get获取对象
 *
 * https://www.cnblogs.com/dolphin0520/p/3949310.html
 */
class SumTask implements Callable<Integer>{
    public Integer call(){
        System.out.println(Thread.currentThread().getName() + " is running");
        int s = 0;
        for (int i = 0 ; i < 10; i++){
            s += i;
        }
        return s;
    }
}

public class FutureTest {

    public static void main(String[] args){
        // 新建Callable任务对象
        Callable<Integer> sumTask = new SumTask();
        // 创建线程池
        ExecutorService executor = Executors.newCachedThreadPool();
        // 提交任务
        Future<Integer> res = executor.submit(sumTask);

        // 方式2
        FutureTask<Integer> futureTask2 = new FutureTask<>(sumTask);
        Thread t = new Thread(futureTask2, "t2");
        t.start();

        // 方式3
        FutureTask<Integer> futureTask3 = new FutureTask<>(sumTask);
        executor.submit(futureTask3);

        executor.shutdown();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        System.out.println("主线程在执行任务");

        try {
            System.out.println("task运行结果"+res.get());
            System.out.println("task2运行结果"+futureTask2.get());
            System.out.println("task3运行结果"+futureTask3.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println("所有任务执行完毕");


    }
}
