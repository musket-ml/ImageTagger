package com.onpositive.imagetagger.presenters;

import com.onpositive.imagetagger.models.TaggedImage;
import com.onpositive.imagetagger.views.TaggedImageView;

import java.text.Format;
import java.text.SimpleDateFormat;

public class TaggedImagePresenter extends BasePresenter<TaggedImage, TaggedImageView> {
    @Override
    protected void updateView() {
        view().showCard(model);
    }

    public void onClick() {
        view().showContextMenu();
    }

    public String onCreateContextMenuTitle() {
        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(model.getImage().getLastModified());
    }

    public String onCreateContextMenuImageId() {
        return model.getId();
    }
}
