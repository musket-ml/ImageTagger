package com.onpositive.imagetagger.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "image_tag_join",
        primaryKeys = {"imagePath", "tagId"},
        foreignKeys = {
                @ForeignKey(entity = Image.class,
                        parentColumns = "imagePath",
                        childColumns = "imagePath"),
                @ForeignKey(entity = Tag.class,
                        parentColumns = "tagId",
                        childColumns = "tagId")
        })
public class ImageTag {
    private String imagePath;
    private int tagId;

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }
}
