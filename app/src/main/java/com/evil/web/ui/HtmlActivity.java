package com.evil.web.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;
import android.widget.EditText;

import com.evil.app.R;
import com.evil.baselib.base.BaseActivity;


public class HtmlActivity extends BaseActivity {
    private EditText mTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_htmls);
        mTv = (EditText) findViewById(R.id.tv);
        Bundle bundle = getBundle();
        String html = bundle.getString("html");
        mTv.setText(html);
    }
}
