package com.example.wordquizgame_mvc;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wordquizgame_mvc.etc.Music;

import static com.example.wordquizgame_mvc.etc.Constants.KEY_DIFFICULTY;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();

    private Music mMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");

        setContentView(R.layout.activity_main);

        Button playGameButton = (Button) findViewById(R.id.play_game_button);
        playGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Hello log");

                Toast t = Toast.makeText(
                        MainActivity.this,
                        "Hello Toast",
                        Toast.LENGTH_SHORT
                );
                t.show();

                //showPlainChooseDifficultyDialog();
                showCustomChooseDifficultyDialog();
            }
        });

        Button highScoreButton = (Button) findViewById(R.id.high_score_button);
        highScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, HighScoreActivity.class);
                startActivity(i);
            }
        });

        mMusic = new Music(this, R.raw.main);
    }

    private void showPlainChooseDifficultyDialog() {
        final String[] diffLabels = getResources().getStringArray(R.array.difficulty_labels);

        new AlertDialog.Builder(MainActivity.this)
                .setTitle(getString(R.string.choose_difficulty_title))
                .setIcon(R.drawable.ic_abc)
                .setItems(diffLabels, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(TAG, "คุณเลือก: " + diffLabels[which]);

                        Intent i = new Intent(MainActivity.this, GameActivity.class);
                        i.putExtra(KEY_DIFFICULTY, which);
                        startActivity(i);
                    }
                })
                .show();
    }

    private void showCustomChooseDifficultyDialog() {
        final String[] diffLabels = getResources().getStringArray(R.array.difficulty_labels);

        DifficultyOptionsAdapter adapter = new DifficultyOptionsAdapter(
                this,
                R.layout.difficulty_row,
                diffLabels
        );

        new AlertDialog.Builder(MainActivity.this)
                .setTitle(getString(R.string.choose_difficulty_title))
                .setIcon(R.drawable.ic_abc)
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainActivity.this, GameActivity.class);
                        intent.putExtra(KEY_DIFFICULTY, which);
                        startActivity(intent);
                    }
                })
                .show();
    }

    private static class DifficultyOptionsAdapter extends ArrayAdapter<String> {

        private Context mContext;
        private int mItemLayoutId;
        private String[] mDifficulties;

        DifficultyOptionsAdapter(Context context, int itemLayoutId, String[] difficulties) {
            super(context, itemLayoutId, difficulties);

            this.mContext = context;
            this.mItemLayoutId = itemLayoutId;
            this.mDifficulties = difficulties;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater)
                    mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View itemView = convertView;
            if (itemView == null) {
                assert inflater != null;
                itemView = inflater.inflate(mItemLayoutId, parent, false);
            }

            TextView difficultyTextView = (TextView) itemView.findViewById(R.id.difficulty_text_view);
            ImageView difficultyImageView = (ImageView) itemView.findViewById(R.id.difficulty_image_view);

            String diff = mDifficulties[position];
            difficultyTextView.setText(diff);

            if (diff.equals(mContext.getString(R.string.easy_label))) {
                difficultyImageView.setImageResource(R.drawable.dog_easy);
            } else if (diff.equals(mContext.getString(R.string.medium_label))) {
                difficultyImageView.setImageResource(R.drawable.dog_medium);
            } else if (diff.equals(mContext.getString(R.string.hard_label))) {
                difficultyImageView.setImageResource(R.drawable.dog_hard);
            }

            return itemView;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
        mMusic.play();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
        mMusic.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
