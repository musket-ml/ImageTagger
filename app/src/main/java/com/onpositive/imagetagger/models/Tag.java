package com.onpositive.imagetagger.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tags")
public class Tag {

    @PrimaryKey(autoGenerate = true) private int tagId;
    private String tagLabel;
}
