package com.itheima.animatortest;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;

public class MainActivity extends Activity {

    protected static final String TAG = "MainActivity";

    /** 动画开始执行时间 */
    private static final Long TIME_BOAT_STOP = 10000l;//轮船从开始到中途停止时间
    private static final Long TIME_BOAT_UPDOWN = 8000l;//轮船停止时间
    private static final Long TIME_BOAT_END = 7000l;//轮船停止到结束时间

    /** 动画状态 */
    private static final int STATE_BOAT_STOP = 0;//轮船停止状态
    private static final int STATE_BOAT_GOON = 1;//轮船继续状态
    private static final int STATE_BOAT_Leave = 2;//轮船结束状态

    /** 轮船相关参数 */
    ImageView ship_img = null;//轮船控件
    private static final String SHIP_POSITION= "boat/ship.png";//轮船图片位置

    /** 海浪相关参数 */
    ImageView lang1 = null;//海浪一号图
    ImageView lang2 = null;//海浪二号图
    ImageView lang3 = null;//海浪三号图
    private static final String[] lang_Position = //海浪图片位置
            {"boat/lang1.png","boat/lang2.png","boat/lang3.png"};

    /** 泡泡相关参数 */
    ImageView paopao;
    ImageView paopao2;
    ImageView paopao3;
    AnimationDrawable paopaoAnim;
    AnimationDrawable paopaoAnim2;
    AnimationDrawable paopaoAnim3;

    /** 烟花相关参数 */
    ImageView fire_img;
    AnimationDrawable fireAnim;//烟花动画
    private int[] meetPics = new int[]{
        R.drawable.fire01,R.drawable.fire02,R.drawable.fire03,R.drawable.fire04,
            R.drawable.fire05,R.drawable.fire06,R.drawable.fire07,R.drawable.fire08,
            R.drawable.fire09,R.drawable.fire10,R.drawable.fire11,R.drawable.fire12,
            R.drawable.fire13,R.drawable.fire14,R.drawable.fire15,R.drawable.fire16,
            R.drawable.fire17,R.drawable.fire18,R.drawable.fire19,R.drawable.fire20,
            R.drawable.fire21,R.drawable.fire22
    };

    /** 船长头像相关参数 */
    ImageView head =null;//头像控件
    private static final String HEAD_POSITION= "boat/head.png";//头像图片位置
    AnimationDrawable headAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    /** 控制动画执行 */
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case STATE_BOAT_STOP:
                    FireworksDeVoice();
                    headDeVoice();
                    break;
                case STATE_BOAT_GOON:

                    break;
                case STATE_BOAT_Leave:
                    //移除所有动画
                    break;
            }
        };
    };

    private void init() {
        /** 初始化轮船 */
        ship_img = (ImageView) findViewById(R.id.view);
        ship_img.setImageBitmap(GetAssetsUtils.getBitmapImage(SHIP_POSITION,MainActivity.this));

        /** 初始化海浪 */
        lang1 = (ImageView) findViewById(R.id.lang1);
        lang2 = (ImageView) findViewById(R.id.lang2);
        lang3 = (ImageView) findViewById(R.id.lang3);
        lang1.setAlpha(.3f);
        lang2.setAlpha(.3f);
        lang3.setAlpha(.1f);
        lang1.setImageBitmap(GetAssetsUtils.getBitmapImage(lang_Position[0],MainActivity.this));
        lang2.setImageBitmap(GetAssetsUtils.getBitmapImage(lang_Position[1],MainActivity.this));
        lang3.setImageBitmap(GetAssetsUtils.getBitmapImage(lang_Position[2],MainActivity.this));

        /** 初始化泡泡 */
        paopao = (ImageView) findViewById(R.id.paopao);
        paopao2 = (ImageView) findViewById(R.id.paopao2);
        paopao3 = (ImageView) findViewById(R.id.paopao3);

        /** 初始化烟花 */
        fire_img = (ImageView) findViewById(R.id.fire);

        /** 初始化头像 */
        head = (ImageView) findViewById(R.id.head);
        head.setImageBitmap(GetAssetsUtils.getBitmapImage(HEAD_POSITION,MainActivity.this));

    }

    public void click(View v) {
        shipAnimatorSet();
        langDeVoice();
        paopaoDeVoice();
        mHandler.sendEmptyMessageDelayed(STATE_BOAT_STOP,TIME_BOAT_STOP);
    }

    /**
     * 头像动画--渐变升上移，停顿，渐变浅上移
     */
    private void headDeVoice(){
        int top = 800;
        int bottom = top+head.getHeight();
        PropertyValuesHolder pvhAlpha1 = PropertyValuesHolder.ofFloat("alpha",.0f, .4f, 1f);
        PropertyValuesHolder pvhTop1 = PropertyValuesHolder.ofInt("top",top,top-200);
        PropertyValuesHolder pvhBottom1 = PropertyValuesHolder.ofInt("bottom",bottom,bottom-200);
        ObjectAnimator headAnim1 = ObjectAnimator.ofPropertyValuesHolder(head,pvhAlpha1,pvhTop1,pvhBottom1);
        headAnim1.setDuration(3000);


        //5秒的无动画
        PropertyValuesHolder noAnim = PropertyValuesHolder.ofFloat("alpha");
        ObjectAnimator headAnim0 = ObjectAnimator.ofPropertyValuesHolder(head,noAnim);
        headAnim0.setDuration(3000);

        top = head.getTop();
        bottom = head.getBottom();
        PropertyValuesHolder pvhAlpha2 = PropertyValuesHolder.ofFloat("alpha",1f, .4f, .0f);
        PropertyValuesHolder pvhTop2 = PropertyValuesHolder.ofInt("top",top,top-200);
        PropertyValuesHolder pvhBottom2 = PropertyValuesHolder.ofInt("bottom",bottom,bottom-200);
        ObjectAnimator headAnim2 = ObjectAnimator.ofPropertyValuesHolder(head,pvhAlpha2,pvhTop2,pvhBottom2);
        headAnim2.setDuration(3000);

        //********一起摇摆*******
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(headAnim1);
        animSet.play(headAnim2).after(headAnim1);
        animSet.start();

        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mHandler.sendEmptyMessage(STATE_BOAT_GOON);
            }
        });
    }

    /**
     * 烟花动画--分布式加载帧动画
     */
    private void FireworksDeVoice() {
        SceneAnimation anim = new SceneAnimation(fire_img, meetPics, 10);
    }

    /**
     * 气泡动画--平分三个区域，从下到上的帧动画
     */
    private void paopaoDeVoice() {
        paopao.setImageResource(R.drawable.paopao_list);
        paopaoAnim = (AnimationDrawable) paopao.getDrawable();
        paopaoAnim.start();

        paopao2.setImageResource(R.drawable.paopao_list);
        paopaoAnim2 = (AnimationDrawable) paopao2.getDrawable();
        paopaoAnim2.start();

        paopao3.setImageResource(R.drawable.paopao_list);
        paopaoAnim3= (AnimationDrawable) paopao3.getDrawable();
        paopaoAnim3.start();
    }

    /**
     * 海浪的动画--第三张渐变、第一张平移左到右、第二张平移右到左
     */
    private void langDeVoice() {

        PropertyValuesHolder pvhAlpha = PropertyValuesHolder.ofFloat("alpha",.2f, .4f, .2f);
        ObjectAnimator langAnim3 = ObjectAnimator.ofPropertyValuesHolder(lang3,pvhAlpha);
        langAnim3.setDuration(10000);
        langAnim3.start();

        PropertyValuesHolder pvhTransXLeft = PropertyValuesHolder.ofFloat("translationX",400);
        ObjectAnimator langAnim2 = ObjectAnimator.ofPropertyValuesHolder(lang2,pvhTransXLeft);
        langAnim2.setDuration(10000);
        langAnim2.start();

        PropertyValuesHolder pvhTransXRight = PropertyValuesHolder.ofFloat("translationX",-400);
        ObjectAnimator langAnim1 = ObjectAnimator.ofPropertyValuesHolder(lang1,pvhTransXRight);
        langAnim1.setDuration(10000);
        langAnim1.start();

    }

    /**
     * 轮船动画--前：移动放大10秒、中：上下8秒、后：前放到7秒
     */
    private void shipAnimatorSet(){

        //*******前动画*********
        // 获取view左边位置
        int left = -ship_img.getWidth()+30;
        // 获取view右边位置
        int right = 30;
        // 获取view上边位置
        int top = 100;
        // 获取view下边位置
        int bottom = top+ship_img.getHeight();
        // 将view左边增加10像素
        PropertyValuesHolder pvhLeft = PropertyValuesHolder.ofInt("left", left,left + 1000);
        // 将view右边减少10像素
        PropertyValuesHolder pvhRight = PropertyValuesHolder.ofInt("right",right, right + 1000);
        // 将view的上边。。。
        PropertyValuesHolder pvhTop = PropertyValuesHolder.ofInt("top",top,top+500);
        // 将view的下边。。。
        PropertyValuesHolder pvhBottom = PropertyValuesHolder.ofInt("bottom",bottom,bottom+500);
        // 在X轴缩放从原始比例1f,缩小到最小0f,再放大到原始比例1f
        PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofFloat("scaleX",.3f, 1f);
        // 在Y轴缩放从原始比例1f,缩小到最小0f,再放大到原始比例1f
        PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofFloat("scaleY",.3f, 1f);
        // 将PropertyValuesHolder交付给ObjectAnimator进行构建
        ObjectAnimator anim1 = ObjectAnimator.ofPropertyValuesHolder(ship_img,
                pvhLeft, pvhRight,pvhTop,pvhBottom,pvhScaleX, pvhScaleY);
        anim1.setDuration(STATE_BOAT_STOP);

        //********中动画********
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(ship_img, "translationY",
                100);
        // 设置插值器(用于调节动画执行过程的速度)
        anim2.setInterpolator(new BounceInterpolator());
        // 设置重复次数(缺省为0,表示不重复执行)
        anim2.setRepeatCount(1);
        // 设置重复模式(RESTART或REVERSE),重复次数大于0或INFINITE生效
        anim2.setRepeatMode(ValueAnimator.REVERSE);
        anim2.setDuration(TIME_BOAT_UPDOWN);

        //*********后动画********
        left = left+1000;
        right = left+1000;
        top = top+500;
        bottom = bottom+500;
        // 将view左边增加10像素
        PropertyValuesHolder pvhLeft3 = PropertyValuesHolder.ofInt("left", left,left + 1500);
        // 将view右边减少10像素
        PropertyValuesHolder pvhRight3 = PropertyValuesHolder.ofInt("right",right, right + 1500);
        // 将view的上边。。。
        PropertyValuesHolder pvhTop3 = PropertyValuesHolder.ofInt("top",top,top+800);
        // 将view的下边。。。
        PropertyValuesHolder pvhBottom3 = PropertyValuesHolder.ofInt("bottom",bottom,bottom+800);
        // 在X轴缩放从原始比例1f,缩小到最小0f,再放大到原始比例1f
        PropertyValuesHolder pvhScaleX3 = PropertyValuesHolder.ofFloat("scaleX",1f, 1.5f);
        // 在Y轴缩放从原始比例1f,缩小到最小0f,再放大到原始比例1f
        PropertyValuesHolder pvhScaleY3 = PropertyValuesHolder.ofFloat("scaleY",1f, 1.5f);
        // 将PropertyValuesHolder交付给ObjectAnimator进行构建
        ObjectAnimator anim3 = ObjectAnimator.ofPropertyValuesHolder(ship_img,
                pvhLeft3, pvhRight3,pvhTop3,pvhBottom3,pvhScaleX3, pvhScaleY3);
        anim3.setDuration(TIME_BOAT_END);


        //********一起摇摆*******
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(anim1);
        animSet.play(anim2).after(anim1);
        animSet.play(anim3).after(anim2);
        animSet.start();
    }
}
