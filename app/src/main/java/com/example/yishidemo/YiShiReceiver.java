package com.example.yishidemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.videogo.openapi.EZOpenSDK;
import com.videogo.openapi.bean.EZAccessToken;

public class YiShiReceiver  extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (TextUtils.equals(intent.getAction(),"com.videogo.action.OAUTH_SUCCESS_ACTION")){
            EZAccessToken accessToken = EZOpenSDK.getInstance().getEZAccessToken();
            // begin your work
        }

    }
}
