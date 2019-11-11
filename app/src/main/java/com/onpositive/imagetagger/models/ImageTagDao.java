package com.onpositive.imagetagger.models;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ImageTagDao {
    @Query("SELECT * FROM image INNER JOIN image_tag_join ON " +
            "Image.imagePath=image_tag_join.imagePath WHERE " +
            "image_tag_join.tagId = :tagId")
    List<Image> getImagesForTag(final int tagId);

    @Query("SELECT * FROM Tag INNER JOIN image_tag_join ON " +
            "Tag.tagId=image_tag_join.tagId WHERE " +
            "image_tag_join.imagePath = :imagePath")
    List<Tag> getTagsForImage(final String imagePath);

    @Insert
    void insert(ImageTag imageTag);

    @Update
    void update(ImageTag imageTag);

    @Delete
    void delete(ImageTag imageTag);

    @Query("DELETE FROM image_tag_join WHERE image_tag_join.imagePath = :imagePath")
    void deleteTagsForImage(final String imagePath);

    @Query("DELETE FROM image_tag_join WHERE image_tag_join.tagId = :tagId")
    void deleteTagById(final int tagId);
}
