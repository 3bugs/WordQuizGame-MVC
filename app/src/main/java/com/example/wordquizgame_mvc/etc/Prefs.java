package com.example.wordquizgame_mvc.etc;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Set;

/**
 * Created by Promlert on 2018-01-17.
 */

public class Prefs {

    public static String getCharCasePrefValue(Context context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPrefs.getString("pref_char_case", null);
    }

    public static Set<String> getCategoriesPrefValue(Context context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPrefs.getStringSet("pref_categories", null);
    }
}
