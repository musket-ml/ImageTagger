package com.onpositive.imagetagger.models;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TagDao {
    @Query("SELECT * FROM Tag ORDER BY tagLabel")
    List<Tag> getTags();

    @Query("SELECT * FROM Tag WHERE tagId = :tagId")
    Tag getById(int tagId);

    @Insert
    void insert(Tag tag);

    @Update
    void update(Tag tag);

    @Delete
    void delete(Tag tag);

    @Query("DELETE FROM Tag WHERE Tag.tagId = :tagId")
    void deleteTagById(Integer tagId);
}
