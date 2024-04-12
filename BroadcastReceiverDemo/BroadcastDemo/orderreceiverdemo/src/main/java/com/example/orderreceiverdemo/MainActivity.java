package com.example.orderreceiverdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

    }
    public void SendBroadcast(View view){
        Intent intent = new Intent("orderReceiver");

        //封装数据
        Bundle bundle = new Bundle();
        bundle.putCharSequence("content","我是发送的内容");////将输入内容也传递过去
        //Android8.0以后，静态注册的BroadcastReceiver是无法接收隐式广播的，而默认情况下我们发出的自定义广播恰恰都是隐式广播。
        //因此这里一定要调用setPackage()方法，指定这条广播是发送给那个应用程序的，从而让它变成一条显示广播。否则，静态注册的BroadcastReceiver将无法接收这条广播。
        intent.setPackage(getPackageName());
        sendOrderedBroadcast(intent,null,null,null,Activity.RESULT_OK,null,bundle);



    }


}