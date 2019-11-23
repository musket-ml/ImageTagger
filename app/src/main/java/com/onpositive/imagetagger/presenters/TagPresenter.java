package com.onpositive.imagetagger.presenters;

import com.onpositive.imagetagger.models.Tag;
import com.onpositive.imagetagger.views.TagView;

public class TagPresenter extends BasePresenter<Tag, TagView> {

    public void onClick() {
        model.setChecked(
                !model.isChecked());
        setSelection();
    }

    public void setSelection() {
        view().setSelection(model.isChecked());
    }

    @Override
    protected void updateView() {
        view().setTagLabel(model.getTagLabel());
        view().setSelection(model.isChecked());
    }

    public void onLongClick() {
        view().showContextMenu();
    }

    public String onCreateContextMenuTitle() {
        return model.getTagLabel();
    }

    public int onCreateContextMenuTagId() {
        return model.getTagId();
    }
}
