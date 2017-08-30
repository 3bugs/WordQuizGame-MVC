package com.example.wordquizgame_mvc;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.wordquizgame_mvc.etc.Music;
import com.example.wordquizgame_mvc.model.Question;
import com.example.wordquizgame_mvc.model.Quiz;

import java.io.IOException;
import java.util.ArrayList;

import static com.example.wordquizgame_mvc.etc.Constants.KEY_DIFFICULTY;

public class GameActivity extends AppCompatActivity {

    private static final String TAG = GameActivity.class.getName();

    private TextView mQuestionNumberTextView;
    private ImageView mQuestionImageView;
    private TextView mFeedbackTextView;
    private TableLayout mButtonTableLayout;

    private ArrayList<String> mFileNameList = new ArrayList<>();

    private int mDifficulty;
    private Quiz mQuiz;
    private Question mCurrentQuestion;

    private Handler mHandler = new Handler();
    private Animation mShakeAnimation;
    private Music mMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();
        mDifficulty = intent.getIntExtra(KEY_DIFFICULTY, 0);

        mShakeAnimation = AnimationUtils.loadAnimation(this, R.anim.shake);
        mShakeAnimation.setRepeatCount(3);

        mMusic = new Music(this, R.raw.game);

        setupViews();
        getImageFileNames();
        newGame();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMusic.play();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMusic.stop();
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

        for (int i = 0; i < mFileNameList.size(); i++) {
            Log.i(TAG, "Image file name #" + i + ": " + mFileNameList.get(i));
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
        mCurrentQuestion = mQuiz.nextQuestion();

        if (mCurrentQuestion == null) {
            double percentScore = mQuiz.getPercentScore();
            mQuiz.savePercentScoreToDatabase();

            String dialogMsg = getString(R.string.total_guesses_label, mQuiz.getTotalGuesses());
            dialogMsg += "\n" + getString(R.string.success_percentage_label, percentScore);

            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.result_title))
                    .setMessage(dialogMsg)
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.restart_quiz_label),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    newGame();
                                }
                            })
                    .setNegativeButton(getString(R.string.return_to_menu_label),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                    .show();
        } else {
            mFeedbackTextView.setText(null);

            String msg = getString(
                    R.string.question_number_label,
                    mQuiz.getCurrentQuestionNumber(),
                    mQuiz.getNumQuestionsPerQuiz()
            );
            mQuestionNumberTextView.setText(msg);

            mQuestionImageView.setImageDrawable(mCurrentQuestion.getImageDrawable());
            createChoiceButtons();
        }
    }

    private void createChoiceButtons() {
        for (int row = 0; row < mButtonTableLayout.getChildCount(); row++) {
            TableRow tr = (TableRow) mButtonTableLayout.getChildAt(row);
            tr.removeAllViews();
        }

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int row = 0; row < mCurrentQuestion.getChoiceWordList().size() / 2; row++) {
            TableRow tr = (TableRow) mButtonTableLayout.getChildAt(row);

            for (int column = 0; column < 2; column++) {
                Button guessButton = (Button) inflater.inflate(R.layout.guess_button, tr, false);
                guessButton.setText(mCurrentQuestion.getChoiceWordList().get(row * 2 + column));
                guessButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        submitGuess((Button) v);
                    }
                });
                tr.addView(guessButton);
            }
        }
    }

    private void submitGuess(Button guessButton) {
        Log.i(TAG, "You selected " + guessButton.getText().toString());

        mQuiz.addTotalGuesses();
        String guessWord = guessButton.getText().toString();

        // ตอบถูก
        if (mCurrentQuestion.checkAnswer(guessWord)) {
            mQuiz.addScore();
            disableAllButtons();

            MediaPlayer mp = MediaPlayer.create(this, R.raw.applause);
            mp.start();

            String msg = guessWord + " " + getString(R.string.correct_label);
            mFeedbackTextView.setText(msg);
            mFeedbackTextView.setTextColor(
                    ContextCompat.getColor(this, android.R.color.holo_green_dark)
            );

            mHandler.postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            loadNextQuestion();
                        }
                    },
                    2000
            );
        }
        // ตอบผิด
        else {
            guessButton.setEnabled(false);

            MediaPlayer mp = MediaPlayer.create(this, R.raw.fail);
            mp.start();

            String msg = getString(R.string.incorrect_label);
            mFeedbackTextView.setText(msg);
            mFeedbackTextView.setTextColor(
                    ContextCompat.getColor(this, android.R.color.holo_red_dark)
            );

            mQuestionImageView.startAnimation(mShakeAnimation);
        }
    }

    private void disableAllButtons() {
        for (int row = 0; row < mButtonTableLayout.getChildCount(); row++) {
            TableRow tr = (TableRow) mButtonTableLayout.getChildAt(row);

            for (int column = 0; column < tr.getChildCount(); column++) {
                Button b = (Button) tr.getChildAt(column);
                b.setEnabled(false);
            }
        }
    }
}
