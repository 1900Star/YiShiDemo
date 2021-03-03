package com.example.yishidemo;

import android.app.Application;

import com.videogo.openapi.EZOpenSDK;

public class YiShiApplication extends Application {
    private String APP_KEY = "fa3e5b6ccca4478abb26f8bad320ad3c";

    @Override
    public void onCreate() {
        super.onCreate();

        /** * sdk日志开关，正式发布需要去掉 */
        EZOpenSDK.showSDKLog(true);
        /** * 设置是否支持P2P取流,详见api */
        EZOpenSDK.enableP2P(false);
        EZOpenSDK.initLib(this,APP_KEY);

    }
}
