package com.example.travelexpertmobileapplication.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import timber.log.Timber;

public class SharedPrefUtil {
    private static final String PREF_NAME = "secret_shared_prefs";
    public static SharedPreferences getEncryptedPrefs(Context context) {
        try {
            MasterKey masterKey = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            return EncryptedSharedPreferences.create(
                    context,
                    PREF_NAME,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (Exception e) {
            Timber.e(e, "Failed to create shared preferences");
            return null;
        }
    }

    public static String getToken(Context context) {
        SharedPreferences prefs = getEncryptedPrefs(context);
        return prefs != null ? prefs.getString("jwt_token", null) : null;
    }

    public static void saveToken(Context context, String token) {
        SharedPreferences prefs = getEncryptedPrefs(context);
        if (prefs != null) {
            prefs.edit().putString("jwt_token", token).apply();
        }
    }

    public static void clearToken(Context context) {
        SharedPreferences prefs = getEncryptedPrefs(context);
        if (prefs != null) {
            prefs.edit().remove("jwt_token").apply();
        }
    }
}
