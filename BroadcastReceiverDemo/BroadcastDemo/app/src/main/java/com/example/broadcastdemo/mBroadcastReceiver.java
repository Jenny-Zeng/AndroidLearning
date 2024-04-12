package com.example.broadcastdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class mBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "mBroadcastReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"我是静态注册广播 "+intent.getAction());
        Toast.makeText(context, "我是静态注册", Toast.LENGTH_SHORT).show();

    }
}
