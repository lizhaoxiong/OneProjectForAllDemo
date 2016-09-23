package com.seven.notificationlistenerdemo.recycle.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.notificationlistenerdemo.R;
import com.ok.utils.utils.AppUtils;
import com.ok.utils.utils.cache.PreferencesUtils;

import java.util.ArrayList;

public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.PackageViewHolder>
{
    private Context context;
    private ArrayList<AppUtils.AppInfo> mlistAppInfo;
    public PackageAdapter(Context context, ArrayList s){
        this.context = context;
        this.mlistAppInfo = s;
    }
    @Override
    public PackageViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        PackageViewHolder holder = new PackageViewHolder(LayoutInflater.from(
                context).inflate(R.layout.item, parent,
                false));
        return holder;
    }
    @Override
    public void onBindViewHolder(final PackageViewHolder holder, final int position)
    {
        ViewGroup.LayoutParams params =  holder.itemView.getLayoutParams();//得到item的LayoutParams布局参数
        params.height = 200;//把随机的高度赋予itemView布局
        holder.itemView.setLayoutParams(params);//把params设置给itemView布局

        AppUtils.AppInfo appInfo = mlistAppInfo.get(position);
        holder.bindData(appInfo);

    }

    @Override
    public int getItemCount()
    {
        return mlistAppInfo.size();
    }

    public class PackageViewHolder extends RecyclerView.ViewHolder
    {
        ImageView iv_package;
        TextView tv;
        public CheckBox tb;

        public PackageViewHolder(View view)
        {
            super(view);
            iv_package = (ImageView) view.findViewById(R.id.iv_package);
            tv = (TextView) view.findViewById(R.id.tv_name);
            tb = (CheckBox)view.findViewById(R.id.cb_notify);
            view.setTag(this);
        }

        public void bindData(AppUtils.AppInfo appInfo) {
            if(appInfo==null){
                return;
            }
            tv.setText(appInfo.getName());
            iv_package.setImageDrawable(appInfo.getIcon());
            tb.setChecked(PreferencesUtils.getSharePrefStr(context,"white_table",appInfo.getPackagName()).equals("0"));

            // 如果设置了回调，则设置点击事件
            if (mOnItemClickLitener != null)
            {
                itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        int pos = getLayoutPosition();
                        mOnItemClickLitener.onItemClick(itemView, pos);
                    }
                });
            }
        }
    }

    //---------------------------对外提供点击和长按的接口
    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }
    private OnItemClickLitener mOnItemClickLitener;
    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    /***
     * 1.数据从哪里来的，每次因该都得遍历本地应用程序
     * 2.并且判断应用程序的对应的拦截属性
     * 3.
     */

}
