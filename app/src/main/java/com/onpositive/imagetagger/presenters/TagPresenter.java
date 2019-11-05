package com.onpositive.imagetagger.presenters;

import com.onpositive.imagetagger.models.Tag;
import com.onpositive.imagetagger.views.TagView;

public class TagPresenter extends BasePresenter<Tag, TagView> {

    public void onClick() {
        model.setChecked(
                !model.isChecked());
        setSelection();
    }

    public void setSelection(){
        view().setSelection(model.isChecked());
    }

    @Override
    protected void updateView() {
        view().setTagLabel(model.getTagLabel());
    }
}
