package com.ok.rxjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /******RxAndroid简单api使用******/
        //createRx();
        //createSimpleRx();
        //observableJust();
        //observableRange();
        //observableRepeat();

        Observable<String> observable = new Observable

    }

    private void observableRepeat() {
        Observable.just(1,2,3,4,5).repeat(3).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d(TAG,"onNext:"+integer);
            }
        });
    }

    private void observableRange() {
        Observable.range(0,10).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d(TAG,"onNext:"+integer);
            }
        });
    }

    private void observableJust() {
        Observable.just(1,3,4).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d(TAG,"onNext:"+integer);
            }
        });
    }

    private void createSimpleRx() {
        Observable<String> observable = Observable.just("我要吃肉肉");
        Action1<String> action1 = new Action1<String>() {
            @Override
            public void call(String s) {
                Log.d(TAG,"onNext，我要吃饭："+s);
            }
        };
        observable.subscribe(action1);
    }

    private void createRx() {
        //观察者
        Subscriber<String> stringSubscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Log.d(TAG,"onNext，我要吃饭："+s);
            }
        };

        //被观察者
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>(){

            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("去装一大碗米饭");
            }
        });

        //绑定
        observable.subscribe(stringSubscriber);
    }
}
