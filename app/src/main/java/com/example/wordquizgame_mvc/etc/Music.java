package com.example.wordquizgame_mvc.etc;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

public class Music {

    private static final String TAG = Music.class.getName();

    private MediaPlayer mPlayer;

    public Music(Context context, int resId) {
        mPlayer = MediaPlayer.create(context, resId);
        mPlayer.setVolume(0.3f, 0.3f);
        mPlayer.setLooping(true);
    }

    public void play() {
        //stop();
        if (mPlayer != null) {
            mPlayer.start();
            Log.i(TAG, "Play music");
        }
    }

    public void stop() {
        if (mPlayer != null) {
            mPlayer.pause();
            Log.i(TAG, "Stop music");
        }
    }
}
