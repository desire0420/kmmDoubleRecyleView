package com.fatchao.gangedrecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangwei on 2018/1/3.
 */

public class ClassifyDetailAdapter1 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<RightBean> mDatas = new ArrayList<>();

    private OnItemClickLitener mOnItemClickLitener;

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

        void onHeadClick(View view, int position);
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


    public ClassifyDetailAdapter1(Context context, List<RightBean> list) {
        this.mContext = context;
        this.mDatas = list;
        mLayoutInflater = LayoutInflater.from(context);
    }


    //判断当前item类型
    @Override
    public int getItemViewType(int position) {
        return mDatas.get(position).isTitle() ? 0 : 1;
    }

    //内容 ViewHolder
    public static class ContentViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCity;

        public ContentViewHolder(View itemView) {
            super(itemView);
            tvCity = (TextView) itemView.findViewById(R.id.tvCity);
        }
    }

    //头部 ViewHolder
    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv_title);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new HeaderViewHolder(mLayoutInflater.inflate(R.layout.item_title, parent, false));
        } else if (viewType == 1) {
            return new ContentViewHolder(mLayoutInflater.inflate(R.layout.item_classify_detail, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HeaderViewHolder) {
            final HeaderViewHolder viewHolder = ((HeaderViewHolder) holder);

            viewHolder.textView.setText(mDatas.get(position).getName());
            viewHolder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onHeadClick(viewHolder.textView, position);
                }
            });

        } else if (holder instanceof ContentViewHolder) {
            final ContentViewHolder viewHolder = ((ContentViewHolder) holder);
            viewHolder.tvCity.setText(mDatas.get(position).getName());
            viewHolder.tvCity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(viewHolder.tvCity, position);
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }


}

