package com.evil.web.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.evil.app.R;
import com.evil.baselib.base.BaseActivity;
import com.evil.baselib.db.FavoriteInfo;
import com.evil.web.adapter.FavoriteAdapter;
import com.fxc.base.dialog.DialogCancelListener;
import com.fxc.base.dialog.IosDialog;
import com.fxc.util.RefreshUtils;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 *  @项目名： WebBrowser
 *  @包名： com.evil.webbrowser.ui
 *  @创建者: Noah.冯
 *  @时间: 19:09
 *  @描述： 书签
 */
public class FavoriteActivity
        extends BaseActivity
        implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener
{
    private ListView               mLv;
    private ImageView              mIvBack;
    private TextView               mTvTitle;
    private TextView               mTvClear;
    private FavoriteAdapter mAdapter;
    private TwinklingRefreshLayout mTkrefresh;
    public final static int HISTORY_RESULT_CODE =0x99;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        initView();
    }

    private void initView() {
        mLv = (ListView) findViewById(R.id.lv);
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvTitle.setText("书签");
        mTvClear = (TextView) findViewById(R.id.tv_clear);
        mTvClear.setText("清除所有书签");
        mTkrefresh = (TwinklingRefreshLayout) findViewById(R.id.tkrefresh);
        RefreshUtils.initRefreshLayout(mTkrefresh,this);
        mTkrefresh.setEnableRefresh(false);
        mTkrefresh.setOnRefreshListener(mRefreshListener);
        setOnClick(mIvBack, mTvClear);
        mAdapter = new FavoriteAdapter(this);
        mLv.setAdapter(mAdapter);
        mLv.setOnItemClickListener(this);
        mLv.setOnItemLongClickListener(this);
        findData();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_clear:
                try {
                    DataSupport.deleteAll(FavoriteInfo.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mAdapter.clear();
                break;
        }
    }

    public void findData() {
        List<FavoriteInfo> infos = DataSupport.order("id desc")
                                             .limit(20)//查询20条
                                             .offset(mAdapter.getCount())//偏移量为20
                                             .find(FavoriteInfo.class);
        mAdapter.addData(infos);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        FavoriteInfo info   = mAdapter.getItem(position);
        Intent      intent = new Intent();
        intent.putExtra("url", info.getUrl());
        setResult(HISTORY_RESULT_CODE, intent);
        finish();
    }

    private RefreshListenerAdapter mRefreshListener = new RefreshListenerAdapter() {
        @Override
        public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
            findData();
            refreshLayout.finishLoadmore();
        }
    };

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        IosDialog iosDialog = new IosDialog(getContext());
        iosDialog.setTitle("提示");
        iosDialog.setMessage("是否删除该条书签?");
        iosDialog.setLeftButton("确定",new DialogCancelListener(){
            @Override
            public void onClick(DialogInterface dialog) {
                super.onClick(dialog);
                FavoriteInfo info = mAdapter.getItem(position);
                info.delete();
                mAdapter.delete(position);
            }
        });
        iosDialog.setRightButton("取消",new DialogCancelListener());
        iosDialog.show();
        return true;
    }
}
