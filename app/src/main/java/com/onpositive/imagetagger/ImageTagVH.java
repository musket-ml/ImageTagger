package com.onpositive.imagetagger;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.onpositive.imagetagger.presenters.TagPresenter;
import com.onpositive.imagetagger.tools.Logger;
import com.onpositive.imagetagger.views.TagView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

class ImageTagVH extends MvpViewHolder<TagPresenter> implements TagView, View.OnCreateContextMenuListener {
    private static Logger log = new Logger(ImageTagVH.class);
    @BindView(R.id.tag_item_tv)
    TextView itemTV;

    public ImageTagVH(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @OnClick()
    public void onClick() {
        presenter.onClick();
    }

    @Override
    public void setSelection(boolean checked) {
        if (checked) {
            this.itemView.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.item_selected_background));
            log.log("Set item checked on position: " + this.getAdapterPosition());
        } else {
            this.itemView.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.cardview_light_background));
            log.log("Set item unchecked on position: " + this.getAdapterPosition());
        }
    }

    @Override
    public void setTagLabel(String tagLabel) {
        itemTV.setText(tagLabel);
    }
}
