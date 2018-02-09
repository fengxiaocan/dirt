package com.evil.meizi.bean;

import com.evil.my.inface.MeiziCallback;
import com.fxc.util.HandlerUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class Meizi {
    private String title;
    private String href;
    private String src;

    public Meizi(String title, String href, String src) {
        this.title = title;
        this.href = href;
        this.src = src;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public static void getInfoList(final String url, final MeiziCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect(url).get();
                    Elements elements = doc.getElementsByClass("panel-body");
                    final List<Meizi> list = new ArrayList<>();
                    for (Element element : elements) {
                        Elements link = element.getElementsByClass("link");
                        for (Element element1 : link) {
                            String href = element1.attr("href");
                            Elements img = element1.getElementsByClass("height_min");
                            String title = img.attr("title");
                            String src = img.attr("src");
                            Meizi meizi = new Meizi(title, href, src);
                            list.add(meizi);
                        }
                    }
                    HandlerUtils.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onResult(list);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onError(e.getMessage());
                }

            }
        }).start();
    }
}
