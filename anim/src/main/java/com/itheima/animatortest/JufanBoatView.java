package com.itheima.animatortest;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * <p>Copyright: Copyright (c) 2016</p>
 * <p/>
 * <p>Company: 浙江齐聚科技有限公司<a href="www.guagua.cn">www.guagua.cn</a></p>
 *
 * @author lizhaoxiong
 * @version 1.3.0
 * @description
 * @modify
 */
public class JufanBoatView extends RelativeLayout {

    protected static final String TAG = "JufanBoatView";
    private Context mContext;

    /**
     * 动画开始执行时间
     */
    private static final int TIME_BOAT_STOP = 7000;//轮船从开始到中途停止时间
    private static final int TIME_BOAT_UPDOWN = 5000;//轮船停止时间
    private static final int TIME_BOAT_END = 7000;//轮船停止到结束时间

    private static final int TIME_HEAD_START = 3000;//头像从水下到船上时间
    private static final int TIME_head_URPDOWN = 8000;//头像静止在船上时间
    private static final int TIME_HEAD_END = 3000;//头像从船上消失时间

    private static final int TIME_LANG_ALPHA = 15000;//最外层浪花渐变时间
    private static final int TIME_LANG_RIGHT = 15000;//中间层浪花时间
    private static final int TIME_LANG_LEFT = 15000;//最下层浪花时间

    private static final int TIME_FIRE_SPEED = 10;//烟花幀频率
    private static final long TIME_FIRE_DELAY = 2000;//烟花延迟发射时间

    /**
     * 动画状态
     */
    private static final int STATE_BOAT_STOP = 0;//轮船停止状态
    private static final int STATE_BOAT_GOON = 1;//轮船继续状态
    private static final int STATE_BOAT_Leave = 2;//轮船结束状态

    /**
     * 轮船相关参数
     */
    ImageView ship_img = null;//轮船控件
    private static final String SHIP_POSITION = "boat/ship.png";//轮船图片位置

    /**
     * 海浪相关参数
     */
    ImageView lang1 = null;//海浪一号图
    ImageView lang2 = null;//海浪二号图
    ImageView lang3 = null;//海浪三号图
    private static final String[] lang_Position = //海浪图片位置
            {"boat/lang1.png", "boat/lang2.png", "boat/lang3.png"};

    /**
     * 泡泡相关参数
     */
    ImageView paopao;
    ImageView paopao2;
    ImageView paopao3;
    AnimationDrawable paopaoAnim;
    AnimationDrawable paopaoAnim2;
    AnimationDrawable paopaoAnim3;

    /**
     * 烟花相关参数
     */
    ImageView fire_img;
    AnimationDrawable fireAnim;//烟花动画
    private int[] meetPics = new int[]{
            R.drawable.fire01, R.drawable.fire02, R.drawable.fire03, R.drawable.fire04,
            R.drawable.fire05, R.drawable.fire06, R.drawable.fire07, R.drawable.fire08,
            R.drawable.fire09, R.drawable.fire10, R.drawable.fire11, R.drawable.fire12,
            R.drawable.fire13, R.drawable.fire14, R.drawable.fire15, R.drawable.fire16,
            R.drawable.fire17, R.drawable.fire18, R.drawable.fire19, R.drawable.fire20,
            R.drawable.fire21, R.drawable.fire22
    };

    /**
     * 船长头像相关参数
     */
    ImageView head = null;//头像控件
    private static final String HEAD_POSITION = "boat/head.png";//头像图片位置


    public JufanBoatView(Context context) {
        super(context);
        this.mContext = context;
        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View anim = mInflater.inflate(R.layout.anim, null);
        this.addView(anim);
        init();
    }


    /**
     * 控制动画执行
     */
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case STATE_BOAT_STOP:
                    FireworksDeVoice();
                    headDeVoice();
                    break;
                case STATE_BOAT_GOON:
                    //移除掉
                    break;
                case STATE_BOAT_Leave:
                    //移除所有动画
                    break;
                case 2000:
                    shipAnimatorSet();
                    break;
            }
        }
    };

    private void init() {

        /** 初始化轮船 */
        ship_img = (ImageView) findViewById(R.id.view);
        ship_img.setImageBitmap(GetAssetsUtils.getBitmapImage(SHIP_POSITION, mContext));

        /** 初始化海浪 */
        lang1 = (ImageView) findViewById(R.id.lang1);
        lang2 = (ImageView) findViewById(R.id.lang2);
        lang3 = (ImageView) findViewById(R.id.lang3);
        lang1.setAlpha(.3f);
        lang2.setAlpha(.3f);
        lang3.setAlpha(.1f);
        lang1.setImageBitmap(GetAssetsUtils.getBitmapImage(lang_Position[0], mContext));
        lang2.setImageBitmap(GetAssetsUtils.getBitmapImage(lang_Position[1], mContext));
        lang3.setImageBitmap(GetAssetsUtils.getBitmapImage(lang_Position[2], mContext));

        /** 初始化泡泡 */
        paopao = (ImageView) findViewById(R.id.paopao);
        paopao2 = (ImageView) findViewById(R.id.paopao2);
        paopao3 = (ImageView) findViewById(R.id.paopao3);

        /** 初始化烟花 */
        fire_img = (ImageView) findViewById(R.id.fire);

        /** 初始化头像 */
        head = (ImageView) findViewById(R.id.head);
        head.setImageBitmap(GetAssetsUtils.getBitmapImage(HEAD_POSITION, mContext));
        head.setAlpha(0.0f);
    }

    /**
     * 头像动画--渐变升上移，停顿，渐变浅上移
     */
    private void headDeVoice() {
        int top = head.getTop();
        int bottom = head.getBottom();
        PropertyValuesHolder pvhAlpha1 = PropertyValuesHolder.ofFloat("alpha", .0f, .4f, 1f);
        PropertyValuesHolder pvhTop1 = PropertyValuesHolder.ofInt("top", top, top - 600);
        PropertyValuesHolder pvhBottom1 = PropertyValuesHolder.ofInt("bottom", bottom, bottom - 600);
        ObjectAnimator headAnim1 = ObjectAnimator.ofPropertyValuesHolder(head, pvhAlpha1, pvhTop1, pvhBottom1);
        headAnim1.setDuration(TIME_HEAD_START);

        top = top - 600;
        bottom = bottom - 600;
        PropertyValuesHolder pvhAlpha2 = PropertyValuesHolder.ofFloat("alpha", 1f, .4f, .0f);
        PropertyValuesHolder pvhTop2 = PropertyValuesHolder.ofInt("top", top, top - 600);
        PropertyValuesHolder pvhBottom2 = PropertyValuesHolder.ofInt("bottom", bottom, bottom - 600);
        ObjectAnimator headAnim2 = ObjectAnimator.ofPropertyValuesHolder(head, pvhAlpha2, pvhTop2, pvhBottom2);
        headAnim2.setDuration(TIME_HEAD_END);

        //********一起摇摆*******
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(headAnim1);
        animSet.play(headAnim2).after(TIME_head_URPDOWN);
        animSet.start();

        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                ViewGroup parent = (ViewGroup) head.getParent();
                if (parent != null)
                    parent.removeView(head);
                mHandler.sendEmptyMessage(STATE_BOAT_GOON);

                ViewGroup parent5 = (ViewGroup) fire_img.getParent();
                if (parent5 != null) {
                    parent5.removeView(fire_img);
                }
            }
        });
    }

    /**
     * 烟花动画--分布式加载帧动画
     */
    private void FireworksDeVoice() {
        SceneAnimation anim = new SceneAnimation(fire_img, meetPics, TIME_FIRE_SPEED,TIME_FIRE_DELAY);
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
        paopaoAnim3 = (AnimationDrawable) paopao3.getDrawable();
        paopaoAnim3.start();
    }

    /**
     * 海浪的动画--第三张渐变、第一张平移左到右、第二张平移右到左
     */
    private void langDeVoice() {

        PropertyValuesHolder pvhAlpha = PropertyValuesHolder.ofFloat("alpha", .2f, .4f, .2f);
        ObjectAnimator langAnim3 = ObjectAnimator.ofPropertyValuesHolder(lang3, pvhAlpha);
        langAnim3.setDuration(TIME_LANG_ALPHA);
        langAnim3.start();

        PropertyValuesHolder pvhTransXLeft = PropertyValuesHolder.ofFloat("translationX", 400);
        ObjectAnimator langAnim2 = ObjectAnimator.ofPropertyValuesHolder(lang2, pvhTransXLeft);
        langAnim2.setDuration(TIME_LANG_RIGHT);
        langAnim2.start();

        PropertyValuesHolder pvhTransXRight = PropertyValuesHolder.ofFloat("translationX", -400);
        ObjectAnimator langAnim1 = ObjectAnimator.ofPropertyValuesHolder(lang1, pvhTransXRight);
        langAnim1.setDuration(TIME_LANG_LEFT);
        langAnim1.start();

    }

    /**
     * 轮船动画--前：移动放大10秒、中：上下8秒、后：前放到7秒
     */
    public void shipAnimatorSet() {
        /*******前动画*********/
        // 获取view左边位置
        int left = ship_img.getLeft();
        // 获取view右边位置
        int right = ship_img.getRight();
        // 获取view上边位置
        int top = ship_img.getTop();
        // 获取view下边位置
        int bottom = ship_img.getBottom();
        // 将view左边增加10像素
        PropertyValuesHolder pvhLeft = PropertyValuesHolder.ofInt("left", left, left + 1000);
        // 将view右边减少10像素
        PropertyValuesHolder pvhRight = PropertyValuesHolder.ofInt("right", right, right + 1000);
        // 将view的上边。。。
        PropertyValuesHolder pvhTop = PropertyValuesHolder.ofInt("top", top, top + 100);
        // 将view的下边。。。
        PropertyValuesHolder pvhBottom = PropertyValuesHolder.ofInt("bottom", bottom, bottom + 100);
        // 在X轴缩放从原始比例1f,缩小到最小0f,再放大到原始比例1f
        PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofFloat("scaleX", .3f, .8f);
        // 在Y轴缩放从原始比例1f,缩小到最小0f,再放大到原始比例1f
        PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofFloat("scaleY", .3f, .8f);
        // 将PropertyValuesHolder交付给ObjectAnimator进行构建
        ObjectAnimator anim1 = ObjectAnimator.ofPropertyValuesHolder(ship_img,
                pvhLeft, pvhRight, pvhTop, pvhBottom, pvhScaleX, pvhScaleY);
        anim1.setDuration(TIME_BOAT_STOP);


        /********中动画********/
//        ObjectAnimator anim2 = ObjectAnimator.ofFloat(ship_img, "translationY",
//                100);
//        // 设置插值器(用于调节动画执行过程的速度)
//        anim2.setInterpolator(new BounceInterpolator());
//        // 设置重复次数(缺省为0,表示不重复执行)
//        anim2.setRepeatCount(1);
//        // 设置重复模式(RESTART或REVERSE),重复次数大于0或INFINITE生效
//        anim2.setRepeatMode(ValueAnimator.REVERSE);
//        // 设置动画开始的延时时间(200ms)
//        anim2.setDuration(8000);
        left = left + 1000;
        right = right + 1000;
        top = top + 100;
        bottom = bottom + 100;
        PropertyValuesHolder pvhLeft2 = PropertyValuesHolder.ofInt("left", left, left);
        PropertyValuesHolder pvhRight2 = PropertyValuesHolder.ofInt("right", right, right);
        PropertyValuesHolder pvhTop2 = PropertyValuesHolder.ofInt("top", top, top + 50);
        PropertyValuesHolder pvhBottom2 = PropertyValuesHolder.ofInt("bottom", bottom, bottom + 50);
        ObjectAnimator anim2 = ObjectAnimator.ofPropertyValuesHolder(ship_img, pvhLeft2, pvhRight2,
                pvhTop2, pvhBottom2);
        anim2.setInterpolator(new LinearInterpolator());
        anim2.setRepeatMode(ValueAnimator.REVERSE);
        anim2.setRepeatCount(1);
        anim2.setDuration(TIME_BOAT_UPDOWN);

        //*********后动画********
//        left = left+1000;
//        right = right+1000;
//        top = top+500;
//        bottom = bottom+500;
//        top = top+100;
//        bottom = bottom+100;
        // 将view左边增加10像素
        PropertyValuesHolder pvhLeft3 = PropertyValuesHolder.ofInt("left", left, left + 1500);
        // 将view右边减少10像素
        PropertyValuesHolder pvhRight3 = PropertyValuesHolder.ofInt("right", right, right + 1500);
        // 将view的上边。。。
        PropertyValuesHolder pvhTop3 = PropertyValuesHolder.ofInt("top", top, top + 100);
        // 将view的下边。。。
        PropertyValuesHolder pvhBottom3 = PropertyValuesHolder.ofInt("bottom", bottom, bottom + 100);
        // 在X轴缩放从原始比例1f,缩小到最小0f,再放大到原始比例1f
        PropertyValuesHolder pvhScaleX3 = PropertyValuesHolder.ofFloat("scaleX", .8f, 1.5f);
        // 在Y轴缩放从原始比例1f,缩小到最小0f,再放大到原始比例1f
        PropertyValuesHolder pvhScaleY3 = PropertyValuesHolder.ofFloat("scaleY", .8f, 1.5f);
        // 将PropertyValuesHolder交付给ObjectAnimator进行构建
        ObjectAnimator anim3 = ObjectAnimator.ofPropertyValuesHolder(ship_img,
                pvhLeft3, pvhRight3, pvhTop3, pvhBottom3, pvhScaleX3, pvhScaleY3);
        anim3.setDuration(TIME_BOAT_END);
        ship_img.animate().translationX(0).withLayer();

        /********一起摇摆*******/
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(anim1);
        animSet.play(anim2).after(anim1);
        animSet.play(anim3).after(anim2);

        animSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                ship_img.setLayerType(View.LAYER_TYPE_HARDWARE,null);
                langDeVoice();
                paopaoDeVoice();
                mHandler.sendEmptyMessageDelayed(STATE_BOAT_STOP, TIME_BOAT_STOP);

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                ship_img.setLayerType(View.LAYER_TYPE_NONE,null);
//                ViewGroup parent = (ViewGroup) head.getParent();
//                if (parent != null)
//                    parent.removeView(head);

                ViewGroup parent2 = (ViewGroup) ship_img.getParent();
                if (parent2 != null)
                    parent2.removeView(ship_img);

                ViewGroup parent3 = (ViewGroup) lang1.getParent();
                if (parent3 != null) {
                    parent3.removeView(lang1);
                    parent3.removeView(lang2);
                    parent3.removeView(lang3);
                }

                ViewGroup parent4 = (ViewGroup) paopao.getParent();
                if (parent4 != null) {
                    parent4.removeView(paopao);
                    parent4.removeView(paopao2);
                    parent4.removeView(paopao3);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animSet.start();

//        langDeVoice();
//        paopaoDeVoice();
//        mHandler.sendEmptyMessageDelayed(STATE_BOAT_STOP, TIME_BOAT_STOP);

    }


}
