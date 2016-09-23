package com.ok.utils.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.ok.utils.R;

/**
 * 自定义EditText,增加可以带右侧Clean图标功能
 */
public class GEditText extends EditText {
	private Drawable mRightDrawable;
	private Drawable[] drawables;
	private Drawable mRightDrawable_pressed;
	private boolean isHasFocus;

	public GEditText(Context context) {
		super(context);
		init();
	}

	public GEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray typeArray = getContext().obtainStyledAttributes(attrs, R.styleable.li_GEditText);
		Drawable drawable = typeArray.getDrawable(R.styleable.li_GEditText_li_drawableRightPressed);
		mRightDrawable_pressed = drawable;
		typeArray.recycle();
		init();
	}

	private void init() {
		drawables = this.getCompoundDrawables();
		//取得right位置的Drawable
		//即我们在布局文件中设置的android:drawableRight
		mRightDrawable = drawables[2];
		//设置焦点变化的监听
		this.setOnFocusChangeListener(new FocusChangeListenerImpl());
		//设置EditText文字变化的监听
		this.addTextChangedListener(new TextWatcherImpl());
		//初始化时让右边clean图标不可见
		setClearDrawableVisible(false);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (TextUtils.isEmpty(this.getText())) {
			return super.onTouchEvent(event);
		}
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if ((event.getX() > (getWidth() - getTotalPaddingRight()))
						&& (event.getX() < (getWidth() - getPaddingRight()) && event.getY() < getHeight() - getTotalPaddingBottom() && event.getY() > getTotalPaddingTop())) {
					setCompoundDrawablesWithIntrinsicBounds(getCompoundDrawables()[0], getCompoundDrawables()[1], mRightDrawable_pressed,
							getCompoundDrawables()[3]);

				}
				break;
			case MotionEvent.ACTION_UP:
				boolean isClean = (event.getX() > (getWidth() - getTotalPaddingRight()))
						&& (event.getX() < (getWidth() - getPaddingRight()) && event.getY() < getHeight() - getTotalPaddingBottom() && event.getY() > getTotalPaddingTop());
				if (isClean) {
					setText("");
				}
				else {
					setClearDrawableVisible(true);
				}
				break;
		}
		return super.onTouchEvent(event);
	}

	private class FocusChangeListenerImpl implements OnFocusChangeListener {
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			isHasFocus = hasFocus;
			if (isHasFocus) {
				boolean isVisible = getText().toString().length() >= 1;
				setClearDrawableVisible(isVisible);
			}
			else {
				setClearDrawableVisible(false);
			}
			
			if (gOnFocusChangeListener != null) {
				gOnFocusChangeListener.onFocusChange(v, hasFocus);
			}
		}

	}

	private GOnFocusChangeListener gOnFocusChangeListener;

	public void setGOnFocusChangeListener(GOnFocusChangeListener gOnFocusChangeListener) {
		this.gOnFocusChangeListener = gOnFocusChangeListener;
	}

	public interface GOnFocusChangeListener {
		public void onFocusChange(View v, boolean hasFocus);
	}

	//当输入结束后判断是否显示右边clean的图标
	private class TextWatcherImpl implements TextWatcher {
		@Override
		public void afterTextChanged(Editable s) {
			boolean isVisible = getText().toString().length() >= 1 && isHasFocus;
			setClearDrawableVisible(isVisible);
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {

		}

	}

	//隐藏或者显示右边clean的图标
	protected void setClearDrawableVisible(boolean isVisible) {
		Drawable rightDrawable;
		if (isVisible) {
			rightDrawable = mRightDrawable;
		}
		else {
			rightDrawable = null;
		}
		//使用代码设置该控件left, top, right, and bottom处的图标
		setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], rightDrawable, getCompoundDrawables()[3]);
	}

}
