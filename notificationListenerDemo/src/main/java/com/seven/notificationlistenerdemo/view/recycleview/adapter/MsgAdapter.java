package com.seven.notificationlistenerdemo.view.recycleview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.notificationlistenerdemo.R;
import com.seven.notificationlistenerdemo.view.recycleview.bean.NotificationInfo;

import java.util.ArrayList;

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.MsgViewHolder>
{
    private Context context;
    private ArrayList<NotificationInfo> mCacheNotificationInfo;
    public MsgAdapter(Context context, ArrayList s){
        this.context = context;
        this.mCacheNotificationInfo = s;
    }
    @Override
    public MsgViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        MsgViewHolder holder = new MsgViewHolder(LayoutInflater.from(
                context).inflate(R.layout.item_msg, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MsgViewHolder holder, final int position)
    {
        ViewGroup.LayoutParams params =  holder.itemView.getLayoutParams();//得到item的LayoutParams布局参数
        params.height = 200;//把随机的高度赋予itemView布局
        holder.itemView.setLayoutParams(params);//把params设置给itemView布局
        holder.iv_icon.setImageBitmap(mCacheNotificationInfo.get(position).icon);
        holder.tv_title.setText(mCacheNotificationInfo.get(position).title);
        holder.tv_content.setText(mCacheNotificationInfo.get(position).content);
        holder.tv_time.setText(mCacheNotificationInfo.get(position).time);

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
                    removeData(pos);
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
        return mCacheNotificationInfo.size();
    }

    class MsgViewHolder extends RecyclerView.ViewHolder
    {
        ImageView iv_icon;
        TextView tv_title;
        TextView tv_content;
        TextView tv_time;

        public MsgViewHolder(View view)
        {
            super(view);
            iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_content = (TextView) view.findViewById(R.id.tv_content);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
        }
    }

    public void addData(int position) {
        mCacheNotificationInfo.add(position, null);
        notifyItemInserted(position);
    }

    public void removeData(int position) {
        if(mCacheNotificationInfo.size()!=0){
            mCacheNotificationInfo.remove(position);
        }
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
