package com.evil.my.inface;

import com.evil.meizi.bean.Meizi;

import java.util.List;

public interface MeiziCallback {
    void onResult(List<Meizi> list);

    void onError(String str);
}
