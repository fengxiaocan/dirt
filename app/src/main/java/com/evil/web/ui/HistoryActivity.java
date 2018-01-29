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
import com.evil.baselib.db.HistoryInfo;
import com.evil.web.adapter.HistoryAdapter;
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
 *  @描述： 历史记录
 */
public class HistoryActivity
        extends BaseActivity
        implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener
{
    private ListView               mLv;
    private ImageView              mIvBack;
    private TextView               mTvClear;
    private HistoryAdapter mAdapter;
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
        mTvClear = (TextView) findViewById(R.id.tv_clear);
        mTkrefresh = (TwinklingRefreshLayout) findViewById(R.id.tkrefresh);
        RefreshUtils.initRefreshLayout(mTkrefresh,this);
        mTkrefresh.setEnableRefresh(false);
        mTkrefresh.setOnRefreshListener(mRefreshListener);
        setOnClick(mIvBack, mTvClear);
        mAdapter = new HistoryAdapter(this);
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
                    DataSupport.deleteAll(HistoryInfo.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mAdapter.clear();
                break;
        }
    }

    public void findData() {
        List<HistoryInfo> infos = DataSupport.order("id desc")
                                             .limit(20)//查询20条
                                             .offset(mAdapter.getCount())//偏移量为20
                                             .find(HistoryInfo.class);
        mAdapter.addData(infos);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        HistoryInfo info   = mAdapter.getItem(position);
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
        iosDialog.setMessage("是否删除该条记录?");
        iosDialog.setLeftButton("确定",new DialogCancelListener(){
            @Override
            public void onClick(DialogInterface dialog) {
                super.onClick(dialog);
                HistoryInfo info = mAdapter.getItem(position);
                info.delete();
                mAdapter.delete(position);
            }
        });
        iosDialog.setRightButton("取消",new DialogCancelListener());
        iosDialog.show();
        return true;
    }
}
