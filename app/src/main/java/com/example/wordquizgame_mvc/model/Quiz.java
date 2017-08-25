package com.example.wordquizgame_mvc.model;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import static com.example.wordquizgame_mvc.etc.Constants.DIFFICULTY_EASY;
import static com.example.wordquizgame_mvc.etc.Constants.DIFFICULTY_HARD;
import static com.example.wordquizgame_mvc.etc.Constants.DIFFICULTY_MEDIUM;

/**
 * Created by Promlert on 2017-08-25.
 */

public class Quiz {

    private static final String TAG = Quiz.class.getSimpleName();

    private static final int NUM_QUESTIONS_PER_QUIZ = 3;

    private Context mContext;

    private int mDifficulty;
    private int mNumChoices;

    private int mScore;
    private int mTotalGuesses;

    private ArrayList<String> mFileNameList;
    private ArrayList<String> mQuizWordList = new ArrayList<>();

    private Random mRandom = new Random();

    public Quiz(Context context, ArrayList<String> fileNameList, int difficulty) {
        mContext = context;
        mFileNameList = fileNameList;
        mDifficulty = difficulty;

        switch (mDifficulty) {
            case DIFFICULTY_EASY:
                mNumChoices = 2;
                break;
            case DIFFICULTY_MEDIUM:
                mNumChoices = 4;
                break;
            case DIFFICULTY_HARD:
                mNumChoices = 6;
                break;
        }

        startQuiz();
    }

    private void startQuiz() {
        mScore = 0;
        mTotalGuesses = 0;
        mQuizWordList.clear();

        while (mQuizWordList.size() < NUM_QUESTIONS_PER_QUIZ) {
            int randomIndex = mRandom.nextInt(mFileNameList.size());
            String fileName = mFileNameList.get(randomIndex);

            if (!mQuizWordList.contains(fileName)) {
                mQuizWordList.add(fileName);
            }
        }

        Log.i(TAG, "***** รายชื่อไฟล์คำถามที่สุ่มได้");
        for (String f : mQuizWordList) {
            Log.i(TAG, f);
        }
    }

    public Question nextQuestion() {
        if (mQuizWordList.isEmpty()) {
            return null;
        }
        String questionFileName = mQuizWordList.remove(0);
        Question question = new Question(
                mContext,
                mFileNameList,
                questionFileName,
                mNumChoices
        );
        return question;
    }

    public int getScore() {
        return mScore;
    }

    public int getTotalGuesses() {
        return mTotalGuesses;
    }
}
