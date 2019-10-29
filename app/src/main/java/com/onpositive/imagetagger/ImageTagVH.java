package com.onpositive.imagetagger;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.onpositive.imagetagger.presenters.TagPresenter;
import com.onpositive.imagetagger.views.TagView;

import butterknife.BindView;
import butterknife.ButterKnife;

class ImageTagVH extends MvpViewHolder<TagPresenter> implements TagView {
    @BindView(R.id.tag_item_tv)
    TextView itemTV;

    public ImageTagVH(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void setTagLabel(String tagLabel) {
        itemTV.setText(tagLabel);
    }
}
