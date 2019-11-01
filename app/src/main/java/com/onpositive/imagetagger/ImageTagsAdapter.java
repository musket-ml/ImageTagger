package com.onpositive.imagetagger;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.onpositive.imagetagger.models.Tag;
import com.onpositive.imagetagger.presenters.TagPresenter;
import com.onpositive.imagetagger.tools.Logger;

import java.util.ArrayList;
import java.util.List;

public class ImageTagsAdapter extends MvpRecyclerListAdapter<Tag, TagPresenter, ImageTagVH> {
    private static Logger log = new Logger(ImageTagsAdapter.class);

    @NonNull
    @Override
    public ImageTagVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        log.log("onCreateViewHolder");
        return new ImageTagVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_item, parent, false));
    }

    @NonNull
    @Override
    protected TagPresenter createPresenter(@NonNull Tag model) {
        TagPresenter tagPresenter = new TagPresenter();
        tagPresenter.setModel(model);
        log.log("createPresenter for model:" + model.getTagLabel());
        return tagPresenter;
    }

    @NonNull
    @Override
    protected Object getModelId(@NonNull Tag model) {
        log.log("getModelId: " + model.getTagId());
        return model.getTagId();
    }

    @Override
    public List<Tag> getSelectedItems() {
        List<Tag> selectedItems = new ArrayList<>();
        for (Tag tag : models) {
            if (tag.isChecked()) {
                selectedItems.add(tag);
            }
        }

        log.log("SelectedItems count: " + selectedItems.size());
        return selectedItems;
    }
}
