package com.example.wordquizgame_mvc.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.wordquizgame_mvc.db.DatabaseHelper;

import java.util.ArrayList;
import java.util.Locale;

import static com.example.wordquizgame_mvc.etc.Constants.DIFFICULTY_EASY;
import static com.example.wordquizgame_mvc.etc.Constants.DIFFICULTY_HARD;
import static com.example.wordquizgame_mvc.etc.Constants.DIFFICULTY_MEDIUM;

public class Quiz {

    private static final String TAG = Quiz.class.getName();

    private static final int NUM_QUESTIONS_PER_QUIZ = 3;

    private Context mContext;

    private int mDifficulty;
    private int mNumChoices;

    private int mCurrentQuestionNumber;
    private int mScore;
    private int mTotalGuesses;

    private ArrayList<Word> mQuestionWordList = new ArrayList<>();
    private SQLiteDatabase mDatabase;

    public Quiz(Context context, int difficulty) {
        mContext = context;
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

        DatabaseHelper dbHelper = new DatabaseHelper(mContext);
        mDatabase = dbHelper.getWritableDatabase();

        initQuiz();
    }

    private void initQuiz() {
        mScore = 0;
        mTotalGuesses = 0;

        WordLibrary wl = WordLibrary.getInstance(mContext);
        mQuestionWordList = wl.getRandomQuestionWordList(NUM_QUESTIONS_PER_QUIZ);

        Log.i(TAG, "----------");
        for (int i = 0; i < mQuestionWordList.size(); i++) {
            Log.i(TAG, "Random question word #" + i + ": " + mQuestionWordList.get(i).text);
        }
    }

    public Question nextQuestion() {
        if (mQuestionWordList.isEmpty()) {
            return null;
        }
        Word questionWord = mQuestionWordList.remove(0);
        Question question = new Question(
                mContext,
                questionWord,
                mNumChoices
        );
        mCurrentQuestionNumber++;
        return question;
    }

    public int getCurrentQuestionNumber() {
        return mCurrentQuestionNumber;
    }

    public int getNumQuestionsPerQuiz() {
        return NUM_QUESTIONS_PER_QUIZ;
    }

    public int getScore() {
        return mScore;
    }

    public void addScore() {
        mScore++;
    }

    public int getTotalGuesses() {
        return mTotalGuesses;
    }

    public void addTotalGuesses() {
        mTotalGuesses++;
    }

    public double getPercentScore() {
        return 100 * mScore / (double) mTotalGuesses;
    }

    public void savePercentScoreToDatabase() {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COL_SCORE, String.format(Locale.US, "%.1f", getPercentScore()));
        cv.put(DatabaseHelper.COL_DIFFICULTY, mDifficulty);
        mDatabase.insert(DatabaseHelper.TABLE_NAME, null, cv);
    }
}
