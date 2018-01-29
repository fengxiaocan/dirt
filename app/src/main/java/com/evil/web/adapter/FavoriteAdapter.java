package com.evil.web.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.evil.app.R;
import com.evil.baselib.db.FavoriteInfo;

import java.util.List;

/**
 *  @项目名： WebBrowser
 *  @包名： com.evil.webbrowser.adapter
 *  @创建者: Noah.冯
 *  @时间: 13:57
 *  @描述： TODO
 */
public class FavoriteAdapter
        extends BaseAdapter
{
    private Context           mContext;
    private List<FavoriteInfo> mList;

    public FavoriteAdapter(Context context) {
        mContext = context;
    }

    public void clear(){
        mList = null;
        notifyDataSetChanged();
    }

    public FavoriteAdapter(Context context, List<FavoriteInfo> list) {
        mContext = context;
        mList = list;
    }

    public void setData(List<FavoriteInfo> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public void addData(List<FavoriteInfo> list) {
        if (mList == null) {
            mList = list;
        }else{
            mList.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void delete(int position) {
        if (mList != null) {
            mList.remove(position);
            notifyDataSetChanged();
        }
    }
    @Override
    public int getCount() {
        if (mList != null) {
            return mList.size();
        }
        return 0;
    }

    @Override
    public FavoriteInfo getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_history, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        FavoriteInfo info = getItem(position);
        holder.mIv.setImageResource(R.mipmap.bookmark);
        holder.mTvName.setText(info.getTitle());
        holder.mTvUrl.setText(info.getUrl());
        holder.mTvTime.setText(info.getTime());
        return convertView;
    }


    public static class ViewHolder {
        public View     rootView;
        public ImageView mIv;
        public TextView mTvName;
        public TextView mTvUrl;
        public TextView mTvTime;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.mIv = (ImageView) rootView.findViewById(R.id.iv);
            this.mTvName = (TextView) rootView.findViewById(R.id.tv_name);
            this.mTvUrl = (TextView) rootView.findViewById(R.id.tv_url);
            this.mTvTime = (TextView) rootView.findViewById(R.id.tv_time);
        }

    }
}
