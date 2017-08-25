package com.example.wordquizgame_mvc;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.wordquizgame_mvc.model.Question;
import com.example.wordquizgame_mvc.model.Quiz;

import java.io.IOException;
import java.util.ArrayList;

import static com.example.wordquizgame_mvc.etc.Constants.KEY_DIFFICULTY;

public class GameActivity extends AppCompatActivity {

    private static final String TAG = GameActivity.class.getSimpleName();

    private TextView mQuestionNumberTextView;
    private ImageView mQuestionImageView;
    private TextView mFeedbackTextView;
    private TableLayout mButtonTableLayout;

    private ArrayList<String> mFileNameList = new ArrayList<>();

    private Quiz mQuiz;
    private int mDifficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();
        mDifficulty = intent.getIntExtra(KEY_DIFFICULTY, 0);

        setupViews();
        getImageFileNames();
        newGame();
    }

    private void setupViews() {
        mQuestionNumberTextView = (TextView) findViewById(R.id.question_number_text_view);
        mQuestionImageView = (ImageView) findViewById(R.id.question_image_view);
        mFeedbackTextView = (TextView) findViewById(R.id.answer_text_view);
        mButtonTableLayout = (TableLayout) findViewById(R.id.button_table_layout);
    }

    private void getImageFileNames() {
        String[] categories = new String[]{"animals", "body", "colors", "numbers", "objects"};

        AssetManager am = getAssets();
        for (String c : categories) {
            try {
                String[] fileNames = am.list(c);

                for (String f : fileNames) {
                    mFileNameList.add(f.replace(".png", ""));
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "Error listing file name in " + c);
            }
        }

        Log.i(TAG, "***** รายชื่อไฟล์ภาพทั้งหมด");
        for (String f : mFileNameList) {
            Log.i(TAG, f);
        }
    }

    private void newGame() {
        mQuiz = new Quiz(
                this,
                mFileNameList,
                mDifficulty
        );
        loadNextQuestion();
    }

    private void loadNextQuestion() {
        Question question = mQuiz.nextQuestion();
        if (question == null) {
            
        }
    }
}
