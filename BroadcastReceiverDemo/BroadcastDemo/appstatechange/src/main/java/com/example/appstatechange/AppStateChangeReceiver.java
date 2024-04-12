package com.example.appstatechange;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AppStateChangeReceiver extends BroadcastReceiver {
    public static final String TAG = "AppStateChangeReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null && action.equals(Intent.ACTION_PACKAGE_ADDED)) {
            // 应用已安装
            String packageName = intent.getData().getSchemeSpecificPart();
            Log.d(TAG,"新增了"+packageName);
        } else if (action != null && action.equals(Intent.ACTION_PACKAGE_REMOVED)) {
            // 应用已卸载
            String packageName = intent.getData().getSchemeSpecificPart();
            Log.d(TAG,"卸载了"+packageName);
        }
    }
}