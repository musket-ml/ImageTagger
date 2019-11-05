package com.onpositive.imagetagger.views;

import com.onpositive.imagetagger.models.Tag;

import java.util.List;

public interface ImageTaggerView {
    void showImage(String imagePath);

    void showTags(List<Tag> tags);

    void showNewTagDialog();

    List<Tag> getSelectedTags();

    void showTagsSelection(List<Tag> imageTagList);
}
