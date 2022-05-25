package com.xiaozeng.mediaplayerdemo1;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Button play = findViewById(R.id.btn_play);
        Button pause = findViewById(R.id.btn_pause);
        Button stop = findViewById(R.id.btn_stop);


    }

    private void initMediaPlayer(){


    }

}