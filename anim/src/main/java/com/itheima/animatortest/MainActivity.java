package com.itheima.animatortest;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.content.Context;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {

    protected static final String TAG = "MainActivity";
    ImageView view = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view = (ImageView) findViewById(R.id.view);
        view.setImageBitmap(GetAssetsUtils.getBitmapImage("boat/ship.png",MainActivity.this));
    }

    public void click(View v) {
        // objectAnimator();
        // valueAnimator();
        // propertyValuesHolder();
        // keyframe();
        // animatorSet();
        // animatorUpdateListener();
        // animatorListener();
        //animatorInflater();
        // paowuxian(view);
        //mutiObjectAnimator();

        shipAnimatorSet();
    }
    /**
     * 抛物线
     * @param view
     */
    public void paowuxian(final View view) {
        
        // 类型估值 - 抛物线示例
        TypeEvaluator<PointF> typeEvaluator = new TypeEvaluator<PointF>() {
            @Override
            public PointF evaluate(float fraction, PointF startValue,
                    PointF endValue) {
                float time = fraction * 3;
                Toast.makeText(getApplicationContext(),time+" ",Toast.LENGTH_SHORT).show();
                // x方向200px/s ，y方向0.5 * 200 * t * t
                PointF point = new PointF();
                point.x = 200 * time+300;
                point.y = 0.5f * 200 * time * time;
                return point;
            }
        };
        ValueAnimator valueAnimator = ValueAnimator.ofObject(typeEvaluator,
                new PointF(0, 0));
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setDuration(1000);
        valueAnimator.start();
        
        valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF point = (PointF) animation.getAnimatedValue();
                view.setX(point.x);
                view.setY(point.y);
            }
        });
    }

    private void animatorInflater() {
        // 加载xml属性动画
        Animator anim = AnimatorInflater
                .loadAnimator(this, R.anim.animator_set);
        anim.setTarget(view);
        anim.start();
    }

    private void animatorListener() {
        // 将view透明度从当前的1.0f更新为0.5f,在动画结束时移除该View
        ObjectAnimator anim = ObjectAnimator.ofFloat(view, "alpha", 0.5f);
        anim.setDuration(1000);
        anim.addListener(new AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.e(TAG, "onAnimationEnd");
                ViewGroup parent = (ViewGroup) view.getParent();
                if (parent != null)
                    parent.removeView(view);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }
        });
        anim.start();
    }

    private void animatorUpdateListener() {
        // 1. 在回调中手动更新View对应属性：
        AnimatorUpdateListener l = new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                // 当前的分度值范围为0.0f->1.0f
                // 分度值是动画执行的百分比。区别于AnimatedValue。
                float fraction = animation.getAnimatedFraction();
                Toast.makeText(getApplicationContext(),fraction+" ",Toast.LENGTH_SHORT).show();
                // 以下的的效果为 View从完全透明到不透明,
                view.setAlpha(fraction);
                // Y方向向下移动300px的距离.
                view.setTranslationY(fraction * 300.0f);
            }
        };
        ValueAnimator mAnim = ValueAnimator.ofFloat(0f);
        mAnim.addUpdateListener(l);
        mAnim.setDuration(1000);
        mAnim.start();
    }

    // 2. 在自定义View内部用于引发重绘
    public class MyAnimationView extends View implements
            ValueAnimator.AnimatorUpdateListener {

        public MyAnimationView(Context context) {
            super(context);
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            // 手动触发界面重绘
            invalidate();
        }

    }

    private void animatorSet() {
//         AnimatorSet bouncer = new AnimatorSet();
//        bouncer.play(bounceAnim).before(squashAnim1);
//        bouncer.play(squashAnim1).with(squashAnim2);
//        bouncer.play(squashAnim1).with(stretchAnim1);
//        bouncer.play(squashAnim1).with(stretchAnim2);
//        bouncer.play(bounceBackAnim).after(stretchAnim2);
//        ValueAnimator fadeAnim = ObjectAnimator.ofFloat(newBall, "alpha", 1f,
//                0f);
//        fadeAnim.setDuration(250);
//        AnimatorSet animatorSet = new AnimatorSet();
//        animatorSet.play(bouncer).before(fadeAnim);
//        animatorSet.start();

    }

    private void keyframe() {
        // 设置在动画开始时,旋转角度为0度
        Keyframe kf0 = Keyframe.ofFloat(0f, 0f);
        // 设置在动画执行50%时,旋转角度为360度
        Keyframe kf1 = Keyframe.ofFloat(.5f, 360f);
        // 设置在动画结束时,旋转角度为0度
        Keyframe kf2 = Keyframe.ofFloat(0f, 0f);
        // 使用PropertyValuesHolder进行属性名称和值集合的封装
        PropertyValuesHolder pvhRotation = PropertyValuesHolder.ofKeyframe(
                "rotation", kf0, kf1, kf2);
        // 通过ObjectAnimator进行执行
        ObjectAnimator.ofPropertyValuesHolder(view, pvhRotation)
        // 设置执行时间(1000ms)
                .setDuration(1000)
                // 开始动画
                .start();
    }

    private void propertyValuesHolder() {
        // 获取view左边位置
        int left = -view.getWidth();
        // 获取view右边位置
        int right = 0;
        // 获取view上边位置
        int top = 100;
        // 获取view下边位置
        int bottom = top+view.getHeight();
        // 将view左边增加10像素
        PropertyValuesHolder pvhLeft = PropertyValuesHolder.ofInt("left", left,left + 1000);
        // 将view右边减少10像素
        PropertyValuesHolder pvhRight = PropertyValuesHolder.ofInt("right",right, right + 1000);
        // 将view的上边。。。
        PropertyValuesHolder pvhTop = PropertyValuesHolder.ofInt("top",top,top+500);
        // 将view的下边。。。
        PropertyValuesHolder pvhBottom = PropertyValuesHolder.ofInt("bottom",bottom,bottom+500);
        // 在X轴缩放从原始比例1f,缩小到最小0f,再放大到原始比例1f
        PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofFloat("scaleX",.3f, .8f, 1f);
        // 在Y轴缩放从原始比例1f,缩小到最小0f,再放大到原始比例1f
        PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofFloat("scaleY",.3f, .8f, 1f);
        // 将PropertyValuesHolder交付给ObjectAnimator进行构建
        ObjectAnimator customAnim = ObjectAnimator.ofPropertyValuesHolder(view,
                pvhLeft, pvhRight,pvhTop,pvhBottom,pvhScaleX, pvhScaleY);
        // 设置执行时间(1000ms)
        customAnim.setDuration(15000);
        // 开始动画
        customAnim.start();

        Toast.makeText(getApplicationContext(),customAnim.getCurrentPlayTime()+" ",Toast.LENGTH_SHORT).show();

        if(customAnim.getCurrentPlayTime()==5000){
            customAnim.cancel();
        }
        if(customAnim.getCurrentPlayTime()==10000){
            customAnim.end();
        }

    }

    private void valueAnimator() {
        // 通过静态方法构建一个ValueAnimator对象
        // 设置数值集合
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1000.0f);
        // 设置作用对象
        animator.setTarget(view);
        // 设置执行时间(1000ms)
        animator.setDuration(1000);
        // 添加动画更新监听
        animator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 获取当前值
                Float mValue = (Float) animation.getAnimatedValue();
                // 设置横向偏移量
                view.setTranslationX(mValue);
                // 设置纵向偏移量
                view.setTranslationY(mValue);
            }
        });
        // 开始动画
        animator.start();
    }

    private void objectAnimator() {
        // 通过静态方法构建一个ObjectAnimator对象
        // 设置作用对象、属性名称、数值集合
        ObjectAnimator.ofFloat(view, "translationX", -500F, 1000.0F)
        // 设置执行时间(1000ms)
                .setDuration(10000)
                // 开始动画
                .start();

        // 修改view的y属性, 从当前位置移动到300.0f
        ObjectAnimator yBouncer =

        ObjectAnimator.ofFloat(view, "translationY",
                200F, 1000.0f);

        yBouncer.setDuration(10000);
        // 设置插值器(用于调节动画执行过程的速度)
        yBouncer.setInterpolator(new BounceInterpolator());
        // 设置重复次数(缺省为0,表示不重复执行)
        yBouncer.setRepeatCount(1);
        // 设置重复模式(RESTART或REVERSE),重复次数大于0或INFINITE生效
        yBouncer.setRepeatMode(ValueAnimator.REVERSE);
        // 设置动画开始的延时时间(200ms)
        yBouncer.setStartDelay(200);
        // 开始动画
        yBouncer.start();
    }


    /**
     * 一个动画能够让View既可以放大、又能够移动，ObjectAnimator实现
     */
    private void mutiObjectAnimator(){
        ObjectAnimator anim = ObjectAnimator
                .ofFloat(view, "zhy", 0.5F,  1.0F)
                .setDuration(10000);
                anim.start();

        anim.addUpdateListener(new AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                float cVal = (Float) animation.getAnimatedValue();
                view.setAlpha(cVal);
                view.setScaleX(cVal);
                view.setScaleY(cVal);

                view.setTranslationX(cVal);
                view.setTranslationX(cVal);
            }
        });
    }

    /**
     * 轮船动画--前：移动放大10秒、中：上下8秒、后：前放到7秒
     */

    private void shipAnimatorSet(){
        //*******前动画*********
        // 获取view左边位置
        int left = -view.getWidth()+30;
        // 获取view右边位置
        int right = 30;
        // 获取view上边位置
        int top = 100;
        // 获取view下边位置
        int bottom = top+view.getHeight();
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
        ObjectAnimator anim1 = ObjectAnimator.ofPropertyValuesHolder(view,
                pvhLeft, pvhRight,pvhTop,pvhBottom,pvhScaleX, pvhScaleY);
        anim1.setDuration(7000);

        //********中动画********
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(view, "translationY",
                100);
        // 设置插值器(用于调节动画执行过程的速度)
        anim2.setInterpolator(new BounceInterpolator());
        // 设置重复次数(缺省为0,表示不重复执行)
        anim2.setRepeatCount(1);
        // 设置重复模式(RESTART或REVERSE),重复次数大于0或INFINITE生效
        anim2.setRepeatMode(ValueAnimator.REVERSE);
        // 设置动画开始的延时时间(200ms)
        anim2.setDuration(8000);

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
        ObjectAnimator anim3 = ObjectAnimator.ofPropertyValuesHolder(view,
                pvhLeft3, pvhRight3,pvhTop3,pvhBottom3,pvhScaleX3, pvhScaleY3);
        // 设置执行时间(1000ms)
        anim3.setDuration(7000);


        //********一起摇摆*******
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(anim1);
        animSet.play(anim2).after(anim1);
        animSet.play(anim3).after(anim2);
        animSet.start();
    }

    /**
     * 轮船前动画
     */


}
