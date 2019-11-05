package com.onpositive.imagetagger.models;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ImageDao {
    @Query("SELECT * FROM image ORDER BY lastModified")
    List<Image> getImages();

    @Query("SELECT * FROM image WHERE imagePath = :imagePath")
    Image getByPath(String imagePath);

    @Insert
    void insert(Image image);

    @Update
    void update(Image image);

    @Delete
    void delete(Image image);
}
