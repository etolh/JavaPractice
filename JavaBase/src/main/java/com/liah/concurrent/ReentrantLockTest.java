package com.liah.concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockTest {

    ReentrantLock lock = new ReentrantLock();
    public void lockTest(){

        try {
            lock.lock();
            lock.tryLock();
        }finally {
            lock.unlock();
        }

        try {
            lock.tryLock(5L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }

        try {
            lock.lockInterruptibly();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
}
