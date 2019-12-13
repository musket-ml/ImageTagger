package com.onpositive.imagetagger.presenters;

import com.onpositive.imagetagger.models.TaggedImage;
import com.onpositive.imagetagger.views.Main;

import java.util.List;

public class MainPresenter extends BasePresenter<List<TaggedImage>, Main> {
    @Override
    protected void updateView() {

    }

    public void onCreate() {
        view().showImagesFragment();
    }
}
