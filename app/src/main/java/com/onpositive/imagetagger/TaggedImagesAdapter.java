package com.onpositive.imagetagger;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.onpositive.imagetagger.models.TaggedImage;
import com.onpositive.imagetagger.presenters.TaggedImagePresenter;
import com.onpositive.imagetagger.tools.Logger;

import java.util.ArrayList;
import java.util.List;

public class TaggedImagesAdapter extends MvpRecyclerListAdapter<TaggedImage, TaggedImagePresenter, TaggedImageVH> {
    private static Logger log = new Logger(TaggedImagesAdapter.class);

    @NonNull
    @Override
    public TaggedImageVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        log.log("onCreateViewHolder");
        return new TaggedImageVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.tagged_image_item_card, parent, false));
    }

    @NonNull
    @Override
    protected TaggedImagePresenter createPresenter(@NonNull TaggedImage model) {
        TaggedImagePresenter presenter = new TaggedImagePresenter();
        presenter.setModel(model);
        log.log("createPresenter for model:" + model.getId());
        return presenter;
    }

    @NonNull
    @Override
    protected Object getModelId(@NonNull TaggedImage model) {
        log.log("getModelId: " + model.getId());
        return model.getId();
    }

    @Override
    public List<TaggedImage> getSelectedItems() {
        List<TaggedImage> selectedItems = new ArrayList<>();
        for (TaggedImage taggedImage : models) {
            if (taggedImage.isChecked()) {
                selectedItems.add(taggedImage);
            }
        }
        log.log("SelectedItems count: " + selectedItems.size());
        return selectedItems;
    }
}
