package com.evil.meizi.a;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.evil.app.R;
import com.evil.baselib.base.BaseActivity;
import com.evil.meizi.adapter.MeiziAdapter;
import com.evil.meizi.bean.Meizi;
import com.evil.my.inface.MeiziCallback;
import com.fxc.util.RefreshUtils;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.List;

public class MeiziActivity extends BaseActivity {
    private ListView mLv;
    private MeiziAdapter mAdapter;
    private TwinklingRefreshLayout mRefresh;
    private int position = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meizi);
        initView();
        mAdapter = new MeiziAdapter();
        mLv.setAdapter(mAdapter);
        loadData();
        mLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Meizi meizi = mAdapter.getItem(position);
                Bundle bundle = newBundle();
                bundle.putString("url",meizi.getHref());
                openActivity(MeiziDetailActivity.class,bundle);
            }
        });
    }

    private void loadData() {
        Meizi.getInfoList("https://www.dbmeinv.com/dbgroup/show.htm", new MeiziCallback() {
            @Override
            public void onResult(List<Meizi> list) {
                mAdapter.setData(list);
                position = 2;
            }

            @Override
            public void onError(String str) {
                toast(str);
            }
        });
    }

    private void initView() {
        mLv = (ListView) findViewById(R.id.lv);
        mRefresh = (TwinklingRefreshLayout) findViewById(R.id.refresh);
        RefreshUtils.initRefreshLayout(mRefresh, this);
        mRefresh.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                Meizi.getInfoList("https://www.dbmeinv.com/dbgroup/show.htm", new MeiziCallback() {
                    @Override
                    public void onResult(List<Meizi> list) {
                        mAdapter.setData(list);
                        position = 2;
                        refreshLayout.finishRefreshing();
                    }

                    @Override
                    public void onError(String str) {
                        toast(str);
                        refreshLayout.finishRefreshing();
                    }
                });
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                Meizi.getInfoList("https://www.dbmeinv.com/dbgroup/show.htm?pager_offset=" + position, new
                        MeiziCallback() {
                            @Override
                            public void onResult(List<Meizi> list) {
                                mAdapter.setData(list);
                                position++;
                                refreshLayout.finishLoadmore();
                            }

                            @Override
                            public void onError(String str) {
                                toast(str);
                                refreshLayout.finishLoadmore();
                            }
                        });
            }
        });
    }
}
