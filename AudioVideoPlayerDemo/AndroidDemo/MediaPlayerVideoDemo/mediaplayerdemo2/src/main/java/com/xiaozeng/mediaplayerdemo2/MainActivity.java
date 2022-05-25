package com.xiaozeng.mediaplayerdemo2;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity implements SeekBar.OnSeekBarChangeListener {
    public String mMusicPath;
    private SeekBar     sb;
    private MediaPlayer player;
    private int         duration;
    // 播放器的几个状态
    private static final int PLAYING  = 1;// 播放状态
    private static final int PAUSING  = 2;// 暂停状态
    private static final int STOPPING = 3;// 停止状态
    private volatile     int CURRENT  = 0;// 当前状态
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkMusicFile();
        sb = (SeekBar) findViewById(R.id.sb);
        //设置拖动监听
        sb.setOnSeekBarChangeListener(this);
    }

    //播放
    @SuppressLint("WrongConstant")
    public void play(View view) {
        if (player != null) {
            if (CURRENT == PLAYING) {
                Toast.makeText(this, "音乐已经在播放了", Toast.LENGTH_SHORT).show();
                return;
            } else if (CURRENT == PAUSING) {
                player.start();
                CURRENT = PLAYING;
                return;
            }
        }
        try {
            //创建一个播放器对象
            player = new MediaPlayer();
            //给播放器设置音乐路径
            player.setDataSource(mMusicPath);
            //设置音乐格式
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            //准备
            player.prepare();
            //获取音乐最大长度（毫秒单位）
            duration = player.getDuration();
            //给SeekBar 设置最大值
            sb.setMax(duration);
            //音乐开始播放
            player.start();
            //设置当前的状态为播放
            CURRENT = PLAYING;
            if (timer == null) {
                //创建定时器
                timer = new Timer();
            }
            /**
             * 参数1：匿名内部类，相当于Runnable 类
             * 参数2：第一次延时多长时间（毫秒）后执行，0 则代表立即执行
             * 参数3：每隔多长时间(毫秒)执行一次
             */
            timer.schedule(new TimerTask() {

                @Override
                public void run() {//该方法每1 秒被调用一次
                    if (CURRENT == PLAYING) {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                //双重判断，尽可能避免线程问题，因为该段代码时在主线程中的，
                                //第一次判断是在子线程中进行的
                                if (player != null && CURRENT == PLAYING) {
                                    //获取当前的播放进度
                                    int currentPosition = player.getCurrentPosition();
                                    //设置给SeekBar
                                    sb.setProgress(currentPosition);
                                }
                            }
                        });
                    }

                }
            }, 0, 1000);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "音乐播放失败" + e, 0).show();
        }
    }

    /**
     * 暂停
     */
    public void pause(View view) {
        if (player != null && CURRENT == PLAYING) {
            player.pause();
            CURRENT = PAUSING;
        }
    }

    /**
     * 停止
     */
    public void stop(View view) {
        if (player != null) {
            if (CURRENT == PLAYING || CURRENT == PAUSING) {
                CURRENT = STOPPING;
                //取消定时器
                timer.cancel();
                timer = null;
                player.stop();
                player.reset();
                player.release();
                player = null;
                sb.setProgress(0);
            }
        }
    }

    /*
     * 拖动过程中回调多次
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (player == null) {
            sb.setProgress(0);
        } else {
            player.seekTo(progress);
        }
    }

    /*
     * 开始拖动前回调一次
     */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if (player == null) {
            Toast.makeText(this, "音乐播放器还未开始", Toast.LENGTH_SHORT).show();
        }
    }

    /*
     * 结束拖动后回调一次
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stop(null);
    }

    private void checkMusicFile() {
        try {
            mMusicPath = getExternalFilesDir(Environment.DIRECTORY_MUSIC) + File.separator + "testmusic.mp3";
            File musicFile = new File((String) mMusicPath);
            if (musicFile.exists()) {
                return;
            }

            InputStream is = getAssets().open("testmusic.mp3");
            FileOutputStream fos = new FileOutputStream( mMusicPath);
            byte[] buffer = new byte[1024];
            int byteCount;
            while ((byteCount = is.read(buffer)) != -1) {
                fos.write(buffer, 0, byteCount);
            }
            fos.flush();
            is.close();
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}