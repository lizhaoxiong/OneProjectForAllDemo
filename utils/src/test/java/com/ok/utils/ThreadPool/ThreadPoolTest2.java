package com.ok.utils.ThreadPool;

import org.junit.Test;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程池
 * 面试题：
 * 一、控制一个方法的并发量，比如同时只能有5个线程
 * 1.利用api 2.利用线程池
 * 二、线程池的启动策略
 */
public class ThreadPoolTest2 {
    private Executor executor = Executors.newCachedThreadPool();//缓存线程池
    private Executor executor2 = Executors.newFixedThreadPool(5);//固定线程个数的线程池
    private Executor executor3 = Executors.newScheduledThreadPool(5);//计划任务线程池
    private Executor executor4 = Executors.newSingleThreadExecutor();//单个线程的线程池



    @Test
    public  void TestThreadPool(){
        /**
         *  corePoolSize:线程池同时存在的线程个数，也就是当任务全部执行完成后，线程池剩余可复用的线程数
         *      如果是0，那么就会退出程序
         *      Google推荐值：线程个数 = Cpu核心数+1
         *  maximumPoolSize:当缓冲队列满后，往这里放，如果它也满了就会爆线程拒绝异常
         *      so 并发量指的是 maximumPoolSize+BlockingQueueSize
         *  keepAliveTime unit:任务执行完成后存活时间,延迟裁员
         *  workQueue:BlockingQueue<E>单端队列（FIFO,FILO）、BlockingDeque<E>双端队列
         *  threadFactory:创建线程
         */
        ThreadFactory factory = new ThreadFactory() {
            //int i = 0;
            //线程安全的int的包装类
            AtomicInteger atomicInteger = new AtomicInteger(0);
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("MyThread="+atomicInteger.getAndIncrement());
                return thread;
            }
        };
        ThreadPoolExecutor myThreadPoolEx = new ThreadPoolExecutor(20, 20, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(100), factory);

        /** 用自定义的线程池管理者 */
        for(int i=0;i<105;i++){//rejectedExecution
            myThreadPoolEx.execute(new Runnable() {
                @Override
                public void run() {
                    method();
                }
            });
        }
        /** 1.5api封装好的线程池管理者 */
//        for(int i=0;i<100;i++){
//            executor2.execute(new Runnable() {
//                @Override
//                public void run() {
//                    method();
//                }
//            });
//        }
    }

    //同时允许5个线程过来
    public  void method() {
        try {
            System.out.println("ThreadName="+Thread.currentThread().getName()+"进来了");
            Thread.sleep(10);
//            System.out.println("ThreadName="+Thread.currentThread().getName()+"出去了");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
