package com.onpositive.imagetagger.presenters;

import com.onpositive.imagetagger.ImagesGroupFragment;
import com.onpositive.imagetagger.models.ImageTag;

import java.util.List;

public class ImagesGroupPresenter extends BasePresenter<List<ImageTag>, ImagesGroupFragment> {
    @Override
    protected void updateView() {

    }

    public void onMakeImageClicked() {
        view().makeTaggedImage();
    }
}
