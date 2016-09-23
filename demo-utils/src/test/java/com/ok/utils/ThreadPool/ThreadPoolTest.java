package com.ok.utils.ThreadPool;

import org.junit.Test;

import java.util.concurrent.Semaphore;

/**
 * 线程池
 * 面试题：一、控制一个方法的并发量，比如同时只能有5个线程
 * 1.利用信号量Semaphore api 控制
 */
public class ThreadPoolTest {
    //信号量，传入permits限制线程个数
    Semaphore semaphore = new Semaphore(8);//相当于5把锁

    @Test
    public  void TestThreadPool(){

        for(int i=0;i<100;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    method();
                }
            }).start();
        }
    }

    //同时允许5个线程过来
    public  void method() {
        try {

            semaphore.acquire();
            System.out.println("ThreadName="+Thread.currentThread().getName()+"进来了");
            Thread.sleep(1000);
//            System.out.println("ThreadName="+Thread.currentThread().getName()+"出去了");
            semaphore.release();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
