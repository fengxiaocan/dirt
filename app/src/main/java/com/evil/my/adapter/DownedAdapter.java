package com.evil.my.adapter;

import android.view.View;
import android.widget.TextView;

import com.evil.app.R;
import com.evil.app.ui.view.MarqueeTextView;
import com.evil.baselib.db.DownedInfo;
import com.fxc.base.adapter.BaseAdapter;
import com.fxc.base.holder.ListViewHolder;
import com.fxc.util.StringUtils;
import com.fxc.util.TimeUtils;

public class DownedAdapter extends BaseAdapter<DownedInfo, DownedAdapter.ViewHolder> {

    @Override
    public int onBindLayoutRes() {
        return R.layout.item_downed;
    }

    @Override
    public ViewHolder onCreateViewHolder(View view, int i) {
        return new ViewHolder(view);
    }

    @Override
    public void onBindData(ViewHolder holder, int i) {
        DownedInfo info = getItem(i);
        holder.mTvName.setText(info.getName());
        holder.mTvPath.setText(info.getSavePath());
        holder.mTvMd5.setText(info.getMd5());
        holder.mTvUrl.setText(info.getUrl());
        holder.mTvTime.setText(TimeUtils.formatTime(info.getDownTime(), TimeUtils.DATE_TYPE20));
        holder.mTvTotal.setText(StringUtils.formatFileSize(info.getTotalSize()));
    }

    public static class ViewHolder extends ListViewHolder {
        public View rootView;
        public MarqueeTextView mTvName;
        public MarqueeTextView mTvUrl;
        public MarqueeTextView mTvPath;
        public MarqueeTextView mTvMd5;
        public TextView mTvTime;
        public TextView mTvTotal;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.mTvName = (MarqueeTextView) rootView.findViewById(R.id.tv_name);
            this.mTvUrl = (MarqueeTextView) rootView.findViewById(R.id.tv_url);
            this.mTvPath = (MarqueeTextView) rootView.findViewById(R.id.tv_path);
            this.mTvMd5 = (MarqueeTextView) rootView.findViewById(R.id.tv_md5);
            this.mTvTime = (TextView) rootView.findViewById(R.id.tv_time);
            this.mTvTotal = (TextView) rootView.findViewById(R.id.tv_total);
        }

    }
}
