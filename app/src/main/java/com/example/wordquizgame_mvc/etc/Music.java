package com.example.wordquizgame_mvc.etc;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

public class Music {

    private static final String TAG = Music.class.getName();

    private Context mContext;
    private int mResId;
    private MediaPlayer mPlayer;

    public Music(Context context, int resId) {
        mContext = context;
        mResId = resId;
    }

    public void play() {
        mPlayer = MediaPlayer.create(mContext, mResId);
        mPlayer.setVolume(0.3f, 0.3f);
        mPlayer.setLooping(true);
        mPlayer.start();
        Log.i(TAG, "Play music");
    }

    public void stop() {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            Log.i(TAG, "Stop music");
        }
    }
}
