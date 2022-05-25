package com.xiaozeng.videoplayerdemo1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
   private VideoView videoView;
   private Uri uri;
   MediaController mediaController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取控件对象
        videoView = findViewById(R.id.video_view);
        Button play = findViewById(R.id.play);
        Button pause = findViewById(R.id.pause);
        Button replay = findViewById(R.id.replay);
        //监听事件
        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        replay.setOnClickListener(this);

        //动态获取权限
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        }else {
            initVideoPath();//初始化MediaPlayer

        }
        //initVideoPath();

    }
    private void initVideoPath(){
        /*
        // 加载指定的视频文件
        File file = new File(Environment.getExternalStorageDirectory(),"/out.mp4");
        videoView.setVideoPath(file.getPath());//指定视频文件的路径

        */
        uri = Uri.parse("https://img.qunliao.info/4oEGX68t_9505974551.mp4");
        videoView.setVideoURI(uri);

       /* mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        videoView.requestFocus();*/



        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
    }


    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initVideoPath();

                } else {
                    Toast.makeText(this, "拒绝权限，无法使用程序。", Toast.LENGTH_LONG).show();

                    finish();
                }
                break;

            default:

                break;
        }

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.play:
                if(!videoView.isPlaying()){
                    videoView.start();
                }
                break;
            case R.id.pause:
                if(videoView.isPlaying()){
                    videoView.pause();
                }
                break;
            case R.id.replay:
                if(videoView.isPlaying()){
                    videoView.resume();
                }
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(videoView != null ){
            videoView.suspend();
        }
    }

}