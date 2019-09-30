package com.onpositive.imagetagger.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.onpositive.imagetagger.models.Tag;
import com.onpositive.imagetagger.models.TagDao;

@Database(entities = {Tag.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TagDao tagDao();
}
