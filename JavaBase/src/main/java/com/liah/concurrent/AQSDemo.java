package com.liah.concurrent;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;

public class AQSDemo {

    private static class Sync extends AbstractQueuedSynchronizer{
        @Override
        protected boolean tryAcquire(int i) {
            if (super.compareAndSetState(0,1)){
                super.setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        @Override
        protected boolean tryRelease(int i) {
            if (super.getState() == 0){
                throw new IllegalMonitorStateException();
            }
            super.setExclusiveOwnerThread(null);
            super.setState(0);
            return true;
        }

        @Override
        protected boolean isHeldExclusively() {
            return super.getState() == 1;
        }

        Condition Condition(){
            return new ConditionObject();
        }
    }
}
