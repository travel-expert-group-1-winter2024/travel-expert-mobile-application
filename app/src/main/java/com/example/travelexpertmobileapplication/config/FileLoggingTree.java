package com.example.travelexpertmobileapplication.config;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import timber.log.Timber;

public class FileLoggingTree extends Timber.Tree {
    private final File logFile;

    public FileLoggingTree(Context context) {
        File dir = new File(context.getFilesDir(), "logs");
        if (!dir.exists()) dir.mkdirs();

        logFile = new File(dir, "log.txt");
    }

    @Override
    protected void log(int priority, @Nullable String tag, @NonNull String message, @Nullable Throwable t) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                .format(new Date());

        String level;
        switch (priority) {
            case Log.VERBOSE: level = "VERBOSE"; break;
            case Log.DEBUG:   level = "DEBUG"; break;
            case Log.INFO:    level = "INFO"; break;
            case Log.WARN:    level = "WARN"; break;
            case Log.ERROR:   level = "ERROR"; break;
            default:          level = "UNKNOWN"; break;
        }

        String logMessage = String.format("%s [%s] %s: %s",
                timestamp,
                level,
                tag != null ? tag : "App",
                message);

        if (t != null) {
            logMessage += " | EXCEPTION: " + Log.getStackTraceString(t);
        }

        try (FileWriter fw = new FileWriter(logFile, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(logMessage);
            bw.newLine();
        } catch (IOException e) {
            Log.e("FileLoggingTree", "Error writing log to file", e);
        }
    }
}
