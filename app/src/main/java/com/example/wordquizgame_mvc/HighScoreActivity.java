package com.example.wordquizgame_mvc;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.wordquizgame_mvc.databinding.ActivityHighScoreBinding;
import com.example.wordquizgame_mvc.db.DatabaseHelper;

import java.util.Arrays;

public class HighScoreActivity extends AppCompatActivity {

    private static final String TAG = HighScoreActivity.class.getName();

    private ActivityHighScoreBinding mBinding;

    private SQLiteDatabase mDatabase;
    private SimpleCursorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_high_score);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        mDatabase = dbHelper.getWritableDatabase();

        mBinding.highScoreListView.setEmptyView(mBinding.empty);

        setListAdapter();

        // กำหนดข้อความปุ่มเรดิโอ
        mBinding.easyRadioButton.setText(R.string.easy_label);
        mBinding.mediumRadioButton.setText(R.string.medium_label);
        mBinding.hardRadioButton.setText(R.string.hard_label);

        // กำหนด Checked Change Listener ให้กับ RadioGroup
        mBinding.difficultyRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String[] diffLabels = getResources().getStringArray(R.array.difficulty_labels);
                String selectedRadioLabel = ((RadioButton) findViewById(checkedId)).getText().toString();
                int diffIndex = Arrays.asList(diffLabels).indexOf(selectedRadioLabel);
                showHighScoreByDifficulty(diffIndex);
            }
        });

        // เลือกปุ่มเรดิโอ "ง่าย" เป็นค่าเริ่มต้น
        mBinding.easyRadioButton.setChecked(true);
    }

    void setListAdapter() {
        final String[] columns = {
                DatabaseHelper.COL_SCORE
        };
        final int[] views = {
                R.id.score_text_view
        };

        mAdapter = new SimpleCursorAdapter(this, R.layout.high_score_row, null, columns, views, 0);
        mBinding.highScoreListView.setAdapter(mAdapter);
    }

    void showHighScoreByDifficulty(int difficulty) {
/*
        final String sqlSelect = "SELECT * FROM " + DatabaseHelper.TABLE_NAME +
                " WHERE " + DatabaseHelper.COL_DIFFICULTY + "=" + difficulty +
                " ORDER BY " + DatabaseHelper.COL_SCORE + " DESC LIMIT 5";
        Cursor cursor = mDatabase.rawQuery(sqlSelect, null);
*/

        Cursor cursor = mDatabase.query(
                DatabaseHelper.TABLE_NAME,
                null,
                DatabaseHelper.COL_DIFFICULTY + "=?",
                new String[]{String.valueOf(difficulty)},
                null,
                null,
                DatabaseHelper.COL_SCORE + " DESC",
                "5"
        );
        mAdapter.changeCursor(cursor);
    }
}
