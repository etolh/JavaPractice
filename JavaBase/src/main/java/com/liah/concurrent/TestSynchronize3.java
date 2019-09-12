package com.liah.concurrent;



/**
 * 停车场
 */
class Park{
    private volatile int state = 3; // 表示车位个数
    private final Object lock = new Object();

    public  void carIn(String name){
        synchronized(lock) {
            try {
                while (state == 0) {
                    System.out.println("目前空余车位为：" + state + "请等待");
                    lock.wait();    // 阻塞
                }

                System.out.println(name+"车位停车成功");
                state=state-1;
                System.out.println("目前剩余车位为："+state);

                lock.notifyAll();   // 释放
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public void carOut(String name){
        synchronized(lock) {
            try {
                while (state == 3) {
                    System.out.println("目前无车,请等待");
                    lock.wait();    // 阻塞
                }


                System.out.println(name+"车驶出");
                state=state+1;
                System.out.println("目前剩余车位为："+state);

                lock.notifyAll();   // 释放
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}

class CarIn implements Runnable{

    private Park park;
    private String name;
    CarIn(Park park, String name){
        this.park = park;
        this.name = name;
    }

    @Override
    public void run() {
        park.carIn(name);
    }
}

class CarOut implements Runnable{

    private Park park;
    private String name;
    CarOut(Park park, String name){
        this.park = park;
        this.name = name;
    }

    @Override
    public void run() {
        park.carOut(name);
    }
}

/**
 * 假设车库有3个车位（可以通过boolean[]数组来表示车库）可以停车，写一个程序模拟多个用户开车离开，停车入库的效果
 * https://blog.csdn.net/Fly_as_tadpole/article/details/84064058
 */
public class TestSynchronize3 {

    public static void main(String[] args) {
        Park park = new Park();
        Thread t1 = new Thread(new CarIn(park, "宝马"));
        Thread t2 = new Thread(new CarIn(park, "奔驰"));
        Thread t3 = new Thread(new CarIn(park, "奥迪"));
        t1.start();
        t2.start();
        t3.start();

        Thread t4 = new Thread(new CarOut(park, "宝马"));
        Thread t5 = new Thread(new CarOut(park, "奔驰"));
        Thread t6 = new Thread(new CarOut(park, "奥迪"));
        t4.start();
        t5.start();
        t6.start();
    }
}
