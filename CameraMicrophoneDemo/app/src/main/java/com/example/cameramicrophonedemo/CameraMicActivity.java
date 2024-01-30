package com.example.cameramicrophonedemo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.qiniu.droid.rtc.QNAudioVolumeInfo;
import com.qiniu.droid.rtc.QNCameraVideoTrack;
import com.qiniu.droid.rtc.QNClientEventListener;
import com.qiniu.droid.rtc.QNConnectionDisconnectedInfo;
import com.qiniu.droid.rtc.QNConnectionState;
import com.qiniu.droid.rtc.QNCustomMessage;
import com.qiniu.droid.rtc.QNMediaRelayState;
import com.qiniu.droid.rtc.QNMicrophoneAudioTrack;
import com.qiniu.droid.rtc.QNPublishResultCallback;
import com.qiniu.droid.rtc.QNRTC;
import com.qiniu.droid.rtc.QNRTCClient;
import com.qiniu.droid.rtc.QNRTCEventListener;
import com.qiniu.droid.rtc.QNRemoteAudioTrack;
import com.qiniu.droid.rtc.QNRemoteTrack;
import com.qiniu.droid.rtc.QNRemoteVideoTrack;
import com.qiniu.droid.rtc.QNSurfaceView;
import com.qiniu.droid.rtc.model.QNAudioDevice;

import java.util.List;

public class CameraMicActivity extends AppCompatActivity {
    QNSurfaceView mLocalWindow;
    QNSurfaceView mRemoteWindow;
    QNCameraVideoTrack mCameraVideoTrack;
    QNMicrophoneAudioTrack mMicrophoneAudioTrack;
    QNRTCClient mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_mic);
        Log.v("MainActivity", "onCreate");

        // 1. 初始化视图
        initView();
        //2. 初始化RTC
        QNRTC.init(this, new QNRTCEventListener() {
            @Override
            public void onAudioRouteChanged(QNAudioDevice qnAudioDevice) {

            }
        });
        // 创建音视频通话核心类
        mClient = QNRTC.createClient(mClientEventListener);
        // 创建音视频采集Track

        mCameraVideoTrack = QNRTC.createCameraVideoTrack();
        mMicrophoneAudioTrack = QNRTC.createMicrophoneAudioTrack();
        mClient.join("QxZugR8TAhI38AiJ_cptTl3RbzLyca3t-AAiH-Hh:bqI-_X6WX6DPMl3REiv6pqDPrGY=:eyJhcHBJZCI6ImcybTB5YTd3NyIsImV4cGlyZUF0IjoxNzA0Mjc5NzQ2LCJwZXJtaXNzaW9uIjoidXNlciIsInJvb21OYW1lIjoiemVuZyIsInVzZXJJZCI6IjIzNDUifQ==");
        mLocalWindow = findViewById(R.id.local_surface_view);
        mRemoteWindow = findViewById(R.id.remote_surface_view);
        //渲染
        mCameraVideoTrack.play(mLocalWindow);

    }

    private final QNClientEventListener mClientEventListener = new QNClientEventListener() {
        @Override
        public void onConnectionStateChanged(QNConnectionState qnConnectionState, @Nullable QNConnectionDisconnectedInfo qnConnectionDisconnectedInfo) {
            if (qnConnectionState == QNConnectionState.CONNECTED) {
                mClient.publish(new QNPublishResultCallback() {
                    @Override
                    public void onPublished() {
                        // Track 发布成功时回调
                    }

                    @Override
                    public void onError(int errorCode, String errorMessage) {
                        // Track 发布失败时回调
                    }
                }, mCameraVideoTrack, mMicrophoneAudioTrack);
            }
        }

        @Override
        public void onUserJoined(String s, String s1) {

        }

        @Override
        public void onUserReconnecting(String s) {

        }

        @Override
        public void onUserReconnected(String s) {

        }

        @Override
        public void onUserLeft(String s) {

        }

        @Override
        public void onUserPublished(String s, List<QNRemoteTrack> list) {

        }

        @Override
        public void onUserUnpublished(String s, List<QNRemoteTrack> list) {

        }

        @Override
        public void onSubscribed(String s, List<QNRemoteAudioTrack> list, List<QNRemoteVideoTrack> remoteVideoTracks) {
            for (QNRemoteVideoTrack track : remoteVideoTracks){
                track.play(mRemoteWindow);
            }

        }

        @Override
        public void onMessageReceived(QNCustomMessage qnCustomMessage) {

        }

        @Override
        public void onMediaRelayStateChanged(String s, QNMediaRelayState qnMediaRelayState) {

        }

        @Override
        public void onUserVolumeIndication(List<QNAudioVolumeInfo> list) {

        }
    };


    private void initView() {
        // 初始化本地预览视图
        mLocalWindow = findViewById(R.id.local_surface_view);
        mLocalWindow.setZOrderOnTop(true);
        // 初始化远端预览视图
        mRemoteWindow = findViewById(R.id.remote_surface_view);
        mRemoteWindow.setZOrderOnTop(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 开始视频采集
        if (mCameraVideoTrack != null) {
            mCameraVideoTrack.startCapture();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 停止视频采集
        if (mCameraVideoTrack != null) {
            mCameraVideoTrack.stopCapture();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mClient != null) {
            mClient.leave();
            mClient = null;
        }
        QNRTC.deinit();
    }


}