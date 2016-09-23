/*
 * Copyright (C) 2013 Leszek Mzyk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ok.utils.widget;

import android.content.Context;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;


import com.ok.utils.utils.log.LogUtils;

import java.lang.reflect.Field;


/**
 * A ViewPager subclass enabling infinte scrolling of the viewPager elements
 * 
 * When used for paginating views (in opposite to fragments), no code changes
 * should be needed only change xml's from <android.support.v4.view.ViewPager>
 * to <com.imbryk.viewPager.LoopViewPager>
 * 
 * If "blinking" can be seen when paginating to first or last view, simply call
 * seBoundaryCaching( true ), or change DEFAULT_BOUNDARY_CASHING to true
 * 
 * When using a FragmentPagerAdapter or FragmentStatePagerAdapter,
 * additional changes in the adapter must be done. 
 * The adapter must be prepared to create 2 extra items e.g.:
 * 
 * The original adapter creates 4 items: [0,1,2,3]
 * The modified adapter will have to create 6 items [0,1,2,3,4,5]
 * with mapping realPosition=(position-1)%count
 * [0->3, 1->0, 2->1, 3->2, 4->3, 5->0] 
 */
public class LoopViewPager extends ViewPagerModify {

	public static final String TAG = LoopViewPager.class.getSimpleName();

	OnPageChangeListener mOuterPageChangeListener;
	private LoopPagerAdapterWrapper mAdapter;
	private PagerAdapter originAdapter;
	/**
	 * helper function which may be used when implementing FragmentPagerAdapter
	 *   
	 * @param position
	 * @param count
	 * @return (position-1)%count
	 */
	public static int toRealPosition(int position, int count) {
		position = position - 2;
		if(position < 0) {
			position += count;
		} else {
			position = position % count;
		}
		return position;
	}

	/**
	 * If set to true, the boundary views (i.e. first and last) will never be destroyed
	 * This may help to prevent "blinking" of some views 
	 * 
	 * @param flag
	 */
	public void setBoundaryCaching(boolean flag) {
		if(mAdapter != null) {
			mAdapter.setBoundaryCaching(flag);
		}

	}

	@Override
	public void setAdapter(PagerAdapter adapter) {
		if(adapter != null && (adapter.getCount() == 1 || adapter.getCount() == 0)) {
			originAdapter = adapter;
			super.setAdapter(originAdapter);
		} else {
			mAdapter = new LoopPagerAdapterWrapper(adapter);
			mAdapter.setBoundaryCaching(false);
			super.setAdapter(mAdapter);
			getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
				@SuppressWarnings("deprecation")
				@Override
				public void onGlobalLayout() {
					triggerPageTransformer();
					getViewTreeObserver().removeGlobalOnLayoutListener(this);
				}
			});
			setCurrentItem(0, false);
		}
	}

	@Override
	public PagerAdapter getAdapter() {
		if(originAdapter != null && (originAdapter.getCount() == 1 || originAdapter.getCount() == 0)) {
			return originAdapter;
		}
		return mAdapter != null ? mAdapter.getRealAdapter() : mAdapter;
	}

	@Override
	public int getCurrentItem() {
		if(originAdapter != null && originAdapter.getCount() == 1) {
			return 0;
		}
		return mAdapter != null ? mAdapter.toRealPosition(super.getCurrentItem()) : 0;
	}

	public int getCurrentRealItem() {
		if(originAdapter != null && originAdapter.getCount() == 1) {
			return 0;
		}
		return super.getCurrentItem();
	}

	public void setCurrentItem(int item, boolean smoothScroll) {
		if(originAdapter != null && originAdapter.getCount() == 1) {
			super.setCurrentItem(0, smoothScroll);
			return;
		}
		if(mAdapter != null) {
			int realItem = mAdapter.toInnerPosition(item);
			LogUtils.d(TAG, " loopviewPager current item " + item + " realItem " + realItem);
			super.setCurrentItem(realItem, smoothScroll);
		}
		if(!smoothScroll) {
			triggerPageTransformer();
		}
	}

	public void triggerPageTransformer() {
		if(pageTransformer != null) {
			LogUtils.d(TAG, "trigger pagetransformer");
			final int scrollX = getScrollX();
			final int childCount = getChildCount();
			for (int i = 0; i < childCount; i++) {
				final View child = getChildAt(i);
				final LayoutParams lp = (LayoutParams) child.getLayoutParams();
				if(lp.isDecor)
					continue;
				final float transformPos = (float) (child.getLeft() - scrollX) / getClientWidth();
				pageTransformer.transformPage(child, transformPos);
			}
		}
	}

	private int getClientWidth() {
		return getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
	}

	@Override
	public void setCurrentItem(int item) {
		if(originAdapter != null && originAdapter.getCount() == 1) {
			super.setCurrentItem(0);
			return;
		}
		if(getCurrentItem() != item) {
			setCurrentItem(item, true);
		}
	}

	public void setCurrentRealItem(int item) {
		if(originAdapter != null && originAdapter.getCount() == 1) {
			super.setCurrentItem(0);
			return;
		}
		if(getCurrentItem() != item) {
			super.setCurrentItem(item, true);
		}
	}

	@Override
	public void setOnPageChangeListener(OnPageChangeListener listener) {
		mOuterPageChangeListener = listener;
	};

	public LoopViewPager(Context context) {
		super(context);
		init();
	}

	public LoopViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		super.setOnPageChangeListener(onPageChangeListener);
	}

	private PageTransformer pageTransformer;

	@Override
	public void setPageTransformer(boolean arg0, PageTransformer arg1) {
//		super.setPageTransformer(arg0, arg1);
		pageTransformer = arg1;
		if(Build.VERSION.SDK_INT >= 11) {
			super.setPageTransformer(arg0, arg1);
		} else {
			try {
				Field field = ViewPagerModify.class.getDeclaredField("mPageTransformer");
				field.setAccessible(true);
				field.set(this, arg1);
			} catch ( Exception e ) {
				e.printStackTrace();
			}
		}

	}

	private OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {
		private float mPreviousOffset = -1;
		private float mPreviousPosition = -1;

		@Override
		public void onPageSelected(int position) {
			if(originAdapter != null && originAdapter.getCount() == 1) {
				return;
			}
			int realPosition = mAdapter.toRealPosition(position);
			if(mPreviousPosition != realPosition) {
				mPreviousPosition = realPosition;
				if(mOuterPageChangeListener != null) {
					mOuterPageChangeListener.onPageSelected(realPosition);
				}
			}
		}

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			if(originAdapter != null && originAdapter.getCount() == 1) {
				return;
			}
			int realPosition = position;
			if(mAdapter != null) {
				realPosition = mAdapter.toRealPosition(position);

				if(positionOffset == 0 && mPreviousOffset == 0
						&& (position == 1 || position == mAdapter.getCount() - 2 || position == 0 || position == mAdapter.getCount() - 1)) {
					LogUtils.d("LoopViewPager", "resetCurrent item " + position + "->" + realPosition);
					setCurrentItem(realPosition, false);
				}
			}

			mPreviousOffset = positionOffset;
			if(mOuterPageChangeListener != null && mAdapter != null) {
				if(realPosition != mAdapter.getRealCount() - 2) {
					mOuterPageChangeListener.onPageScrolled(realPosition, positionOffset, positionOffsetPixels);
				} else {
					if(positionOffset > .5) {
						mOuterPageChangeListener.onPageScrolled(1, 0, 0);
					} else {
						mOuterPageChangeListener.onPageScrolled(realPosition, 0, 0);
					}
				}
			}
		}

		@Override
		public void onPageScrollStateChanged(int state) {
			if(originAdapter != null && originAdapter.getCount() == 1) {
				return;
			}
			if(mAdapter != null) {
				int position = LoopViewPager.super.getCurrentItem();
				int realPosition = mAdapter.toRealPosition(position);
				if(state == ViewPagerModify.SCROLL_STATE_IDLE
						&& (position == 1 || position == 0 || position == mAdapter.getCount() - 1 || position == mAdapter.getCount() - 2)) {
					LogUtils.d("LoopViewPager", "resetCurrent item " + position + "->" + realPosition);
					setCurrentItem(realPosition, false);
				}
			}
			if(mOuterPageChangeListener != null) {
				mOuterPageChangeListener.onPageScrollStateChanged(state);
			}
		}
	};

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		try {
			return super.onTouchEvent(e);
		} catch ( IllegalArgumentException ex ) {
			ex.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent e) {
		try {
			return super.onInterceptTouchEvent(e);
		} catch ( IllegalArgumentException ex ) {
			ex.printStackTrace();
		}
		return false;
	}
}
