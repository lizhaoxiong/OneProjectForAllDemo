package com.ok.ui.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.ok.ui.R;

/**
 * 1.自定义子线程向主线程发送消息
 * 2.处理当activity变成垃圾时，依然，由于looper内部的死循环依然
 *
 */
public class HandlerActivity extends AppCompatActivity {
    private Handler subHandler;
    private Looper looper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler);

        /**
         * 匿名内部类对象对外部类有一隐式的强引用
         * 解决办法：activity不用时将子线程销毁掉
         * soso : 线程stop是有严重bug的，那么让线程执行完，就是将run方法执行完
         * 而就卡在Looper.loop()里面的for的死循环，想办法释放掉
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                /**
                 * 创建Looper和MQ
                 */
                Looper.prepare();

                /**
                 * 创建子线程Handler
                 */
                subHandler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        Toast.makeText(HandlerActivity.this,msg.obj.toString(),Toast.LENGTH_SHORT).show();
                    }
                };

                /**
                 * 终止掉死循环looper
                 */
                looper = Looper.myLooper();

                /**
                 * 建立取消息的循环
                 */
                Looper.loop();

                System.out.println("死循环停止");
            }
        }).start();
    }

    /**
     * 主线程发送消息
     */
    public void sendMsg(View view){
        subHandler.obtainMessage(2,"主线程向子线程发送的消息").sendToTarget();
    }

    /**
     * activity销毁时，结束掉死循环
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(looper!=null){
            //不再接收消息
            looper.quitSafely();//api 18,和quit区别在在于他只是清空掉了延迟消息，而非延迟消息交给handler处理
            looper=null;
        }
    }
}
