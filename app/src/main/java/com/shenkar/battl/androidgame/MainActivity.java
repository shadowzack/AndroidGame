package com.shenkar.battl.androidgame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class MainActivity extends Activity {

    private Button play;
    private Button playmusic;
    private static MyMusicRunnable musicPlayer;

    private void init(){
        play=(Button)findViewById(R.id.play);
        playmusic=(Button)findViewById(R.id.playmusic);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,game.class));

            }
        });

        playmusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_playmusic_text(!musicPlayer.isMusicIsPlaying());

            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        if (musicPlayer == null) {
            musicPlayer = new MyMusicRunnable(this);
        }
        set_playmusic_text(musicPlayer.isMusicIsPlaying());


    }
    private void set_playmusic_text(boolean isPlaying) {
        if (isPlaying) {
            playmusic.setText("pause music");
        } else {
            playmusic.setText("play music");
        }
    }


    static class MyMusicRunnable implements Runnable, MediaPlayer.OnCompletionListener {
        Context appContext;
        MediaPlayer mPlayer;
        boolean musicIsPlaying = false;

        public MyMusicRunnable(Context c) {
            // be careful not to leak the activity context.
            // can keep the app context instead.
            appContext = c.getApplicationContext();
        }

        public boolean isMusicIsPlaying() {
            return musicIsPlaying;
        }

        /**
         * MediaPlayer.OnCompletionListener callback
         *
         * @param mp
         */
        @Override
        public void onCompletion(MediaPlayer mp) {
            // loop back - play again
            if (musicIsPlaying && mPlayer != null) {
                mPlayer.start();
            }
        }

        /**
         * toggles the music player state
         * called asynchronously every time the play/pause button is pressed
         */
        @Override
        public void run() {

            if (musicIsPlaying) {
                mPlayer.stop();
                musicIsPlaying = false;
            } else {
                if (mPlayer == null) {
                   mPlayer = MediaPlayer.create(appContext, R.raw.test);
                    /*try {
                        mPlayer.setDataSource(appContext, Uri.parse("android.resource://com.shenkar.name/raw/song"));
                    }catch (IOException e){
                        e.printStackTrace();
                    }*/
                    mPlayer.start();
                    mPlayer.setOnCompletionListener(this); // MediaPlayer.OnCompletionListener
                } else {
                    try {
                        mPlayer.prepare();
                        mPlayer.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                musicIsPlaying = true;
            }

        }

    }

}
