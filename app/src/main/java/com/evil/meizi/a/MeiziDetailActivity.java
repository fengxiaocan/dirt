package com.evil.meizi.a;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.evil.app.R;
import com.evil.baselib.base.BaseActivity;
import com.fxc.base.adapter.ImagePagerAdapter;
import com.fxc.util.HandlerUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class MeiziDetailActivity extends BaseActivity {
    private ViewPager view_pager;
    private ImagePagerAdapter<String> mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meizi_detail);
        initView();
        loadData();
    }

    private void loadData() {
        final String url = getBundle().getString("url", "https://www.dbmeinv.com:443/dbgroup/1635561");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect(url).get();
                    Elements elements = doc.getElementsByClass("topic-richtext");
                    final List<String> list = new ArrayList<>();
                    for (Element element : elements) {
                        Elements byClass = element.getElementsByClass("image-wrapper");
                        for (Element aClass : byClass) {
                            Elements img = aClass.getElementsByTag("img");
                            for (Element element1 : img) {
                                String src = element1.attr("src");
                                list.add(src);
                            }
                        }
                    }
                    HandlerUtils.post(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter = new ImagePagerAdapter<String>(getContext(), list) {
                                @Override
                                public ImageView giveMeImageView(Context context, int i) {
                                    ImageView imageView = new ImageView(context);
                                    Glide.with(context).load(list.get(i)).into(imageView);
                                    return imageView;
                                }
                            };
                            view_pager.setAdapter(mAdapter);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    private void initView() {
        view_pager = (ViewPager) findViewById(R.id.view_pager);
    }

}
