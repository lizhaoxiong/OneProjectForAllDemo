package com.ok.utils.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.ok.utils.R;

import java.util.ArrayList;


/**
 * 弹出菜单封装，默认从底部弹出
 * 菜单项有三种背景可选：黑、灰、红
 */
public class GBottomMenu extends Dialog {

	public interface GPopupMenuListener {
		public void onMenuSelected(MenuItem item);
	}

	/**
	 * 每个菜单项封装
	 */
	public static class MenuItem {
		//背景颜色类型
		public static final int BACKGROUND_BLACK = 1;
		public static final int BACKGROUND_GRAY = 2;
		public static final int BACKGROUND_RED = 3;

		private Object tag;//存储需要回传的数据
		private CharSequence action;
		private GPopupMenuListener listener;
		private boolean enable = true;;
		private int backgroundType = BACKGROUND_GRAY;//背景类型

		public MenuItem(CharSequence action, Object tag, GPopupMenuListener listener) {
			this.action = action;
			this.listener = listener;
			this.tag = tag;
		}

		public MenuItem(CharSequence action, int backgroundType, Object tag, GPopupMenuListener listener) {
			this(action, tag, listener);
			this.backgroundType = backgroundType;
		}

		public MenuItem(CharSequence action, Object tag, boolean enable, GPopupMenuListener listener) {
			this(action, tag, listener);
			this.enable = enable;
		}

		public Object getTag() {
			return this.tag;
		}
		
		public CharSequence getAction() {
			return this.action;
		}
	}

	private Context context;
	private ArrayList<MenuItem> actionList;

	public GBottomMenu(Context context, ArrayList<MenuItem> actionList) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setCanceledOnTouchOutside(true);
		this.context = context;
		this.actionList = actionList;
		init();
	}

	public GBottomMenu(Context context, int theme, ArrayList<MenuItem> actionList) {
		this(context, actionList);
	}

	private LinearLayout menuContainer;

	/**
	 * 构造菜单列表
	 */
	private void init() {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.li_bottom_menu, null);
		menuContainer = (LinearLayout) view.findViewById(R.id.menu_container);

		Button menuButton;
		Resources resources = context.getResources();
		for (int i = 0, n = actionList.size(); i < n; i++) {
			final MenuItem menuItem = actionList.get(i);
			menuButton = (Button) inflater.inflate(R.layout.li_bottom_menu_item, null);
			//设置菜单项间间隙
			if (i > 0) {
				LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				params.topMargin = (int) resources.getDimension(R.dimen.li_bottom_menu_item_divider);
				menuButton.setLayoutParams(params);
			}
			//设置文字
			menuButton.setText(menuItem.action);
			menuButton.setEnabled(menuItem.enable);
			
			//设置背景
			if (menuItem.backgroundType == MenuItem.BACKGROUND_BLACK) {//黑背景
				menuButton.setBackgroundResource(R.drawable.li_bg_bottom_menu_item_black_selector);
				menuButton.setTextColor(Color.WHITE);
			}
			else {
				menuButton.setTextColor(Color.BLACK);
				if (menuItem.backgroundType == MenuItem.BACKGROUND_RED) {//红背景（因需求，暂时不使用红色，全部改为灰色 2013年3月7号）
					menuButton.setBackgroundResource(R.drawable.li_bg_bottom_menu_item_gray_selector);
				}
				else {//灰背景
					menuButton.setBackgroundResource(R.drawable.li_bg_bottom_menu_item_gray_selector);
				}
			}
			menuButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (menuItem.listener != null) {
						menuItem.listener.onMenuSelected(menuItem);
					}
					dismiss();
				}
			});
			menuContainer.addView(menuButton);
		}

		view.findViewById(R.id.popup_menu_btn_cancel).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});

		setContentView(view);
	}

	public void dismiss() {
		actionList.clear();
		super.dismiss();
	}

	public void show() {
		Window window = getWindow();
		window.setWindowAnimations(R.style.li_bottomWindowAnim); //设置窗口弹出动画
		window.setBackgroundDrawableResource(android.R.color.transparent); //设置对话框背景为透明
		WindowManager.LayoutParams wl = window.getAttributes();
		//窗口需要显示的位置
		wl.gravity = Gravity.BOTTOM;
		wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
		wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		window.setAttributes(wl);
		super.show();
	}

	/**
	 * 菜单构造器
	 */
	public static class Builder {
		private Context context;
		private ArrayList<MenuItem> actionList;

		public Builder(Context context) {
			this.context = context;
			this.actionList = new ArrayList<MenuItem>();
		}

		public Builder appendMenuItem(CharSequence action, Object tag, GPopupMenuListener listener) {
			actionList.add(new MenuItem(action, tag, listener));
			return this;
		}

		public Builder appendMenuItem(CharSequence action, int backgroundType, Object tag, GPopupMenuListener listener) {
			actionList.add(new MenuItem(action, backgroundType, tag, listener));
			return this;
		}

		public Builder appendMenuItem(CharSequence action, Object tag, boolean enable, GPopupMenuListener listener) {
			actionList.add(new MenuItem(action, tag, enable, listener));
			return this;
		}

		/**
		 * Create the custom dialog
		 */
		private GBottomMenu create() {
			GBottomMenu dialog = new GBottomMenu(context, actionList);
			return dialog;
		}

		public GBottomMenu show() {
			GBottomMenu dialog = create();
			dialog.show();
			return dialog;
		}
	}
}
