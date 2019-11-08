package com.onpositive.imagetagger;

import android.graphics.Bitmap;
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

class TaggedImageVH extends MvpViewHolder<TaggedImagePresenter> implements TaggedImageView {

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
            Bitmap thumbnailImage = Utils.decodeSampledBitmapFromFile(taggedImage.getImage().getImagePath(), 320, 320);
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
}
