package com.seven.notificationlistenerdemo.Recycle;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.notificationlistenerdemo.R;

import java.util.ArrayList;

public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.MyViewHolder>
{
    private Context context;
    private ArrayList<AppInfo> mlistAppInfo;
    public PackageAdapter(Context context, ArrayList s){
        this.context = context;
        this.mlistAppInfo = s;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position)
    {
        ViewGroup.LayoutParams params =  holder.itemView.getLayoutParams();//得到item的LayoutParams布局参数
        params.height = 200;//把随机的高度赋予itemView布局
        holder.itemView.setLayoutParams(params);//把params设置给itemView布局
        holder.tv.setText(mlistAppInfo.get(position).appLabel);
        holder.iv_package.setImageDrawable(mlistAppInfo.get(position).appIcon);

        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null)
        {
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount()
    {
        return mlistAppInfo.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        ImageView iv_package;
        TextView tv;
        ToggleButton tb;

        public MyViewHolder(View view)
        {
            super(view);
            iv_package = (ImageView) view.findViewById(R.id.iv_package);
            tv = (TextView) view.findViewById(R.id.tv_name);
            tb = (ToggleButton)view.findViewById(R.id.tb_notify);
        }
    }

    public void addData(int position) {
        mlistAppInfo.add(position, null);
        notifyItemInserted(position);
    }

    public void removeData(int position) {
        mlistAppInfo.remove(position);
        notifyItemRemoved(position);
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

}
