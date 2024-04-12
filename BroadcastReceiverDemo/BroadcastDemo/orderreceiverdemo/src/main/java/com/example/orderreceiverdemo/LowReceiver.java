package com.example.orderreceiverdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class LowReceiver extends BroadcastReceiver {
    private static final String TAG = "LowReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"lowaction is ===>"+intent.getAction());
        Bundle resultExtras = getResultExtras(true);
        String content = resultExtras.getCharSequence("content").toString();
        Log.d(TAG,"content ===>"+content);
    }
}