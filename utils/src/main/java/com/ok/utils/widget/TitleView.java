package com.ok.utils.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ok.utils.R;

/**
 * <p>Copyright: Copyright (c) 2016</p>
 *
 * <p>Company: 浙江齐聚科技有限公司<a href="www.guagua.cn">www.guagua.cn</a></p>
 *
 * @description  自定义标题栏
 *
 * @author 薛文超
 * @modify
 * @version 1.0.0
 */
public class TitleView extends RelativeLayout {
	private ImageButton mImageButtonLeft;
	private ImageButton mImageButtonRight;
	private TextView mTitleTextView;

	public TitleView(Context context) {
		super(context);
	}

	public TitleView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TitleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		mImageButtonLeft = (ImageButton) findViewById(R.id.title_button_left);
		mImageButtonRight = (ImageButton) findViewById(R.id.title_button_right);
		mTitleTextView = (TextView) findViewById(R.id.title_text);
	}

	public ImageButton getButtonLeft() {
		return mImageButtonLeft;
	}

	public ImageButton getButtonRight() {
		return mImageButtonRight;
	}

	public TextView getTitleTextView() {
		return mTitleTextView;
	}
	
	public void setTitle(CharSequence title) {
		if (mTitleTextView != null) {
			mTitleTextView.setText(title);
		}
	}

	private int leftSpace;

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		//必须放在super.onLayout之前，否则三星的部分手机会一直触发requestLayout
		if (mTitleTextView != null) {
			int lbw = 0;//左边按钮宽度(右边界)
			if (mImageButtonLeft != null && mImageButtonLeft.getVisibility() == View.VISIBLE) {
				lbw = mImageButtonLeft.getRight();
			}
			int rbw = 0;//右边按钮宽度(左边界)
			if (mImageButtonRight != null && mImageButtonRight.getVisibility() == View.VISIBLE) {
				rbw = r - mImageButtonRight.getLeft();
			}
			int leftSpace = (r - l) - (rbw > lbw ? rbw : lbw) * 2;
			if (leftSpace > 0 && leftSpace != this.leftSpace) {
				this.leftSpace = leftSpace;
				mTitleTextView.setMaxWidth(leftSpace);
			}
		}
		super.onLayout(changed, l, t, r, b);
	}
}
