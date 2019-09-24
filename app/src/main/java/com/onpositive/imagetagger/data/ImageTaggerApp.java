package com.onpositive.imagetagger.data;

import android.app.Application;

import androidx.room.Room;

public class ImageTaggerApp extends Application {
    public static final String DB_NAME = "imageTaggerDB";
    private static ImageTaggerApp imageTaggerAppInstance;
    private AppDatabase appDatabase;

    public static ImageTaggerApp getInstance() {
        return imageTaggerAppInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        imageTaggerAppInstance = this;
        appDatabase = Room.databaseBuilder(this, AppDatabase.class, DB_NAME).build();
    }

    public AppDatabase getDatabase() {
        return appDatabase;
    }
}
