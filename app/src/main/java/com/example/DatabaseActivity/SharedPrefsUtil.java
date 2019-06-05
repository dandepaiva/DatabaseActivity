package com.example.DatabaseActivity;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * util  to manage {@link SharedPreferences#edit()}
 */
public class SharedPrefsUtil {

    /**
     * Desired preferences file name
     */
    public static final String PREFERENCES_NAME = "saved preferences";

    /**
     * Key used to save the boolean value in shared preferences
     */
    public static final String PREFS_KEY_INSERT = "com.example.DatabaseActivity.insert";

    /**
     *  if the product has been inserted
     * @return true, if so
     */
    public static boolean isInserted() {
        SharedPreferences sharedPref = MyApplication.getContext().getSharedPreferences(
                PREFERENCES_NAME, Context.MODE_PRIVATE);

        boolean isInserted = sharedPref.getBoolean(PREFS_KEY_INSERT, false);

        return isInserted;
    }

    /**
     * makes isInserted true and communicates it to sharedPreferences
     */
    public static void updateIsInserted() {
        SharedPreferences.Editor editor = MyApplication.getContext().getSharedPreferences(
                PREFERENCES_NAME, Context.MODE_PRIVATE).edit();

        editor.putBoolean(PREFS_KEY_INSERT, true);
        editor.apply();

    }
}
