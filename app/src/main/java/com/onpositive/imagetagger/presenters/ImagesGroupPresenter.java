package com.onpositive.imagetagger.presenters;

import android.os.AsyncTask;

import com.onpositive.imagetagger.data.ImageTaggerApp;
import com.onpositive.imagetagger.models.Image;
import com.onpositive.imagetagger.models.ImageTag;
import com.onpositive.imagetagger.models.TaggedImage;
import com.onpositive.imagetagger.views.ImagesGroupView;

import java.util.ArrayList;
import java.util.List;

public class ImagesGroupPresenter extends BasePresenter<List<ImageTag>, ImagesGroupView> {
    @Override
    protected void updateView() {

    }

    public void onCreateView() {
        new LoadTaggedImages().execute();
    }

    public void onMakeImageClicked() {
        view().makeTaggedImage();
    }

    private class LoadTaggedImages extends AsyncTask<Void, Void, List<TaggedImage>> {

        @Override
        protected List<TaggedImage> doInBackground(Void... voids) {
            List<TaggedImage> taggedImages = new ArrayList<>();
            List<Image> images = ImageTaggerApp.getInstance().getDatabase().imageDao().getImages();
            for (Image image : images) {
                TaggedImage taggedImage = new TaggedImage();
                taggedImage.setImage(image);
                taggedImage.setImageTagList(
                        ImageTaggerApp.getInstance().getDatabase().imageTagDao().getTagsForImage(image.getImagePath()));
                taggedImages.add(taggedImage);
            }
            return taggedImages;
        }

        @Override
        protected void onPostExecute(List<TaggedImage> taggedImages) {
            super.onPostExecute(taggedImages);
            view().showTaggedImages(taggedImages);
        }
    }
}
