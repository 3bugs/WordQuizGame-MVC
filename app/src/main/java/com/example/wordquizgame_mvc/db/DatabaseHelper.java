package com.example.wordquizgame_mvc.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = DatabaseHelper.class.getName();

    private static final String DATABASE_NAME = "word_quiz_game.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "scores";
    public static final String COL_ID = "_id";
    public static final String COL_SCORE = "score";
    public static final String COL_DIFFICULTY = "difficulty";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Creating table");

/*
        เทเบิล scores
        +-------+---------+--------------+
        |  _id  |  score  |  difficulty  |
        +-------+---------+--------------+
        |       |         |              |
*/
        String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_SCORE + " REAL, "
                + COL_DIFFICULTY + " INTEGER"
                + ")";
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
