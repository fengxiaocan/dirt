package com.evil.meizi.a;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.evil.app.R;
import com.evil.baselib.base.BaseActivity;
import com.evil.meizi.bean.Meizi;
import com.fxc.log.LogUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MeiziActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meizi);
        loadData();
    }

    private void loadData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect("https://www.dbmeinv.com/dbgroup/show.htm").get();
                    Elements elements = doc.getElementsByClass("panel-body");
                    for (Element element : elements) {
                        Elements link = element.getElementsByClass("link");
                        for (Element element1 : link) {
                            String href = element1.attr("href");
                            LogUtils.e("noah", href);
                            Elements img = element1.getElementsByClass("height_min");
                            String title = img.attr("title");
                            LogUtils.e("noah", title);
                            String src = img.attr("src");
                            LogUtils.e("noah", src);
                            Meizi meizi = new Meizi(title, href, src);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
}
