package com.menthoven.arduinoandroid.utils;

import android.content.Context;
import android.preference.PreferenceManager;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Bilal Rashid on 5/13/2017.
 */

public class PrefUtils {
    public static boolean persistString(Context context, String key, String toPersist) {
        return PreferenceManager.getDefaultSharedPreferences(context).edit().putString(key, toPersist).commit();
    }

    public static String getString(Context context, String key) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(key, null);
    }

    public static void persistBoolean(Context context, String key, boolean toPersist) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(key, toPersist).apply();
    }

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(key, defaultValue);
    }

    public static int getInt(Context context, String key, int defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(key, defaultValue);
    }

    public static void persistInt(Context context, String key, int nextTheme) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(key, nextTheme).apply();
    }

    public static long getLong(Context context, String key, long defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getLong(key, defaultValue);
    }

    public static void persistLong(Context context, String key, long nextTime) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putLong(key, nextTime).apply();
    }

    public static void remove(Context context, String key) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().remove(key).apply();
    }

    public static void reset(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().clear().apply();
    }

    public static void persistStringSet(Context context, String key, Set<String> toPersist) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putStringSet(key, toPersist).apply();
    }

    public static Set<String> getStringSet(Context context, String key) {
        return PreferenceManager.getDefaultSharedPreferences(context).getStringSet(key, new HashSet<String>());
    }
}
