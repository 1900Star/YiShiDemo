package com.example.yishidemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.videogo.exception.BaseException;
import com.videogo.openapi.EZOpenSDK;
import com.videogo.openapi.EZPlayer;
import com.videogo.openapi.bean.EZCameraInfo;
import com.videogo.openapi.bean.EZDeviceInfo;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback{

    private SurfaceView mRealPlaySv;
    private SurfaceHolder mRealPlaySh;
    private EZPlayer mEZPlayer = null;
    private List<EZDeviceInfo> mDeviceInfoList;
    // 摄像头底部的序列号
    private  String serialNo = "6***********5";
    // 摄像头底部的验证码
   private  String devCode = "";

    private static final int MSG_ON_DEVICE_RESPONSE=  1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
         * 这里的AccessToken取个值，用来写Demo的
         *  写项目的时候要注意AccessToken每隔一个星期就会改变一次
         * */
        String accessToken="at.dsrgqr7ja727gdxv8syafg570roig17p-6t5nphl75d-1891fdy-5t70z8wxh";
        EZOpenSDK.getInstance().setAccessToken(accessToken);



        mRealPlaySv=(SurfaceView)findViewById(R.id.remoteplayback_sv);

        mRealPlaySh = mRealPlaySv.getHolder();
        mRealPlaySh.addCallback(this);

        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    mDeviceInfoList = EZOpenSDK.getInstance().getDeviceList(0, 20);

                    mHandler.sendEmptyMessage(MSG_ON_DEVICE_RESPONSE);

                } catch (BaseException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }



    /**
     * 通过ezdevice 得到其中通道信息
     * @param deviceInfo
     * @return
     */
    public static EZCameraInfo getCameraInfoFromDevice(EZDeviceInfo deviceInfo, int camera_index) {
        if (deviceInfo == null) {
            return null;
        }
        if (deviceInfo.getCameraNum() > 0 && deviceInfo.getCameraInfoList() != null && deviceInfo.getCameraInfoList().size() > camera_index) {
            return deviceInfo.getCameraInfoList().get(camera_index);
        }
        return null;
    }


    private void startPlay() {

        EZDeviceInfo deviceInfo = null;
        for (EZDeviceInfo ezDeviceInfo : mDeviceInfoList) {
            Log.d("EZDeviceInfo", ezDeviceInfo.getDeviceName() + ";;SN=" + ezDeviceInfo.getDeviceSerial() + ";; type=" + ezDeviceInfo.getDeviceType());
            //比较的自己的摄像头底部上写的序列号
            if (serialNo.equals(ezDeviceInfo.getDeviceSerial())) {
                deviceInfo = ezDeviceInfo;
                break;
            }
        }
        if(deviceInfo == null){
            return;
        }
        //获取使用的摄像头信息
        EZCameraInfo cameraInfo = getCameraInfoFromDevice(deviceInfo,0);
        // 创建播放器对象

        mEZPlayer = EZOpenSDK.getInstance().createPlayer(cameraInfo.getDeviceSerial(), cameraInfo.getCameraNo());

        if (mEZPlayer == null)
            return;
        if (cameraInfo == null) {
            return;
        }
        //设置handler，该handler被用于从播放器向handler传递消息
        mEZPlayer.setHandler(mHandler);
        // 设置播放器显示的surface
        mEZPlayer.setSurfaceHold(mRealPlaySh);
        // 开始播放
        mEZPlayer.startRealPlay();
        //视频的加密密码，默认为摄像头的验证码
        mEZPlayer.setPlayVerifyCode(devCode);

    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (mEZPlayer != null) {
            mEZPlayer.setSurfaceHold(holder);
        }
        mRealPlaySh = holder;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mEZPlayer != null) {
            mEZPlayer.setSurfaceHold(null);
        }
        mRealPlaySh = null;

    }


    private Handler mHandler = new Handler(getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_ON_DEVICE_RESPONSE:
                    startPlay();
                    break;
            }

        }
    };
}
