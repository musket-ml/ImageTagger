package com.onpositive.imagetagger.models;

import androidx.annotation.Nullable;

import java.util.List;

public class TaggedImage implements Comparable<TaggedImage> {
    Image image;
    List<Tag> imageTagList;
    private boolean isChecked = false;

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public List<Tag> getImageTagList() {
        return imageTagList;
    }

    public void setImageTagList(List<Tag> imageTagList) {
        this.imageTagList = imageTagList;
    }

    public String getId() {
        return image.getImagePath();
    }

    @Override
    public int compareTo(TaggedImage taggedImage) {
        return taggedImage.getImage().getLastModified().compareTo(this.getImage().getLastModified());
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }


    @Override
    public int hashCode() {
        final int prime = 32;
        int result = 1;
        result = prime * result + image.getImagePath().hashCode();
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
        TaggedImage otherImageTag = (TaggedImage) obj;
        if (!getImage().getImagePath().equals(otherImageTag.getImage().getImagePath()))
            return false;
        return true;
    }
}
