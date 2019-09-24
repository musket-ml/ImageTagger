package com.onpositive.imagetagger.data;

import androidx.room.RoomDatabase;

import com.onpositive.imagetagger.models.TagDao;

public abstract class AppDatabase extends RoomDatabase {
    public abstract TagDao tagDao();
}
