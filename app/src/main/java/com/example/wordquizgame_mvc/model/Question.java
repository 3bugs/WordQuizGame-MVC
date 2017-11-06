package com.example.wordquizgame_mvc.model;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Question {

    private static final String TAG = Question.class.getName();

    private Context mContext;

    private int mNumChoices;
    private Word mQuestionWord;
    private Drawable mImageDrawable;

    private ArrayList<Word> mChoiceWordList = new ArrayList<>();

    Question(Context context, Word questionWord, int numChoices) {
        mContext = context;
        mQuestionWord = questionWord;
        mNumChoices = numChoices;

        Log.i(TAG, "----------");
        Log.i(TAG, "Question word: " + mQuestionWord.text);

        loadQuestionImage();
        prepareChoiceWords();
    }

    private void loadQuestionImage() {
        AssetManager am = mContext.getAssets();
        InputStream stream;

        try {
            stream = am.open(mQuestionWord.imageFilePath);
            mImageDrawable = Drawable.createFromStream(stream, null);
            Log.i(TAG, "โหลดไฟล์รูปภาพสำเร็จ: " + mQuestionWord.imageFilePath);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "เกิดปัญหาในการโหลดไฟล์รูปภาพ: " + mQuestionWord.imageFilePath);
        }
    }

    private void prepareChoiceWords() {
        WordLibrary wl = WordLibrary.getInstance(mContext);
        mChoiceWordList = wl.getRandomChoiceWordList(mNumChoices, mQuestionWord);

        for (int i = 0; i < mChoiceWordList.size(); i++) {
            Log.i(TAG, "Random choice word #" + i + ": " + mChoiceWordList.get(i).text);
        }
    }

    public Drawable getImageDrawable() {
        return mImageDrawable;
    }

    public ArrayList<Word> getChoiceWordList() {
        return mChoiceWordList;
    }

    public boolean checkAnswer(String guessWord) {
        return mQuestionWord.text.equals(guessWord);
    }
}
