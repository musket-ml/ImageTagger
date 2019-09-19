package com.onpositive.imagetagger.models;

import java.util.Set;

public class TaggedImage {
    private String imagePath;
    private Set<String> imageTags;

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
