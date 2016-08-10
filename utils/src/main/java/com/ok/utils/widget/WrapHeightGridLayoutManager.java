package com.ok.utils.widget;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * <p>Copyright: Copyright (c) 2016</p>
 * <p/>
 * <p>Company: 浙江齐聚科技有限公司<a href="www.guagua.cn">www.guagua.cn</a></p>
 *
 * @author 薛文超
 * @version 1.0.0
 * @description
 * @modify
 */
public class WrapHeightGridLayoutManager extends GridLayoutManager {
    private RecyclerView.Adapter mAdapter;

    public WrapHeightGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout, RecyclerView.Adapter adapter) {
        super(context, spanCount, orientation, reverseLayout);
        mAdapter = adapter;
    }

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
        if (mAdapter.getItemCount() <= 0) {
            heightSpec = 0;
            super.onMeasure(recycler, state, widthSpec, heightSpec);
            return;
        }
        View view = recycler.getViewForPosition(0);
        if(view != null){
            measureChild(view, widthSpec, heightSpec);
            int measuredHeight = view.getMeasuredHeight();  //这个高度不准确
            int measuredWidth = View.MeasureSpec.getSize(widthSpec);
            int line = mAdapter.getItemCount() / getSpanCount();
            if (mAdapter.getItemCount() % getSpanCount() > 0)
                line++;
            setMeasuredDimension(measuredWidth, measuredHeight * line);
        }
    }
}
