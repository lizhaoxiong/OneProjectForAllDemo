package com.ok.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.ok.ui.R;

import java.util.ArrayList;
import java.util.List;

public class MyFragmentActivity extends FragmentActivity implements View.OnClickListener{
    FragmentA fragmentA;
    FragmentB fragmentB;
    FragmentC fragmentC;
    List<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        findViewById(R.id.bt_1).setOnClickListener(this);
        findViewById(R.id.bt_2).setOnClickListener(this);
        findViewById(R.id.bt_3).setOnClickListener(this);


        fragmentA = new FragmentA();
        fragmentB = new FragmentB();
        fragmentC = new FragmentC();
        //将三个Fragment添加到帧布局中

        /**
         * 1.获取FragmentManager
         */
        FragmentManager fragmentManager = getSupportFragmentManager();
        /**
         * 2.获取事务管理器
         */
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        /**
         * 3.将fragment添加到帧布局中
         */
        transaction
                .add(R.id.fl,fragmentA,"FragmentA")
                .add(R.id.fl,fragmentB,"FragmentB")
                .add(R.id.fl,fragmentC,"FragmentC")
                .hide(fragmentB)
                .hide(fragmentC)
                .commit();

        addToBackStack(fragmentA);
    }

    private void addToBackStack(Fragment fragment) {
        if(fragmentList.contains(fragment)){
            //改变顺序
            fragmentList.remove(fragment);

            fragmentList.add(fragment);
        }else{
            fragmentList.add(fragment);
        }
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch(v.getId()){
            case R.id.bt_1:
                transaction.hide(fragmentB).hide(fragmentC).show(fragmentA);
                addToBackStack(fragmentA);
                break;
            case R.id.bt_2:
                transaction.hide(fragmentA).hide(fragmentC).show(fragmentB);
                addToBackStack(fragmentB);
                break;
            case R.id.bt_3:
                transaction.hide(fragmentA).hide(fragmentB).show(fragmentC);
                addToBackStack(fragmentC);
                break;
        }
//        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if(fragmentList.size()>1){
            //移除栈顶的fragment
            fragmentList.remove(fragmentList.size()-1);
            //将下一个fragment显示出来
            showFragment(fragmentList.get(fragmentList.size()-1));
        }else {
            finish();
        }
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .hide(fragmentA)
                .hide(fragmentB)
                .hide(fragmentC)
                .show(fragment).commit();
    }
}
