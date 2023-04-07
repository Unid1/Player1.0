package com.example.player10;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;


public class MainActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    // создание полей
    private final String DATA_STREAM = "https://mp3melodii.ru/files_site_02/001/veselaya_melodiya_iz_peredachi_kalambur_derevnya_durakov.mp3";
    private String nameAudio = "";
    private final String DATA_SD = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC) + "/music.mp3";

    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;
    private Toast toast;

    private TextView textOut;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch switchLoop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        textOut = findViewById(R.id.textOut);
        switchLoop = findViewById(R.id.switchLoop);


        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);



        switchLoop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(mediaPlayer != null)
                    mediaPlayer.setLooping(isChecked);
            }
        });
    }

    // слушатель нажатия радио-кнопок
    public void onClickSource(View view) {

        releaseMediaPlayer();


        try {
            switch (view.getId()) {
                case R.id.btnStream:

                    toast = Toast.makeText(this, "Запущен поток аудио", Toast.LENGTH_SHORT);
                    toast.show();
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(DATA_STREAM);
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.setOnPreparedListener(this);
                    mediaPlayer.prepareAsync();
                    nameAudio = "веселая мелодия";
                    break;
                case R.id.btnRAW:

                    toast = Toast.makeText(this, "Запущен аудио-файл с памяти телефона", Toast.LENGTH_SHORT);
                    toast.show();
                    mediaPlayer = MediaPlayer.create(this, R.raw.musicc);
                    mediaPlayer.start();
                    nameAudio = "подлая еврейская музыка";
                    break;
                case R.id.btnSD:
                    toast = Toast.makeText(this, "Запущен аудио-файл с SD", Toast.LENGTH_SHORT);
                    toast.show();
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(DATA_SD);
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.prepare();
                    mediaPlayer.start();

                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
            toast = Toast.makeText(this, "Источник информации не найден", Toast.LENGTH_SHORT);
            toast.show();
        }

        if (mediaPlayer == null) return;

        mediaPlayer.setLooping(switchLoop.isChecked());
        mediaPlayer.setOnCompletionListener(this);
    }

    public void onClick(View view) {
        if (mediaPlayer == null) return;

        switch (view.getId()) {
            case R.id.btnResume:
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                }
                break;
            case R.id.btnPause:
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    //Log.d("MyLog", "mediaPlayer.pause()");
                }
                break;
            case R.id.btnStop:
                mediaPlayer.stop();
                break;
            case R.id.btnForward:
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 5000);

                break;
            case R.id.btnBack:
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 5000);
                break;
        }

        textOut.setText(nameAudio + "\n(проигрывание " + mediaPlayer.isPlaying() + ", время " + mediaPlayer.getCurrentPosition()
                + ",\nповтор " + mediaPlayer.isLooping() + ", громкость " + audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) + ")");
    }



    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
        toast = Toast.makeText(this, "Старт медиа-плейера", Toast.LENGTH_SHORT);
        toast.show();
    }


    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        toast = Toast.makeText(this, "Отключение медиа-плейера", Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMediaPlayer();
    }


    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.release();
                mediaPlayer = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}