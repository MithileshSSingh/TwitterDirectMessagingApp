package com.example.mithilesh.twitterdirectmessageapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPref {

    private static final String preferanceName = "NPVISIT_CUSTOMER";
    private SharedPreferences sp = null;

    private static AppPref INSTANCE = null;

    private AppPref(Context context) {
        sp = context.getSharedPreferences(preferanceName, Context.MODE_PRIVATE);
    }

    public static AppPref getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new AppPref(context);
        }
        return INSTANCE;
    }

    /**
     * Shared Preferance Key
     */
    public static class Keys {
        public static final String USER_AUTH_TOKEN = "user_auth_token";
    }

    public static String getString(Context context, String key) {
        return getInstance(context).getString(key);
    }

    public static Integer getInt(Context context, String key) {
        return getInstance(context).getInt(key);
    }

    public static Float getFloat(Context context, String key) {
        return getInstance(context).getFloat(key);
    }

    public static Boolean getBoolean(Context context, String key) {
        return getInstance(context).getBoolean(key);
    }

    public static Long getLong(Context context, String key) {
        return getInstance(context).getLong(key);
    }

    public static void putString(Context context, String key, String value) {
        getInstance(context).putString(key, value);
    }

    public static void putInt(Context context, String key, Integer value) {
        getInstance(context).putInt(key, value);
    }

    public static void putFloat(Context context, String key, Float value) {
        getInstance(context).putFloat(key, value);
    }

    public static void putBoolean(Context context, String key, Boolean value) {
        getInstance(context).putBoolean(key, value);
    }

    public static void putLong(Context context, String key, Long value) {
        getInstance(context).putLong(key, value);
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key) {
        return sp.getString(key, "");
    }

    public void putInt(String key, int value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public Integer getInt(String key) {
        return sp.getInt(key, 0);
    }

    public void putFloat(String key, Float value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    public Float getFloat(String key) {
        return sp.getFloat(key, 0f);
    }

    public void putLong(String key, Long value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public long getLong(String key) {
        return sp.getLong(key, 0);
    }

    public void putBoolean(String key, Boolean value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public Boolean getBoolean(String key) {
        return sp.getBoolean(key, false);
    }


}
