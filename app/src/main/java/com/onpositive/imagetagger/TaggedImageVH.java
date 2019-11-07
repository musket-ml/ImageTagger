package com.onpositive.imagetagger;

import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.onpositive.imagetagger.models.Tag;
import com.onpositive.imagetagger.models.TaggedImage;
import com.onpositive.imagetagger.presenters.TaggedImagePresenter;
import com.onpositive.imagetagger.views.TaggedImageView;

import java.text.Format;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

class TaggedImageVH extends MvpViewHolder<TaggedImagePresenter> implements TaggedImageView {

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
        for (Tag tag : taggedImage.getImageTagList()) {
            tagsStringBuilder.append(tag.getTagLabel() + " ");
        }
        imagePreviewIV.setImageBitmap(BitmapFactory.decodeFile(taggedImage.getImage().getImagePath()));
        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = formatter.format(taggedImage.getImage().getLastModified());
        lastModifiedTV.setText(date);
        tagsTV.setText(tagsStringBuilder.toString());
    }
}
