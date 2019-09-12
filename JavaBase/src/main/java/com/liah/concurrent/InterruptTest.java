package com.liah.concurrent;


public class InterruptTest {

    static class MyThread extends Thread{
        public synchronized void run(){
            System.out.println(Thread.currentThread().getName() + "运行");
            /*
            while (!Thread.currentThread().isInterrupted()) {
                System.out.println("线程未中断运行");
            }
            System.out.println("线程被中断");
            */
            try {
                System.out.println("进入睡眠状态");
//                Thread.currentThread().sleep(10000);
                Thread.currentThread().wait();
                System.out.println("1");
                System.out.println("睡眠完毕");
            } catch (InterruptedException e) {

                System.out.println("得到中断异常" + Thread.currentThread().isInterrupted());
            }
            System.out.println("run方法执行完毕");
        }
    }

    public static void main(String[] args) {

        System.out.println("主线程运行");
        MyThread t1 = new MyThread();
        t1.start();
        try {
            Thread.currentThread().sleep(2000);
        } catch (InterruptedException e) {

        }
        t1.interrupt();
    }
}