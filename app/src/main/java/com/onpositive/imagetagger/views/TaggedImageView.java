package com.onpositive.imagetagger.views;

import com.onpositive.imagetagger.models.TaggedImage;

public interface TaggedImageView {
    void showContextMenu();

    void showCard(TaggedImage taggedImage);
}
