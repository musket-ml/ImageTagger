package com.onpositive.imagetagger.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.onpositive.imagetagger.type_converters.ImageConverter;

import java.util.Date;

@Entity
@TypeConverters({ImageConverter.class})
public class Image {
    @NonNull
    @PrimaryKey
    private String imagePath;
    private Date lastModified;
    private String thumbnailPath;

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }
}
