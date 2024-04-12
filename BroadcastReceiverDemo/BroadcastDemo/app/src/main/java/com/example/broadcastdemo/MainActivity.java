package com.example.broadcastdemo;
/*
本示例为静态注册广播Demo
 */

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    DynamicBroadcastReceiver dynamicBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        regist();
    }

    private void regist() {
        dynamicBroadcastReceiver = new DynamicBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("MY_RECEIVER1");
        registerReceiver(dynamicBroadcastReceiver,intentFilter);
    }

    /*
    静态广播注册案例
     */
    public void SendBroadcast(View view){
        Intent intent = new Intent("MY_RECEIVER");
        intent.setPackage(getPackageName());
        sendBroadcast(intent);
    }
    /*
    动态广播注册
     */
    public void DynamicBroadcast(View view){

        Intent intent = new Intent("MY_RECEIVER1");
        sendBroadcast(intent);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(dynamicBroadcastReceiver);
    }

}