package com.onpositive.imagetagger.views;

import com.onpositive.imagetagger.models.TaggedImage;

import java.util.List;

public interface ImagesGroupView {
    void showTaggedImages(List<TaggedImage> taggedImages);

    void makeTaggedImage();
}
