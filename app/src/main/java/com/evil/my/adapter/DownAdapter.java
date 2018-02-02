package com.evil.my.adapter;

import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import com.evil.app.R;
import com.evil.app.ui.view.MarqueeTextView;
import com.evil.baselib.db.DowningInfo;
import com.evil.my.inface.DownConstants;
import com.fxc.base.adapter.BaseAdapter;
import com.fxc.base.holder.ListViewHolder;
import com.fxc.util.StringUtils;
import com.fxc.util.TimeUtils;

public class DownAdapter extends BaseAdapter<DowningInfo, DownAdapter.ViewHolder> {
    private SparseArray<DowningInfo> taskArray;

    public void setTaskArray(SparseArray<DowningInfo> taskArray) {
        this.taskArray = taskArray;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return taskArray == null ? 0 : taskArray.size();
    }

    @Override
    public DowningInfo getItem(int position) {
        int key = taskArray.keyAt(position);
        return taskArray.get(key);
    }

    @Override
    public int onBindLayoutRes() {
        return R.layout.item_down;
    }

    @Override
    public ViewHolder onCreateViewHolder(View view, int i) {
        return new ViewHolder(view);
    }

    @Override
    public void onBindData(ViewHolder holder, int i) {
        DowningInfo info = getItem(i);
        holder.mTvName.setText(info.getName());
        holder.mTvPath.setText(info.getSavePath());
        holder.mTvMd5.setText(info.getMd5());
        holder.mTvUrl.setText(info.getUrl());
        holder.mTvTime.setText(TimeUtils.formatTime(info.getCreateTime(), TimeUtils.DATE_TYPE20));
        holder.mTvTotal.setText(StringUtils.formatFileSize(info.getTotalSize()));
        holder.mTvFe.setText(StringUtils.formatFileSize(info.getDownSize()));
        switch (info.getStatu()) {
            default:
            case DownConstants.NoDown:
                holder.mTvStatu.setText("未下载");
                holder.mTvStatu.setTextColor(R.color.positiveText);
                break;
            case DownConstants.Downing:
                holder.mTvStatu.setText("下载中");
                holder.mTvStatu.setTextColor(R.color.textColorPrimary);
                break;
            case DownConstants.DownPause:
                holder.mTvStatu.setText("暂停中");
                holder.mTvStatu.setTextColor(R.color.colorPrimaryDark);
                break;
            case DownConstants.DownError:
                holder.mTvStatu.setText("下载错误");
                holder.mTvStatu.setTextColor(R.color.colorRed);
                break;
            case DownConstants.DownPending:
                holder.mTvStatu.setText("等待下载");
                holder.mTvStatu.setTextColor(R.color.darksalmon);
                break;
        }
    }

    public static class ViewHolder extends ListViewHolder {
        public View rootView;
        public MarqueeTextView mTvName;
        public MarqueeTextView mTvUrl;
        public MarqueeTextView mTvPath;
        public MarqueeTextView mTvMd5;
        public TextView mTvTime;
        public TextView mTvTotal;
        public TextView mTvFe;
        public TextView mTvStatu;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.mTvName = (MarqueeTextView) rootView.findViewById(R.id.tv_name);
            this.mTvUrl = (MarqueeTextView) rootView.findViewById(R.id.tv_url);
            this.mTvPath = (MarqueeTextView) rootView.findViewById(R.id.tv_path);
            this.mTvMd5 = (MarqueeTextView) rootView.findViewById(R.id.tv_md5);
            this.mTvTime = (TextView) rootView.findViewById(R.id.tv_time);
            this.mTvTotal = (TextView) rootView.findViewById(R.id.tv_total);
            this.mTvFe = (TextView) rootView.findViewById(R.id.tv_fe);
            this.mTvStatu = (TextView) rootView.findViewById(R.id.tv_statu);
        }

    }
}
