package com.onpositive.imagetagger;

import android.content.Intent;
import android.view.ContextMenu;
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
    public static final String TAG_ID = "tag_id";
    private static Logger log = new Logger(ImageTagVH.class);
    @BindView(R.id.tag_item_tv)
    TextView itemTV;

    public ImageTagVH(@NonNull View itemView) {
        super(itemView);
        itemView.setOnCreateContextMenuListener(this);
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

    @Override
    public void showContextMenu() {
        this.itemView.showContextMenu();
    }

    @OnLongClick
    public boolean onLongClick() {
        presenter.onLongClick();
        log.log("OnLongClick, position: " + getAdapterPosition());
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        String tagLabel = presenter.onCreateContextMenuTitle();
        int tagId = presenter.onCreateContextMenuTagId();
        Intent intent = new Intent();
        intent.putExtra(TAG_ID, tagId);
        contextMenu.setHeaderTitle(this.itemView.getContext().getResources().getString(R.string.tag_colon) + " " + tagLabel);
        contextMenu.add(0, view.getId(), 0, this.itemView.getContext().getResources().getString(R.string.edit)).setIntent(intent);
        contextMenu.add(0, view.getId(), 0, this.itemView.getContext().getResources().getString(R.string.delete)).setIntent(intent);

    }
}
