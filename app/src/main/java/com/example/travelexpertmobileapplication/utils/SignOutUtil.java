package com.example.travelexpertmobileapplication.utils;

import android.content.Context;
import android.content.Intent;

import com.example.travelexpertmobileapplication.LoginActivity;

public class SignOutUtil {
    public static void signOut(Context context) {
        // Clear the JWT token
        SharedPrefUtil.clearToken(context);

        // Navigate to LoginActivity and clear back stack
        Intent intent = new Intent(context, LoginActivity.class);
        // These flags clear the current back stack and start LoginActivity as a new task.
        // This prevents the user from returning to the ProfileFragment by pressing the back button.
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
}
