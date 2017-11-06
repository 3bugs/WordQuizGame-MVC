package com.example.wordquizgame_mvc.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

class WordLibrary {

    private static final String TAG = WordLibrary.class.getName();

    @SuppressLint("StaticFieldLeak")
    private static WordLibrary sInstance;
    private Context mApplicationContext;
    private final ArrayList<Word> mWordList = new ArrayList<>();

    static WordLibrary getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new WordLibrary(context);
        }
        return sInstance;
    }

    private WordLibrary(Context context) {
        // Get application-level context to avoid memory leak
        mApplicationContext = context.getApplicationContext();
        loadImageFileNames();
    }

    private void loadImageFileNames() {
        String[] categories = new String[]{"animals", "body", "colors", "numbers", "objects"};

        AssetManager am = mApplicationContext.getAssets();
        for (String c : categories) {
            try {
                String[] fileNames = am.list(c);

                for (String f : fileNames) {
                    Word word = new Word(c, f);
                    mWordList.add(word);
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "เกิดข้อผิดพลาดในการอ่านรายชื่อไฟล์รูปภาพจากหมวด " + c);
            }
        }

        for (int i = 0; i < mWordList.size(); i++) {
            Word word = mWordList.get(i);
            String msg = String.format(
                    Locale.getDefault(),
                    "Word #%d: %s, Image file path: %s",
                    i, word.text, word.imageFilePath
            );
            Log.i(TAG, msg);
        }
    }

    ArrayList<Word> getRandomQuestionWordList(int numQuestions) {
        ArrayList<Word> questionWordList = new ArrayList<>();

        Random random = new Random();

        while (questionWordList.size() < numQuestions) {
            int randomIndex = random.nextInt(mWordList.size());
            Word randomWord = mWordList.get(randomIndex);

            if (!questionWordList.contains(randomWord)) {
                questionWordList.add(randomWord);
            }
        }

        return questionWordList;
    }

    ArrayList<Word> getRandomChoiceWordList(int numChoices, Word answerWord) {
        ArrayList<Word> choiceWordList = new ArrayList<>();

        Random random = new Random();

        while (choiceWordList.size() < numChoices) {
            int randomIndex = random.nextInt(mWordList.size());
            Word randomWord = mWordList.get(randomIndex);

            if (!choiceWordList.contains(randomWord) && !answerWord.equals(randomWord)) {
                choiceWordList.add(randomWord);
            }
        }

        int randomIndex = random.nextInt(choiceWordList.size());
        choiceWordList.set(randomIndex, answerWord);

        return choiceWordList;
    }
}
