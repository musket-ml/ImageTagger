package com.onpositive.imagetagger.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Tag implements Comparable<Tag> {

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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + tagId;
        return result;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Tag otherTag = (Tag) obj;
        if (tagId != otherTag.tagId)
            return false;
        if (!tagLabel.equals(otherTag.tagLabel))
            return false;
        return true;
    }
}
