package com.onpositive.imagetagger;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.onpositive.imagetagger.models.Tag;
import com.onpositive.imagetagger.models.TaggedImage;
import com.onpositive.imagetagger.presenters.TaggedImagePresenter;
import com.onpositive.imagetagger.tools.Logger;
import com.onpositive.imagetagger.tools.Utils;
import com.onpositive.imagetagger.views.TaggedImageView;

import java.text.Format;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnLongClick;

class TaggedImageVH extends MvpViewHolder<TaggedImagePresenter> implements TaggedImageView, View.OnCreateContextMenuListener {

    public static final String IMAGE_PATH = "image_path";
    public static final String ADAPTER_POSITION = "adapter_position";
    private static Logger log = new Logger(TaggedImageVH.class);
    @BindView(R.id.image_previewIV)
    ImageView imagePreviewIV;
    @BindView(R.id.last_modifiedTV)
    TextView lastModifiedTV;
    @BindView(R.id.tags_tv)
    TextView tagsTV;

    public TaggedImageVH(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnCreateContextMenuListener(this);
    }

    @Override
    public void showContextMenu() {
        this.itemView.showContextMenu();
    }

    @Override
    public void showCard(TaggedImage taggedImage) {
        StringBuilder tagsStringBuilder = new StringBuilder();
        for (int i = 0; i < taggedImage.getImageTagList().size(); i++) {
            Tag tag = taggedImage.getImageTagList().get(i);
            tagsStringBuilder.append(tag.getTagLabel());
            if (i < taggedImage.getImageTagList().size() - 1) {
                tagsStringBuilder.append("; ");
            }
        }
        try {
            Bitmap thumbnailImage = Utils.decodeSampledBitmapFromFile(taggedImage.getImage().getThumbnailPath(), 320, 320);
            imagePreviewIV.setImageBitmap(thumbnailImage);
        } catch (Error err) {
            log.log("Error. Failed card preview loading" + err.getMessage());
        }
        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = formatter.format(taggedImage.getImage().getLastModified());
        lastModifiedTV.setText(date);
        tagsTV.setText(tagsStringBuilder.toString());
        log.log("Loaded view for tagged image card: " + taggedImage.getImage().getImagePath());
    }

    @OnLongClick
    public boolean onLongClick() {
        presenter.onLongClick();
        log.log("OnLongClick, position: " + getAdapterPosition());
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        String label = presenter.onCreateContextMenuTitle();
        String imagePath = presenter.onCreateContextMenuImageId();
        int position = getAdapterPosition();
        Intent intent = new Intent();
        intent.putExtra(IMAGE_PATH, imagePath);
        intent.putExtra(ADAPTER_POSITION, position);
        contextMenu.setHeaderTitle(this.itemView.getContext().getResources().getString(R.string.image_colon) + " " + label);
        contextMenu.add(0, view.getId(), 0, this.itemView.getContext().getResources().getString(R.string.edit)).setIntent(intent);
        contextMenu.add(0, view.getId(), 0, this.itemView.getContext().getResources().getString(R.string.delete)).setIntent(intent);
    }
}
