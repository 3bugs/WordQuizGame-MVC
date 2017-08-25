package com.example.wordquizgame_mvc.model;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Promlert on 2017-08-25.
 */

public class Question {

    private static final String TAG = Question.class.getSimpleName();

    private Context mContext;

    private int mNumChoices;
    private String mFileName;
    private Drawable mImageDrawable;

    private ArrayList<String> mFileNameList;
    private ArrayList<String> mChoiceWordList = new ArrayList<>();

    private Random mRandom = new Random();

    public Question(Context context, ArrayList<String> fileNameList, String fileName, int numChoices) {
        mContext = context;
        mFileNameList = fileNameList;
        mFileName = fileName;
        mNumChoices = numChoices;

        loadQuestionImage();
        prepareChoiceWords();
    }

    private void loadQuestionImage() {
        String category = mFileName.substring(
                0,
                mFileName.indexOf('-')
        );

        String filePath = category + "/" + mFileName + ".png";

        AssetManager am = mContext.getAssets();
        InputStream stream;

        try {
            stream = am.open(filePath);
            mImageDrawable = Drawable.createFromStream(stream, null);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Error loading image file: " + filePath);
        }
    }

    private void prepareChoiceWords() {
        mChoiceWordList.clear();
        String answerWord = getWord(mFileName);

        while (mChoiceWordList.size() < mNumChoices) {
            int randomIndex = mRandom.nextInt(mFileNameList.size());
            String randomWord = getWord(mFileNameList.get(randomIndex));

            if (!mChoiceWordList.contains(randomWord) && !answerWord.equals(randomWord)) {
                mChoiceWordList.add(randomWord);
            }
        }

        int randomIndex = mRandom.nextInt(mChoiceWordList.size());
        mChoiceWordList.set(randomIndex, answerWord);

        Log.i(TAG, "***** คำศัพท์ตัวเลือกที่สุ่มได้");
        for (String w : mChoiceWordList) {
            Log.i(TAG, w);
        }
    }

    private String getWord(String fileName) {
        return fileName.substring(fileName.indexOf('-') + 1);
    }

    public Drawable getImageDrawable() {
        return mImageDrawable;
    }

    public ArrayList<String> getChoiceWordList() {
        return mChoiceWordList;
    }
}
