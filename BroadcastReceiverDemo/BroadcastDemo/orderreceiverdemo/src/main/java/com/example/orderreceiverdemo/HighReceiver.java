package com.example.orderreceiverdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class HighReceiver extends BroadcastReceiver {
    private static final String TAG = "HighReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"Highaction is ===>"+intent.getAction());
        //abortBroadcast();
        Bundle resultExtras = getResultExtras(true);
        String content = resultExtras.getCharSequence("content").toString();
        Log.d(TAG,"content ===>"+content);
        resultExtras.putCharSequence("content","我是被修改的内容");
        setResultExtras(resultExtras);

    }
}