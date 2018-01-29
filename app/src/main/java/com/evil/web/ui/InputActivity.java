package com.evil.web.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.evil.app.R;
import com.evil.baselib.base.BaseActivity;
import com.evil.baselib.db.InputHistoryInfo;
import com.evil.web.adapter.InputHistoryAdapter;
import com.fxc.util.KeyboardUtils;
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
public class InputActivity
        extends BaseActivity
        implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener
{
    private ListView               mLv;
    private InputHistoryAdapter mAdapter;
    private TwinklingRefreshLayout mTkrefresh;
    private EditText               mInput;

    public final static int INPUT_URL_RESULT_CODE = 0x011;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        initView();
    }

    private void initView() {
        mLv = (ListView) findViewById(R.id.lv);
        mTkrefresh = (TwinklingRefreshLayout) findViewById(R.id.tkrefresh);
        mInput = (EditText) findViewById(R.id.input);
        mInput.setOnKeyListener(mOnKeyListener);
        RefreshUtils.initRefreshLayout(mTkrefresh,this);
        mTkrefresh.setEnableRefresh(false);
        mTkrefresh.setOnRefreshListener(mRefreshListener);
        mAdapter = new InputHistoryAdapter(this);
        mLv.setAdapter(mAdapter);
        mLv.setOnItemClickListener(this);
        mLv.setOnItemLongClickListener(this);
        findData();
    }

    private View.OnKeyListener mOnKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                // 先隐藏键盘
                KeyboardUtils.hideSoftInput(getActivity());
                //进行搜索操作的方法，在该方法中可以加入mEditSearchUser的非空判断
                submit();
                return true;
            }
            return false;
        }
    };


    public void findData() {
        List<InputHistoryInfo> infos = DataSupport.order("id desc")
                                                  .limit(20)//查询20条
                                                  .offset(mAdapter.getCount())//偏移量为20
                                                  .find(InputHistoryInfo.class);
        mAdapter.addData(infos);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        InputHistoryInfo info   = mAdapter.getItem(position);
        Intent      intent = new Intent();
        intent.putExtra("url", info.getUrl());
        setResult(0x99, intent);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("是否删除该条记录?");
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                InputHistoryInfo info = mAdapter.getItem(position);
                info.delete();
                mAdapter.delete(position);
            }
        });
        builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
        return true;
    }

    private void submit() {
        String inputString = mInput.getText()
                                  .toString()
                                  .trim();
        if (TextUtils.isEmpty(inputString)) {
            toast("请输入网址");
            return;
        }
        InputHistoryInfo info = new InputHistoryInfo(inputString);
        info.save();
        Intent intent = new Intent();
        intent.putExtra("url",inputString);
        setResult(INPUT_URL_RESULT_CODE,intent);
        finish();
    }
}
