package com.onpositive.imagetagger.presenters;

import android.os.AsyncTask;

import com.onpositive.imagetagger.ImageTaggerActivity;
import com.onpositive.imagetagger.data.ImageTaggerApp;
import com.onpositive.imagetagger.models.Tag;

import java.util.List;

public class ImageTaggerPresenter extends BasePresenter<List<Tag>, ImageTaggerActivity> {
    @Override
    protected void updateView() {

    }

    public void onMakeTagClicked() {
        view().showNewTagDialog();
    }

    public void onCreate() {
        new LoadTagsTask().execute();
    }

    public void onNewTagInputed(String tagLabel) {
        new CreateTagsTask().execute(tagLabel);
    }

    private class LoadTagsTask extends AsyncTask<Void, Void, List<Tag>> {

        @Override
        protected List<Tag> doInBackground(Void... voids) {
            return ImageTaggerApp.getInstance().getDatabase().tagDao().getTags();
        }

        @Override
        protected void onPostExecute(List<Tag> tags) {
            super.onPostExecute(tags);
            setModel(tags);
            view().showTags(tags);
        }
    }

    private class CreateTagsTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... tagLabels) {
            for (String tagLabel : tagLabels) {
                Tag tag = new Tag();
                tag.setTagLabel(tagLabel);
                ImageTaggerApp.getInstance().getDatabase().tagDao().insert(tag);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            new LoadTagsTask().execute();
        }
    }

}
