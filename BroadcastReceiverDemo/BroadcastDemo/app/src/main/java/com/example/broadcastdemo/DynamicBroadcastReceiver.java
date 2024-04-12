package com.example.broadcastdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class DynamicBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "DynamicBroadcastReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"我是动态注册广播 "+intent.getAction());
        Toast.makeText(context, "我是动态注册", Toast.LENGTH_SHORT).show();
    }
}
