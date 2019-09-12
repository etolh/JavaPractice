package com.liah.concurrent;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 让三个线程交替打印123各20遍，即线程i始终打印i 123123
 *
 * 设置三个Condition，每个线程对应一个Condition
 * 设置一个nextThread变量表示下一个要打印的线程，当判断自己不是下一个要打印的线程，调用condition阻塞自己
 * 若是，打印完毕后继续唤醒下一个线程的Condition
 */
public class TestAlternate {
    public static void main(String[] args){
        int threadNum = 3;
        int loops = 20;
        // 同一个任务对象,公用一个lock对象和condition条件数组
        Task task = new Task(threadNum, loops);
        for (int i = 0 ; i < threadNum; i++){
            Thread t = new Thread(task, "thread"+(i+1));
            t.start();
        }
    }

    private static class Task implements Runnable{
        private int nextThread = 1;
        private ReentrantLock lock;
        private Condition[] conditions;
        private int loops;

        Task(int threadNum, int loops){
            this.loops = loops;
            this.lock = new ReentrantLock();
            this.conditions = new Condition[threadNum];
            for (int i = 0; i < threadNum; i++){
                conditions[i] = lock.newCondition();
            }
        }

        public void run(){
            for (int i = 0; i < loops; i++){
                lock.lock();
                int currentNo = Thread.currentThread().getName().charAt(6) - '0';
                try {
                    if (currentNo != nextThread) {
                        conditions[currentNo - 1].await();  // 下一个不是自己，等待
                    }
                    System.out.println("线程" + currentNo + ":" + currentNo); // 打印
                    nextThread = nextThread % conditions.length + 1;
                    // 唤醒下一个线程
                    conditions[nextThread-1].signal();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }finally {
                    lock.unlock();
                }
            }
        }
    }
}
