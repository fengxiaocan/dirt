package com.evil.setting;

import com.fxc.util.SpUtils;

import static com.evil.setting.SettingConstants.*;

public class Setting {
    public static int sDownRetryTimes;
    public static boolean sOnlyWifiDown;

    public static void initSetting() {
        sDownRetryTimes = SpUtils.getInfo(DOWN_AUTO_RETRY_TIMES, DEFULT_DOWN_AUTO_RETRY_TIMES);
        sOnlyWifiDown = SpUtils.getInfo(ONLYWIFI_DOWN, DEFULT_WIFI_DOWN);
    }
}
