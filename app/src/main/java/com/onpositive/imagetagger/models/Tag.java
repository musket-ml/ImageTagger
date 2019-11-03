package com.onpositive.imagetagger.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Tag implements Comparable<Tag>{

    @PrimaryKey(autoGenerate = true)
    private int tagId;
    private String tagLabel;
    @Ignore
    private boolean isChecked = false;

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public String getTagLabel() {
        return tagLabel;
    }

    public void setTagLabel(String tagLabel) {
        this.tagLabel = tagLabel;
    }

    @NonNull
    @Override
    public String toString() {
        return tagLabel;
    }

    @Override
    public int compareTo(Tag tag) {
        return this.getTagLabel().toLowerCase().compareTo(tag.getTagLabel().toLowerCase());
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
