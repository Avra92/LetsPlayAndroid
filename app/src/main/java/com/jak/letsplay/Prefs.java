package com.jak.letsplay;

import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {
    private static Prefs prefs;
    private SharedPreferences preferences;

    public static Prefs getInstance(Context context) {
        if (prefs == null) {
            prefs = new Prefs(context);
        }
        return prefs;
    }

    private Prefs() {
    }

    private Prefs(Context context) {
        preferences = context.getSharedPreferences("letsplay", Context.MODE_PRIVATE);
    }

    public void put(String key, String value) {
        preferences.edit().putString(key, value).apply();
    }

    public String get(String key) {
        return preferences.getString(key, "");
    }
}
