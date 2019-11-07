package com.onpositive.imagetagger.presenters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;

import com.onpositive.imagetagger.ImageTaggerActivity;
import com.onpositive.imagetagger.data.ImageTaggerApp;
import com.onpositive.imagetagger.models.Image;
import com.onpositive.imagetagger.models.ImageTag;
import com.onpositive.imagetagger.models.Tag;
import com.onpositive.imagetagger.models.TaggedImage;
import com.onpositive.imagetagger.tools.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

public class ImageTaggerPresenter extends BasePresenter<TaggedImage, ImageTaggerActivity> {
    private static Logger log = new Logger(ImageTaggerPresenter.class);

    @Override
    protected void updateView() {
        log.log("updateView() executed.");
    }

    public void onMakeTagClicked() {
        view().showNewTagDialog();
        log.log("onMakeTagClicked executed.");
    }

    public void onCreate(String currentPhotoPath) {
        view().showImage(currentPhotoPath);
        new LoadTagsTask().execute();
        new LoadModelTask().execute(currentPhotoPath);
        log.log("onCreate() executed.");
    }

    public void onNewTagInputed(String tagLabel) {
        new CreateTagsTask().execute(tagLabel);
    }

    public void onSaveButtonClicked() {
        List<Tag> selectedTags = view().getSelectedTags();
        model.setImageTagList(selectedTags);
        new SaveTaggedImage().execute();
        log.log("onSaveButtonClicked() executed.");
    }

    private String createThumbnail(String imagePath) {
        String thumbnailPath = null;
        try {
            Bitmap preview = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(imagePath), 320, 320);
            File thumbnailFile = view().createImageFile();
            OutputStream out = new FileOutputStream(thumbnailFile);
            preview.compress(Bitmap.CompressFormat.JPEG, 50, out);
            out.flush();
            out.close();
            thumbnailPath = thumbnailFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            log.log("Failed thumbnail creation.\n" + e.getMessage());
        }
        log.log("Thumbnail created.");
        return thumbnailPath;
    }

    private class LoadTagsTask extends AsyncTask<Void, Void, List<Tag>> {

        @Override
        protected List<Tag> doInBackground(Void... voids) {
            return ImageTaggerApp.getInstance().getDatabase().tagDao().getTags();
        }

        @Override
        protected void onPostExecute(List<Tag> tags) {
            super.onPostExecute(tags);
            view().showTags(tags);
            log.log("LoadTagsTask onPostExecute. Load all tags done. Tags count: " + tags.size());
        }
    }

    private class CreateTagsTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... tagLabels) {
            for (String tagLabel : tagLabels) {
                Tag tag = new Tag();
                tag.setTagLabel(tagLabel);
                ImageTaggerApp.getInstance().getDatabase().tagDao().insert(tag);
                log.log("Created a new tag: " + tag.getTagLabel() + ", with ID: " + tag.getTagId());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            new LoadTagsTask().execute();
            log.log("CreateTagsTask onPostExecute");
        }
    }

    private class SaveTaggedImage extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            if (null == ImageTaggerApp.getInstance().getDatabase().imageDao().getByPath(model.getImage().getImagePath())) {
                Date lastModified = new Date(new File(model.getImage().getImagePath()).lastModified());
                model.getImage().setLastModified(lastModified);
                String thumbnailPath = createThumbnail(model.getImage().getImagePath());
                model.getImage().setThumbnailPath(thumbnailPath);
                ImageTaggerApp.getInstance().getDatabase().imageDao().insert(model.getImage());
                log.log("Created new image: " + model.getImage().getImagePath());
            } else {
                ImageTaggerApp.getInstance().getDatabase().imageDao().update(model.getImage());
                log.log("Updated image: " + model.getImage().getImagePath());
            }
            ImageTaggerApp.getInstance().getDatabase().imageTagDao().deleteTagsForImage(model.getImage().getImagePath());
            for (Tag selectedTag : model.getImageTagList()) {
                try {
                    ImageTag imageTag = new ImageTag();
                    imageTag.setImagePath(model.getImage().getImagePath());
                    imageTag.setTagId(selectedTag.getTagId());
                    ImageTaggerApp.getInstance().getDatabase().imageTagDao().insert(imageTag);
                    log.log("Inserted new ImageTag. Tag: " + selectedTag.getTagLabel() + ", imagePath: " + model.getImage().getImagePath());
                } catch (Exception e) {
                    log.log("Failed ImageTag object insert: " + e.getMessage());
                }
            }
            return null;
        }
    }
}
