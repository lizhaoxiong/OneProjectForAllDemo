package com.ok.utils.widget;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

/**
 * <p>Copyright: Copyright (c) 2016</p>
 * <p/>
 * <p>One hundred thousand</p>
 *
 * @author li zhaoxiong
 * @version 1.0.0
 * @description
 * @modify
 */
public class ZoomImageView extends ImageView implements ViewTreeObserver.OnGlobalLayoutListener,
        ScaleGestureDetector.OnScaleGestureListener ,View.OnTouchListener{
    private final Matrix mScaleMatrix;
    private final ScaleGestureDetector mScaleGestureDetector;
    private boolean mOnce = false;//初始化的操作只进行一次


    /**
     * 初始化时缩放的值，缩放的最小值
     */
    private float mInitScale;
    /**
     * 双击放大的值
     */
    private float mMinScale;
    /**
     * 放大的最大值
     */
    private float mMaxScale;

    /**
     * 捕获用户多点触控的比例
     * @param context
     */
//    private float
    public ZoomImageView(Context context) {
        this(context,null);
    }

    public ZoomImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ZoomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs,defStyleAttr);
        //intr();
        mScaleMatrix = new Matrix();
        setScaleType(ScaleType.MATRIX);
        mScaleGestureDetector = new ScaleGestureDetector(context, this);
        setOnTouchListener(this);
    }

    /**
     * 当View加载到窗口会调用该方法
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        //注册该监听
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    /**
     * 当View从窗口移除时会调用该方法
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        //移除改监听
        getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    /**
     * 全局的布局完成后会调用该方法
     */
    @Override
    public void onGlobalLayout() {
        if(!mOnce){
            //得到控件的宽高
            int width = getWidth();
            int height = getHeight();

            Drawable d = getDrawable();
            if(d==null){
                return;
            }
            int dh = d.getIntrinsicHeight();
            int dw = d.getIntrinsicWidth();

            float scale = 1.0f;
            if(dw>width&&dh<height){
                scale = width*1.0f/dw;
            }
            if(dh>height&&dw<width){
                scale = height*1.0f/dh;
            }

            if((dw>width&&dh>height) || (dw<width&&dh<height)){
                scale = Math.min(width*1.0f/dw,height*1.0f/dw);
            }

            /**
             * 得到初始化时的缩放比例
             */
            mInitScale =scale;
            mMinScale = mInitScale*2;
            mMaxScale = mInitScale*4;

            /**
             * 将图片移动到屏幕的中心
             */
            int dx = width/2 - dw/2;
            int dy = height/2 - dh/2;

            mScaleMatrix.postTranslate(dx,dy);
            mScaleMatrix.postScale(mInitScale,mInitScale,width/2,height/2  //缩放中心点
            );
            setImageMatrix(mScaleMatrix);

            mOnce = true;
        }
    }

    /**
     * 获取当前图片的缩放值
     */
    public float getScale(){
        float[] values = new float[9];
        mScaleMatrix.getValues(values);
        return values[Matrix.MSCALE_X];
    }

    /**
     * 缩放区间：intScale maxScale
     * @param detector
     * @return
     */
    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        //拿到当前缩放的值
        float scale = getScale();
        float scaleFactor = detector.getScaleFactor();//scaleFactor<1.0f代表你要缩小，>1.0f代表你要放大
        if (getDrawable()==null)
            return true;

        //缩放范围的控制
        if((scale<mMaxScale&&scaleFactor>1.0f) || (scale>mInitScale&&scaleFactor<1.0f)){
            //边界控制
            if(scale*scaleFactor<mInitScale){
                scaleFactor = mInitScale/scale;
            }
            if(scale*scaleFactor>mMaxScale){
                scaleFactor = mMaxScale/scale;
            }

            //缩放
            mScaleMatrix.postScale(scaleFactor,scaleFactor,getWidth()/2,getHeight()/2);
            setImageMatrix(mScaleMatrix);
        }

        return true;//保证事件能够继续
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }

    /**
     * 捕获触摸事件,绑定ScaleGestureDetector,就能拿到当前手指触摸的值
     * @param v
     * @param event
     * @return
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mScaleGestureDetector.onTouchEvent(event);
        return true;
    }
}
