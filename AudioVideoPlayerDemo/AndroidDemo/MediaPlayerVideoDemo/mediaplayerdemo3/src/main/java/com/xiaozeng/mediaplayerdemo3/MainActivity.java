package com.xiaozeng.mediaplayerdemo3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private MediaPlayer mediaPlayer ;
    private SeekBar seekBar;
    private SeekBar seek;
    Button play;
    Button pause;
    Button stop;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0){
                int mediaProcess = mediaPlayer.getCurrentPosition();
                seek.setProgress(mediaProcess);
                handler.sendEmptyMessageDelayed(0,1000);
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取button对象
        play = findViewById(R.id.btn_play);
        pause = findViewById(R.id.btn_pause);
        stop = findViewById(R.id.btn_stop);

        seek = findViewById(R.id.btn_seekto);

        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        stop.setOnClickListener(this);

        mediaPlayer = MediaPlayer.create(this,R.raw.testmusic);
        seek.setMax(mediaPlayer.getDuration());//设置seekbar的总时长


        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            //可以用来禁用seekbar的滑动功能
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                   // Toast.makeText(MainActivity.this,"当前进度:"+progress/1000,Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int seekProcess = seekBar.getProgress();
                mediaPlayer.seekTo(seekProcess);

            }
        });



    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_play:
                if(mediaPlayer !=null && !mediaPlayer.isPlaying() ){
                    mediaPlayer.start();
                    handler.sendEmptyMessageDelayed(0,1000);
                }
                break;
            case R.id.btn_pause:
                if(mediaPlayer !=null && mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                }
                break;
            case R.id.btn_stop:
                if (mediaPlayer !=null && mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                   // mediaPlayer.reset();
                    try {
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                break;
            default:
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
}