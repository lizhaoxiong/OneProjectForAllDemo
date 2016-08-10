package com.ok.utils.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

import java.util.ArrayList;

/**
 * <p>Copyright: Copyright (c) 2016</p>
 *
 * <p>Company: 浙江齐聚科技有限公司<a href="www.guagua.cn">www.guagua.cn</a></p>
 *
 * @description  自定义标题栏
 *
 * @author TPX
 * @modify
 * @version 1.0.0
 */
public class LiveRefreshView extends BaseRefreshView implements Animatable {

    /** 动画间隔 */
    private static final int ANIMATION_DURATION = 1000;
    /** 天空图片的比例 */
    private final static float SKY_RATIO = 0.65f;
    /** 插值器 */
    private static final Interpolator LINEAR_INTERPOLATOR = new LinearInterpolator();
    private static final String TAG = "LiveRefreshView";
    /** 父布局 */
    private PullToRefreshView mParent;
    private Matrix mMatrix;
    /** 动画对象 */
    private Animation mAnimation;
    /** View的高度位置 */
    private int mTop;
    private int mScreenWidth;

    private int mSkyHeight;
    private float mSunTopOffset;

    private float mPercent = 0.0f;
    private ArrayList<Bitmap> mCatBitmapList = new ArrayList<>();
    private boolean isRefreshing = false;

    public LiveRefreshView(Context context, final PullToRefreshView parent) {
        super(context, parent);
        mParent = parent;
        mMatrix = new Matrix();

        setupAnimations();
        parent.post(new Runnable() {
            @Override
            public void run() {
                initiateDimens(parent.getWidth());
            }
        });
    }

    /**
     * 根据父布局的宽度,初始化尺寸
     * @param viewWidth
     */
    public void initiateDimens(int viewWidth) {
        //这里判断，如果尺寸小于0不合法，或和屏幕尺寸相同，则返回
        if (viewWidth <= 0 || viewWidth == mScreenWidth) return;
        //屏幕宽设为父布局宽
        mScreenWidth = viewWidth;
        /** 天空图片的高度，为屏幕宽度的0.65比例 */
        mSkyHeight = (int) (SKY_RATIO * mScreenWidth);
        //太阳的上边距
        mSunTopOffset = (mParent.getTotalDragDistance() * 0.1f);
        //上边距，最大的滑动距离
        mTop = -mParent.getTotalDragDistance();
        createBitmaps();
    }
    //创建Bitmap
    private void createBitmaps() {
        for (int i = 1; i < 7 ;i++) {
            Bitmap bm = BitmapFactory.decodeResource(getContext().getResources(),getCatResource(i));
//             bm = Bitmap.createScaledBitmap(bm, mScreenWidth, mSkyHeight, true);
            mCatBitmapList.add(bm);
        }
    }

    private int getCatResource(int id) {
        return getContext().getResources().getIdentifier("li_refresh_cat_" + id, "drawable", getContext().getPackageName());
    }
    /**
     * 设置进度
     * */
    @Override
    public void setPercent(float percent, boolean invalidate) {
        setPercent(percent);
        if (invalidate) setRotate(percent);
    }

    @Override
    public void offsetTopAndBottom(int offset) {
        mTop += offset;
        invalidateSelf();
    }

    @Override
    public void draw(Canvas canvas) {
        if (mScreenWidth <= 0) return;

        final int saveCount = canvas.save();

        canvas.translate(0, mTop);
        canvas.clipRect(0, -mTop, mScreenWidth, mParent.getTotalDragDistance());

        drawCat(canvas);
        canvas.restoreToCount(saveCount);
    }

    private void drawCat(Canvas canvas) {
        Matrix matrix = mMatrix;
        matrix.reset();

        float dragPercent = Math.min(1f, Math.abs(mPercent));
        Log.d(TAG,"CLASS " + TAG + "FNC drawCat(),dragPercent:" + dragPercent);
        int index = (int)(dragPercent * 10) - 5 ;
        index = index < 0 ? 0:index ;
        index = index > 5 ? 5:index ;
        float offsetX = 0.5f * (float) mScreenWidth - mCatBitmapList.get(0).getWidth() / 2;;
        float offsetY = mSunTopOffset
                + (mParent.getTotalDragDistance() / 2) * (1.0f - dragPercent) // Move the sun up
                - mTop - dragPercent * 40; // Depending on Canvas position
        matrix.postTranslate(offsetX, offsetY);
        canvas.drawBitmap(mCatBitmapList.get(index), matrix, null);
    }
    /**
     * 设置进度
     * @param percent
     */
    public void setPercent(float percent) {
        mPercent = percent;
    }

    /**
     * 设置图片的角度
     * @param rotate
     */
    public void setRotate(float rotate) {
        //重绘
        invalidateSelf();
    }

    public void resetOriginals() {
        setPercent(0);
        setRotate(0);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, mSkyHeight + top);
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
    }
    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    public void start() {
        mAnimation.reset();
        isRefreshing = true;
        mParent.startAnimation(mAnimation);
    }

    @Override
    public void stop() {
        mParent.clearAnimation();
        isRefreshing = false;
        resetOriginals();
    }

    /**
     * 创建动画
     */
    private void setupAnimations() {
        mAnimation = new Animation() {
            @Override
            public void applyTransformation(float interpolatedTime, Transformation t) {
                setRotate(interpolatedTime);
            }
        };
        mAnimation.setRepeatCount(Animation.INFINITE);
        mAnimation.setRepeatMode(Animation.RESTART);
        mAnimation.setInterpolator(LINEAR_INTERPOLATOR);
        mAnimation.setDuration(ANIMATION_DURATION);
    }

}
