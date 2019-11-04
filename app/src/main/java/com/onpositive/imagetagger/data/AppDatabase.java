package com.onpositive.imagetagger.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.onpositive.imagetagger.models.Image;
import com.onpositive.imagetagger.models.ImageDao;
import com.onpositive.imagetagger.models.ImageTag;
import com.onpositive.imagetagger.models.ImageTagDao;
import com.onpositive.imagetagger.models.Tag;
import com.onpositive.imagetagger.models.TagDao;

@Database(entities = {Tag.class, Image.class, ImageTag.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TagDao tagDao();

    public abstract ImageDao imageDao();

    public abstract ImageTagDao imageTagDao();
}
