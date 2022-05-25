package com.xiaozeng.videoplayerdemo3;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback{
    private Button play,pause,stop;
    private boolean onPlay = true;
    private MediaPlayer mediaPlayer;
    private SurfaceHolder surfaceHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏显示

        SurfaceView surfaceView = findViewById(R.id.surfaceView);
        surfaceView.getHolder().addCallback(this);

        surfaceHolder = surfaceView.getHolder(); //主要是将Surfaceview与mediaplayer关联起来
        mediaPlayer = new MediaPlayer();

        // mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);//设置多媒体类型

        //设置完成事件监听器
       /* mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Toast.makeText(MainActivity.this,"视频播放完毕",
                        Toast.LENGTH_SHORT).show();

            }
        });*/

        ///控制视频的播放、暂停、停止

        play = findViewById(R.id.but_play);
        pause = findViewById(R.id.but_pause);
        stop = findViewById(R.id.but_stop);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onPlay){
                    play();
                    onPlay=false;//表示视频处于播放状态
                }else {
                    mediaPlayer.start();
                }

            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();

                }

            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                    onPlay = true;
                }
            }
        });
    }

    //播放方法
    public void play(){
        mediaPlayer.reset();//重置mediaplayer对象
        mediaPlayer.setDisplay(surfaceHolder);//把视频画面
        try {
            mediaPlayer.setDataSource(Environment.getExternalStorageDirectory() + "/out.mp4");
            mediaPlayer.prepare();
        }catch (Exception e){
            e.printStackTrace();
        }
        mediaPlayer.start();


    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer != null){
            if (mediaPlayer.isPlaying()){
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            // mediaPlayer = null;

        }

    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        //启动绘图的线程。
       mediaPlayer.setDisplay(surfaceHolder);
       play(); //起播播放
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        //surface尺寸发生改变的时候调用，如横竖屏切换。

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        /*mediaPlayer.release();
        mediaPlayer=null;*/
    }
}