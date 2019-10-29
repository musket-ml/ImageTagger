package com.onpositive.imagetagger.presenters;

import com.onpositive.imagetagger.models.Tag;
import com.onpositive.imagetagger.views.TagView;

public class TagPresenter extends BasePresenter<Tag, TagView> {
    @Override
    protected void updateView() {
        view().setTagLabel(model.getTagLabel());
    }
}
