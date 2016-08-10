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

import android.os.Parcelable;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * A PagerAdapter wrapper responsible for providing a proper page to
 * LoopViewPager
 * 
 * This class shouldn't be used directly
 */
public class LoopPagerAdapterWrapper extends PagerAdapter {

    private PagerAdapter mAdapter;

    private SparseArray<ToDestroy> mToDestroy = new SparseArray<ToDestroy>();

    private boolean mBoundaryCaching;

    void setBoundaryCaching(boolean flag) {
        mBoundaryCaching = flag;
    }

    LoopPagerAdapterWrapper(PagerAdapter adapter) {
        this.mAdapter = adapter;
    }

    @Override
    public void notifyDataSetChanged() {
        mToDestroy = new SparseArray<ToDestroy>();
        super.notifyDataSetChanged();
    }
    
    int toRealPosition(int position) {
        int realCount = getRealCount();
        if(realCount == 0){
        	return 0;
        }
        int realPosition = (position-2) % realCount;
        if (realPosition < 0)
            realPosition += realCount;

        return realPosition;
    }
    
    public int toInnerPosition(int realPosition) {
        int position = (realPosition + 2);
        return position;
    }

    private int getRealFirstPosition() {
        return 1;
    }

    private int getRealLastPosition() {
        return getRealFirstPosition() + getRealCount() - 1;
    }
    

    @Override
    public int getCount() {
        return mAdapter.getCount() + 4;
    }

    public int getRealCount() {
        return mAdapter.getCount();
    }

    public PagerAdapter getRealAdapter() {
        return mAdapter;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int realPosition = (mAdapter instanceof FragmentPagerAdapter || mAdapter instanceof FragmentStatePagerAdapter)
                ? position
                : toRealPosition(position);

        if (mBoundaryCaching) {
            ToDestroy toDestroy = mToDestroy.get(position);
            if (toDestroy != null) {
                mToDestroy.remove(position);
                return toDestroy.object;
            }
        }
        
        return mAdapter.instantiateItem(container, positionMeasure(position, realPosition));
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        int realFirst = getRealFirstPosition();
        int realLast = getRealLastPosition();
        int realPosition = (mAdapter instanceof FragmentPagerAdapter || mAdapter instanceof FragmentStatePagerAdapter)
                ? position
                : toRealPosition(position);

        if (mBoundaryCaching && (position == realFirst || position == realLast)) {
            mToDestroy.put(position, new ToDestroy(container, realPosition,
                    object));
        }
        
        mAdapter.destroyItem(container, positionMeasure(position, realPosition), object);
    }

    /*
     * Delegate rest of methods directly to the inner adapter.
     */

    @Override
    public void finishUpdate(ViewGroup container) {
        mAdapter.finishUpdate(container);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return mAdapter.isViewFromObject(view, object);
    }

    @Override
    public void restoreState(Parcelable bundle, ClassLoader classLoader) {
        mAdapter.restoreState(bundle, classLoader);
    }

    @Override
    public Parcelable saveState() {
        return mAdapter.saveState();
    }

    @Override
    public void startUpdate(ViewGroup container) {
        mAdapter.startUpdate(container);
    }

    /*
     * End delegation
     */

    /**
     * Container class for caching the boundary views
     */
    static class ToDestroy {
        ViewGroup container;
        int position;
        Object object;

        public ToDestroy(ViewGroup container, int position, Object object) {
            this.container = container;
            this.position = position;
            this.object = object;
        }
    }
    
    
    
    //需要将虚拟的位置和真实位置一起发送。但是如果超过一旦虚拟位置超过32768，则会异常，应该没啥问题。
    	
    public static final int VIRTUALMASK=0xffff0000;
    
    public static int positionMeasure(int virtural,int real){
    	return (virtural<<16)+real;
    }
    public static int getReal(int positionMeasure){
    	return positionMeasure&(~VIRTUALMASK);
    }
    public static int getVirtual(int positionMeasure){
    	return (positionMeasure&VIRTUALMASK)>>16;
    }
    

}