package com.onpositive.imagetagger.models;

import java.util.List;

public class TaggedImage implements Comparable<TaggedImage> {
    Image image;
    List<Tag> imageTagList;

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
}
