package com.onpositive.imagetagger.models;

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
        return this.getImage().getLastModified().compareTo(taggedImage.getImage().getLastModified());
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
