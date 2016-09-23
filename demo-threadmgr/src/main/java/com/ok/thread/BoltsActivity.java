package com.ok.thread;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import bolts.Continuation;
import bolts.Task;

/**
 * Created by lizhaoxiong on 2016/9/23.
 *
 */
public class BoltsActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bolts);

        createTask();
        createChainingTask();
        createGroupTask();
        createParallelTask();
        createSerialTask();
        createFaultedTask();
    }

    /**
     * onSuccess   只有在上一个task成功后才会执行这个方法。
     * continueWith   上一个方法无论正确与错误都会走这个方法，因此可以对错误、取消、完成三个状态进行处理。
     */
    private void createFaultedTask() {
        findViewById(R.id.btn_bolts_failed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Task.call(new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        Thread.sleep(2000);
//                        if (1 < 10) {
//                            Logger.d(Thread.currentThread().getName() + " RuntimeException");
//                            throw new RuntimeException("There was an error.");
//                        }
                        return "Hello Bolts";
                    }
                },Task.BACKGROUND_EXECUTOR).onSuccess(new Continuation<String, Void>() {
                    @Override
                    public Void then(Task<String> task) throws Exception {
                        Logger.d(Thread.currentThread().getName()+" onSuccess");
                        return null;
                    }
                },Task.UI_THREAD_EXECUTOR).continueWith(new Continuation<Void, Void>() {
                    @Override
                    public Void then(Task<Void> task) throws Exception {
                            if(task.isFaulted()){
                                Logger.d(Thread.currentThread().getName()+" onError");
                                return null;
                            }
                            if(task.isCompleted()){
                                Logger.d(Thread.currentThread().getName()+" onCompleted");
                                return null;
                            }
                            if(task.isCancelled()){
                                Logger.d(Thread.currentThread().getName()+" onCancelled");
                                return null;
                            }
                        Logger.d(Thread.currentThread().getName()+" done");
                        return null;
                    }
                },Task.UI_THREAD_EXECUTOR);
            }
        });
    }

    /**
     * 串行，FIFO
     */
    private void createSerialTask() {
        findViewById(R.id.btn_bolts_serial_tasks).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Executor SERIAL_EXECUTOR = Executors.newSingleThreadExecutor();

                Task.call(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        for(int i=0;i<50;i++){
                            Logger.d(Thread.currentThread().getName()+" i="+i);
                        }
                        return null;
                    }
                },SERIAL_EXECUTOR);

                Task.call(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        for(int i=50;i<100;i++){
                            Logger.d(Thread.currentThread().getName()+" i="+i);
                        }
                        return null;
                    }
                },SERIAL_EXECUTOR);
            }
        });
    }

    /**
     * 并行
     */
    private void createParallelTask() {
        findViewById(R.id.btn_bolts_parallel_tasks).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Executor PARALLEL_EXECUTOR = Executors.newCachedThreadPool();

                Task.call(new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        for(int i=0;i<100;i++){
                            if(i%2==0){
                                Logger.d(Thread.currentThread().getName()+" i="+i);
                            }
                        }
                        return null;
                    }
                },PARALLEL_EXECUTOR);

                Task.call(new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        for(int i=0;i<100;i++){
                            if(i%2==1){
                                Logger.d(Thread.currentThread().getName()+" i="+i);
                            }
                        }
                        return null;
                    }
                },PARALLEL_EXECUTOR);
            }
        });
    }

    /**
     * 请求多任务
     */
    private void createGroupTask() {
        findViewById(R.id.btn_bolts_group_tasks).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Task.call(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        Logger.d(Thread.currentThread().getName()+" calling");
                        return true;
                    }
                },Task.UI_THREAD_EXECUTOR).continueWith(new Continuation<Boolean, List<Task<Void>>>() {
                    @Override
                    public List<Task<Void>> then(Task<Boolean> task) throws Exception {
                        Logger.d(Thread.currentThread().getName()+" tasks start");
                        ArrayList<Task<Void>> tasks = new ArrayList<>();
                        tasks.add(asyncOperation1());
                        tasks.add(asyncOperation2());
                        Task.whenAll(tasks).waitForCompletion();
                        Logger.d(Thread.currentThread().getName()+" tasks end");
                        return null;
                    }

                    private Task<Void> asyncOperation1() {
                        return Task.callInBackground(new Callable<Void>() {
                            @Override
                            public Void call() throws Exception {
                                Logger.d("asyncOperation1 start");
                                Thread.sleep(2000);
                                Logger.d("asyncOperation1 end");
                                return null;
                            }
                        });
                    }
                    private Task<Void> asyncOperation2() {
                        return Task.callInBackground(new Callable<Void>() {
                            @Override
                            public Void call() throws Exception {
                                Logger.d("asyncOperation2 start");
                                Thread.sleep(2000);
                                Logger.d("asyncOperation2 end");
                                return null;
                            }
                        });
                    }


                },Task.BACKGROUND_EXECUTOR).continueWith(new Continuation<List<Task<Void>>, Void>() {
                    @Override
                    public Void then(Task<List<Task<Void>>> task) throws Exception {
                        Logger.d(Thread.currentThread().getName()+" done");
                        return null;
                    }
                },Task.UI_THREAD_EXECUTOR);
            }
        });
    }

    /**
     * 一次完整的请求
     */
    private void createChainingTask() {
        findViewById(R.id.btn_bolts_chaining).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Task.call(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        Logger.d(Thread.currentThread().getName() +" calling");
                        return true;
                    }
                },Task.UI_THREAD_EXECUTOR).continueWith(new Continuation<Boolean, String>() {
                    @Override
                    public String then(Task<Boolean> task) throws Exception {
                        Thread.sleep(2000);
                        Logger.d(Thread.currentThread().getName() +" onSuccess?"+task.getResult());
                        return "hello bolts";
                    }
                },Task.BACKGROUND_EXECUTOR).continueWith(new Continuation<String, Void>() {
                    @Override
                    public Void then(Task<String> task) throws Exception {
                        Logger.d(Thread.currentThread().getName() +" "+task.getResult());
                        return null;
                    }
                },Task.UI_THREAD_EXECUTOR);
            }
        });
    }

    /**
     * bolts begin
     */
    private void createTask() {
        findViewById(R.id.btn_bolts_task).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Task.call(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        Logger.d(Thread.currentThread().getName()+" "+"call in current thread");
                        return true;
                    }
                });

                Task.callInBackground(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        Logger.d(Thread.currentThread().getName() +" "+ "call in background thread");
                        return true;
                    }
                });

                Task.call(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        Logger.d(Thread.currentThread().getName()+" "+ "call in UI Thread");
                        return true;
                    }
                },Task.UI_THREAD_EXECUTOR);

                Task.delay(2000).continueWith(new Continuation<Void, Void>() {
                    @Override
                    public Void then(Task<Void> task) throws Exception {
                        Logger.d(Thread.currentThread().getName() +" "+ "call in main thread(after delay 2 seconds)");
                        return null;
                    }
                });
            }
        });
    }
}
