package com.liah.concurrent;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 创建两个线程，其中一个输出1-52，另外一个输出A-Z。输出格式要求：12A 34B 56C 78D
 * await与signal/signalAll对应
 * wait与notify/notifyAll对应
 * https://blog.csdn.net/weixin_42312342/article/details/87639827
 */
public class TestAlternate2 {

    private static ReentrantLock lock = new ReentrantLock();
    private static Condition charCon = lock.newCondition();
    private static Condition numberCon = lock.newCondition();

    static class NumberTask implements Runnable{

        @Override
        public void run() {
            lock.lock();
            try {
                for (int i = 1; i <= 52; i++) {
                    System.out.println(i);
                    if (i % 2 == 0) {
                        charCon.signalAll();    // 先唤醒字母打印线程
                        try {
                            numberCon.await();       // 再阻塞数字打印
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }
                    }
                }
            }finally {
                lock.unlock();
            }
        }
    }


    static class CharTask implements Runnable{
        public void run(){
            lock.lock();
            try {
                for (int i = 0; i < 26; i++) {
                    System.out.println((char)('A' + i));
                    // 每输出一个字符即需要唤醒数字线程，阻塞字符线程
                    numberCon.signalAll();
                    try {
                        if (i!=25)
                            charCon.await();
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args){
        Thread t1 = new Thread(new NumberTask());
        Thread t2 = new Thread(new CharTask());

        t1.start();
        t2.start();
    }

    public static void main2(String[] args) {
        ReentrantLock r1=new ReentrantLock(); //定义一个互斥锁类型
        Condition c = r1.newCondition();//定义一个字母锁条件
        Condition c2 = r1.newCondition();//定义一个数字锁条件
        new Thread(new Runnable(){

            public void run() {

                try{//逻辑严谨性,定义一个try finally,保证一定释放锁;
                    r1.lock();//获取锁
                    for (int i = 1; i <=52; i++) {
                        System.out.println(i+" ");
                        if(i%2==0){
                            c.signalAll();//唤醒字母进程
                            try {
                                c2.await();//让数字进程等待
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }
                }finally{
                    r1.unlock();//释放锁
                }
            }

        }).start();



        new Thread(new Runnable(){
            @Override
            public void run() {

                try{
                    r1.lock();
                    for (int i = 0; i < 26; i++) {
                        System.out.println((char)('A' + i) + " ");
                        c2.signalAll();
                        try {
                            if(i!=25){//最后一个字母不用进入等待状态
                                c.await();
                            }

                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }
                }finally{
                    r1.unlock();
                }

            }
        }).start();

    }
}
