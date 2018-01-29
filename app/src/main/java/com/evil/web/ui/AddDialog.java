package com.evil.web.ui;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.evil.app.R;
import com.evil.web.intface.WebAddCallback;
import com.fxc.util.BarUtils;

import java.util.List;

/**
 *  @项目名： WebBrowser
 *  @包名： com.evil.webbrowser.ui
 *  @创建者: Noah.冯
 *  @时间: 19:15
 *  @描述： 添加
 */
public class AddDialog
        extends Dialog
        implements View.OnClickListener, AdapterView.OnItemClickListener
{
    public  ListView          mLv;
    public  View              mViewAdd;
    private int               mHeight;
    private int               mWidth;
    private List<WebFragment> mData;
    private int               mStatusHeight;
    private WebAddCallback mWebAddCallback;

    public AddDialog(Context context) {
        this(context, R.style.setting_dialog);
    }

    public AddDialog(Context context, int themeResId) {
        super(context, themeResId);
        initView();

        WindowManager manager = getWindow().getWindowManager();
        Display       display = manager.getDefaultDisplay();
        mWidth = display.getWidth();
        mHeight = display.getHeight();
        mStatusHeight = BarUtils.getStatusBarHeight(getContext());
    }

    public void setData(List<WebFragment> data) {
        mData = data;
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void show() {
        super.show();
        Window window = getWindow();
        window.setGravity(Gravity.CENTER); //显示在中间
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = mWidth; //设置dialog的宽度为当前手机屏幕的宽度
        lp.height = mHeight - mStatusHeight;
        window.setAttributes(lp);
    }

    public void show(List<WebFragment> data) {
        mData = data;
        mAdapter.notifyDataSetChanged();
        show();
    }

    public void setWebAddCallback(WebAddCallback webAddCallback) {
        mWebAddCallback = webAddCallback;
    }

    protected void initView() {
        View view = View.inflate(getContext(), R.layout.dialog_add, null);
        mLv = (ListView) view.findViewById(R.id.lv);
        mLv.setAdapter(mAdapter);
        mLv.setOnItemClickListener(this);
        mViewAdd = view.findViewById(R.id.view_add);
        setOnClick(mViewAdd);
        setContentView(view);
    }

    private BaseAdapter mAdapter = new BaseAdapter() {
        @Override
        public int getCount() {
            if (mData != null) {
                return mData.size();
            }
            return 0;
        }

        @Override
        public WebFragment getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(getContext(), R.layout.item_add_dialog, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            WebFragment web = getItem(position);
            holder.mTvTitle.setText(web.getTitle());
            holder.mTvUrl.setText(web.getUrl());
            holder.mIv.setImageBitmap(web.getIconBitmap());
            holder.mIvClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mWebAddCallback.onCloseWeb(position);
                    dismiss();
                }
            });
            return convertView;
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_add:
                mWebAddCallback.onAddWeb();
                dismiss();
                break;
        }
    }

    public void setOnClick(View view) {
        view.setOnClickListener(this);
    }

    public void setOnClick(View... views) {
        View[] var2 = views;
        int    var3 = views.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            View view = var2[var4];
            view.setOnClickListener(this);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mWebAddCallback.onOpenWeb(position);
        dismiss();
    }

    public static class ViewHolder {
        public View      rootView;
        public ImageView mIv;
        public TextView  mTvTitle;
        public TextView  mTvUrl;
        public ImageView mIvClose;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.mIv = (ImageView) rootView.findViewById(R.id.iv);
            this.mTvTitle = (TextView) rootView.findViewById(R.id.tv_title);
            this.mTvUrl = (TextView) rootView.findViewById(R.id.tv_url);
            this.mIvClose = (ImageView) rootView.findViewById(R.id.iv_close);
        }
    }

}
