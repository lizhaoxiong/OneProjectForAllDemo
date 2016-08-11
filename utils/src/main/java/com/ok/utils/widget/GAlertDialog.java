package com.ok.utils.widget;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ok.utils.R;
import com.ok.utils.utils.log.LogUtils;


/**
 * 自定义AlertDialog
 */
public class GAlertDialog extends Dialog implements View.OnClickListener {

	private LinearLayout mLlContainer;
	private TextView mTvTitle;
	private TextView mTvSubTitle;
	private TextView mTvMessage;
	private GButton mBtnPositive;
	private GButton mBtnNegative;
//	private View mLeftView;
	private View mMiddleView;
//	private View mRightView;
	private TextView mBottomTextView;
	/** Dialog 内容的布局 */
	private LinearLayout mContentLayout ;
	/** 内容区与按钮区的水平分割线 */
	private View mHorMidView ;


	private OnDialogDismissCallBack mDismissCallBack;
	private OnClickListener mOnClickListener;

	public GAlertDialog(Context context) {
		this(context, 0);
	}

	@SuppressLint("InflateParams")
	public GAlertDialog(Context context, int theme) {
		super(context, R.style.li_gAlertDialogTheme);

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.li_dialog_alert, null);
		setContentView(layout);

		mLlContainer = (LinearLayout) layout.findViewById(R.id.ll_container);
		mTvTitle = ((TextView) layout.findViewById(R.id.tv_title));
		mTvSubTitle = ((TextView) layout.findViewById(R.id.tv_sub_title));
		mTvMessage = ((TextView) layout.findViewById(R.id.tv_message));
		mBtnPositive = (GButton) layout.findViewById(R.id.li_button_ok);
		mBtnNegative = (GButton) layout.findViewById(R.id.li_button_cancel);
		mBottomTextView = (TextView) layout.findViewById(R.id.bottom_text);
		mMiddleView =  layout.findViewById(R.id.button_middle_view);
		mHorMidView = layout.findViewById(R.id.view_alert_mid);
		mBtnPositive.setOnClickListener(this);
		mBtnNegative.setOnClickListener(this);
		mTvMessage.setMovementMethod(LinkMovementMethod.getInstance());
		mContentLayout = (LinearLayout) layout.findViewById(R.id.layout_alert_dialog_content);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void show() {
		if (mBtnNegative.getVisibility() == View.GONE && mBtnPositive.getVisibility() == View.VISIBLE) {
			LinearLayout.LayoutParams p = (LinearLayout.LayoutParams) mBtnPositive.getLayoutParams();
//			p.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
//			p.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE + 1);
			p.gravity = Gravity.CENTER_HORIZONTAL;
			p.weight = 4;
			mBtnPositive.setLayoutParams(p);
			mBtnPositive.setBackgroundResource(R.drawable.li_btn_alert_dialog_single_selector);
			mMiddleView.setVisibility(View.GONE);
			hidePlaceHolderView();
		}
		else if (mBtnPositive.getVisibility() == View.GONE && mBtnNegative.getVisibility() == View.VISIBLE) {
			LinearLayout.LayoutParams p = (LinearLayout.LayoutParams) mBtnNegative.getLayoutParams();
//			p.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
//			p.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE + 1);
			p.gravity = Gravity.CENTER_HORIZONTAL;
			p.weight = 4;
			mBtnNegative.setLayoutParams(p);
			mBtnNegative.setBackgroundResource(R.drawable.li_btn_alert_dialog_single_selector);
			hidePlaceHolderView();
		}
		else if(mBtnNegative.getVisibility() == View.GONE && mBtnPositive.getVisibility() == View.GONE) {
			mMiddleView.setVisibility(View.GONE);
			mHorMidView.setVisibility(View.GONE);
			mContentLayout.setBackgroundResource(R.drawable.li_bg_alert_no_buttondialog);
		}
		super.show();
		getWindow().setWindowAnimations(R.style.li_AlertAnim);

		WindowManager m = getWindow().getWindowManager();
		Display display = m.getDefaultDisplay(); //  获取屏幕宽、高用
		DisplayMetrics dm = new DisplayMetrics();
		display.getMetrics(dm);
		WindowManager.LayoutParams params = getWindow().getAttributes(); // 获取对话框当前的参数值
		float scale = 0.7f;
		if (dm.widthPixels < 720) {
			scale = 0.8f;
		}
		params.width = (int) (display.getWidth() * scale); //
//		params.width = (int) (UIUtils.getScreenWidth() - Utils.dip2px(getContext(), 85)); // 宽度设置
		getWindow().setAttributes(params);
	}

	private void hidePlaceHolderView() {
//		LinearLayout.LayoutParams leftparams = (LinearLayout.LayoutParams)mLeftView.getLayoutParams();
//		leftparams.weight = 4;
//		mLeftView.setLayoutParams(leftparams);
//		mMiddleView.setVisibility(View.GONE);
//		LinearLayout.LayoutParams rightparams = (LinearLayout.LayoutParams)mRightView.getLayoutParams();
//		rightparams.weight = 4;
//		mLeftView.setLayoutParams(rightparams);
	}

	public View getPositiveBtn(){
		return mBtnPositive;
	}

	public View getNegativeBtn(){
		return mBtnNegative;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.li_button_ok) {
			if (mOnClickListener != null) {
				mOnClickListener.onClick(GAlertDialog.this, DialogInterface.BUTTON_POSITIVE);
			}
			dismiss();
		}
		else if (id == R.id.li_button_cancel) {
			if (mOnClickListener != null) {
				mOnClickListener.onClick(GAlertDialog.this, DialogInterface.BUTTON_NEGATIVE);
			}
			dismiss();
		}
		else if (id== R.id.li_button_close) {
			dismiss();
		}
	}

	public void setTitle(CharSequence title) {
		if (TextUtils.isEmpty(title)) {
			mTvTitle.setVisibility(View.GONE);
		}
		else {
			mTvTitle.setText(title);
			mTvTitle.setVisibility(View.VISIBLE);
		}
	}

	private static final String END_OF_LINE = "\r\n";
	public void setMessage(CharSequence message) {
		String str = message.toString();

		int index = str.indexOf(END_OF_LINE);
		String subTitle = null;
		if (index > 0 && ((subTitle = str.substring(0, index)).endsWith(":") || subTitle.endsWith("："))) {
			mTvSubTitle.setText(subTitle);
			mTvSubTitle.setVisibility(View.VISIBLE);
			mTvMessage.setText(str.substring(index + END_OF_LINE.length()));
		}
		else{
			mTvSubTitle.setVisibility(View.GONE);
			mTvMessage.setText(toDBC(message.toString()));
		}
	}
	public void setMessageCenter(){
		LogUtils.e("alertDialog","center===");
		mTvMessage.setGravity(Gravity.CENTER_HORIZONTAL);
	}

	public void setMessageLeft(){
		mTvMessage.setGravity(Gravity.LEFT);
	}

	/**
	 * 半角转换为全角
	 *
	 * @param input
	 * @return
	 */
	public String toDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}


	public void setView(View view) {
		mTvTitle.setVisibility(View.GONE);
		mTvSubTitle.setVisibility(View.GONE);
		mTvMessage.setVisibility(View.GONE);
		mLlContainer.addView(view, 0);
	}

	public void setPositiveBtn(CharSequence text) {
		if (!TextUtils.isEmpty(text)) {
			mBtnPositive.setText(text);
		}
		else {
			mBtnPositive.setVisibility(View.GONE);
		}
	}

	public void setNegativeBtn(CharSequence text) {
		if (!TextUtils.isEmpty(text)) {
			mBtnNegative.setText(text);
		}
		else {
			mBtnNegative.setVisibility(View.GONE);
		}
	}

	public void setNegetiveBtn(){
		mBtnNegative.setVisibility(View.GONE);
	}

	public void setOnClickListener(OnClickListener onClickListener) {
		mOnClickListener = onClickListener;
	}

	public void setOnDismissCallBack(OnDialogDismissCallBack dismissCallBack) {
		this.mDismissCallBack = dismissCallBack;
	}

	public void setBottomText(CharSequence text){
		this.mBottomTextView.setText(text);
	}

	@Override
	public void dismiss() {
		super.dismiss();
		if (mDismissCallBack != null) {
			this.mDismissCallBack.dismiss();
		}
	}

	/**
	 * Helper class for creating a custom dialog
	 */
	public static class Builder {
		private Context mContext;
		private CharSequence mTitle;
		private CharSequence mMessage;
		private CharSequence mPositiveButtonText;
		private CharSequence mNegativeButtonText;
		private CharSequence mBottomText;
		private boolean mCancelable = true;
		private boolean mIsCanceledOnTouchOutside;

		private OnDialogDismissCallBack mDismissCallBack;
		private OnClickListener mOnClickListener;

		public Builder(Context context) {
			this.mContext = context;
		}

		/**
		 * Set the Dialog title from String
		 * @param title
		 * @return
		 */
		public Builder setTitle(CharSequence title) {
			this.mTitle = title;
			return this;
		}

		public Builder setBottomText(CharSequence bottomText){
			mBottomText = bottomText;
			return this;
		}

		/**
		 * Set the Dialog message from String
		 * @param message
		 * @return
		 */
		public Builder setMessage(CharSequence message) {
			this.mMessage = message;
			return this;
		}

		/**
		 * Set the Dialog message from resource
		 * @param message
		 * @return
		 */
		public Builder setMessage(int message) {
			this.mMessage = (String) mContext.getText(message);
			return this;
		}

		/**
		 * Set the Dialog view
		 * @param cancelable
		 * @return
		 */
		public Builder setCancelable(boolean cancelable) {
			this.mCancelable = cancelable;
			return this;
		}

		/**
		 * Set the positive button resource and it's listener
		 * @param positiveButtonText
		 * @return
		 */
		public Builder setPositiveButton(CharSequence positiveButtonText) {
			this.mPositiveButtonText = positiveButtonText;
			return this;
		}

		/**
		 * Set the negative button text and it's listener
		 * @param negativeButtonText
		 * @return
		 */
		public Builder setNegativeButton(CharSequence negativeButtonText) {
			this.mNegativeButtonText = negativeButtonText;
			return this;
		}

		public Builder setOnClickListener(OnClickListener onClickListener) {
			mOnClickListener = onClickListener;
			return this;
		}

		public Builder setDissmissCallBack(OnDialogDismissCallBack callBack) {
			this.mDismissCallBack = callBack;
			return this;
		}

		public Builder setCanceledOnTouchOutside(boolean isCanceledOnTouchOutside) {
			this.mIsCanceledOnTouchOutside = isCanceledOnTouchOutside;
			return this;
		}

		/**
		 * Create the custom dialog
		 */
		public GAlertDialog create() {
			final GAlertDialog dialog = new GAlertDialog(mContext, R.style.li_gAlertDialogTheme);
			dialog.setTitle(mTitle);
			dialog.setBottomText(mBottomText);
			dialog.setMessage(mMessage);
			dialog.setPositiveBtn(mPositiveButtonText);
			dialog.setNegativeBtn(mNegativeButtonText);
			dialog.setOnClickListener(mOnClickListener);
			dialog.setOnDismissCallBack(mDismissCallBack);
			dialog.setCancelable(mCancelable);
			dialog.setCanceledOnTouchOutside(mIsCanceledOnTouchOutside);
			return dialog;
		}

		public GAlertDialog show() {
			GAlertDialog dialog = create();
			dialog.show();
			return dialog;
		}
		public GAlertDialog showCenter() {
			GAlertDialog dialog = create();
			dialog.setMessageCenter();
			dialog.show();
			return dialog;
		}
	}

	//当对话框关闭时回调的接口
	public interface OnDialogDismissCallBack {
		public void dismiss();
	}
}