package com.onpositive.imagetagger.presenters;

import com.onpositive.imagetagger.models.TaggedImage;
import com.onpositive.imagetagger.views.TaggedImageView;

public class TaggedImagePresenter extends BasePresenter<TaggedImage, TaggedImageView> {
    @Override
    protected void updateView() {
        view().showCard(model);
    }
}
