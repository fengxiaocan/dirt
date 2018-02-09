package com.evil.meizi.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.evil.app.R;
import com.evil.meizi.bean.Meizi;
import com.fxc.base.adapter.BaseAdapter;
import com.fxc.base.holder.ListViewHolder;

public class MeiziAdapter extends BaseAdapter<Meizi, MeiziAdapter.ViewHolder> {
    @Override
    public int onBindLayoutRes() {
        return R.layout.item_meizi;
    }

    @Override
    public ViewHolder onCreateViewHolder(View view, int i) {
        return new ViewHolder(view);
    }

    @Override
    public void onBindData(ViewHolder holder, int i) {
        Meizi item = getItem(i);
        holder.mTvName.setText(item.getTitle());
        Glide.with(holder.mIv.getContext()).load(item.getSrc()).into(holder.mIv);
    }

    public static class ViewHolder extends ListViewHolder {
        public View rootView;
        public TextView mTvName;
        public ImageView mIv;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.mTvName = (TextView) rootView.findViewById(R.id.tv_name);
            this.mIv = (ImageView) rootView.findViewById(R.id.iv);
        }

    }
}
