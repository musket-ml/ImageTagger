package com.onpositive.imagetagger.presenters;

import android.net.Uri;
import android.os.AsyncTask;

import com.onpositive.imagetagger.data.ImageTaggerApp;
import com.onpositive.imagetagger.models.Image;
import com.onpositive.imagetagger.models.ImageTag;
import com.onpositive.imagetagger.models.Tag;
import com.onpositive.imagetagger.models.TaggedImage;
import com.onpositive.imagetagger.tools.Logger;
import com.onpositive.imagetagger.views.ImagesGroupView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ImagesGroupPresenter extends BasePresenter<List<ImageTag>, ImagesGroupView> {

    private static Logger log = new Logger();
    private File currentZip;

    @Override
    protected void updateView() {

    }

    public void onCreateView() {
        new LoadTaggedImages().execute();
    }

    public void onMakeImageClicked() {
        view().makeTaggedImage();
    }

    public void onEditImageSelected(TaggedImage taggedImage) {
        view().startTaggedImageEditor(taggedImage);
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

    private class ZipExtractor extends AsyncTask<Void, Void, File> {
        public static final String TAGS = "tags";
        public static final String TAGGED_IMAGES = "tagged_images";

        @Override
        protected File doInBackground(Void... voids) {
            File zipArchive = null;
            JSONObject dataJson = new JSONObject();
            List<ImageTag> imagesTags = ImageTaggerApp.getInstance().getDatabase().imageTagDao().getAll();
            List<Tag> tags = ImageTaggerApp.getInstance().getDatabase().tagDao().getTags();
            Set<String> filesPaths = new HashSet<>();
            for (ImageTag imageTag : imagesTags) {
                filesPaths.add(imageTag.getImagePath());
            }
            try {
                dataJson.put(TAGGED_IMAGES, getTaggedImagesJson(imagesTags));
                dataJson.put(TAGS, getTagsJson(tags));
                File jsonFile = view().createTempFile("data.json");
                FileOutputStream fos = new FileOutputStream(jsonFile);
                fos.write(dataJson.toString(2).getBytes());
                fos.close();
                filesPaths.add(jsonFile.getAbsolutePath());

                String archiveName = TAGGED_IMAGES + "_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
                zipArchive = view().createTempFile(archiveName + ".zip");
                makeZip(filesPaths, zipArchive);
                jsonFile.delete();
                log.log("Archive has been created");
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            return zipArchive;
        }

        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);
            if (null != file) {
                view().exportFile(file);
                currentZip = file;
            }
        }

        private JSONObject getTaggedImagesJson(List<ImageTag> imagesTags) {
            JSONObject imagesTagsJson = new JSONObject();
            try {
                for (ImageTag imageTag : imagesTags) {
                    String fileName = imageTag.getImagePath().substring(
                            imageTag.getImagePath().lastIndexOf("/") + "/".length());
                    if (imagesTagsJson.has(fileName)) {
                        imagesTagsJson.getJSONArray(fileName).put(imageTag.getTagId());
                    } else {
                        imagesTagsJson.put(fileName, new JSONArray(new int[]{imageTag.getTagId()}));
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            return imagesTagsJson;
        }

        private JSONObject getTagsJson(List<Tag> tags) {
            JSONObject tagsJson = new JSONObject();
            try {
                for (Tag tag : tags) {
                    tagsJson.put(String.valueOf(tag.getTagId()), tag.getTagLabel());
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            return tagsJson;
        }

        private void makeZip(Set<String> paths, File zipFile) {
            try {
                BufferedInputStream origin = null;
                FileOutputStream dest = new FileOutputStream(zipFile);
                ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
                        dest));

                for (String path : paths) {
                    log.log("Adding: " + path);
                    File imageFile = new File(path);
                    FileInputStream fi = new FileInputStream(imageFile);
                    byte data[] = new byte[(int) imageFile.length()];
                    origin = new BufferedInputStream(fi, (int) imageFile.length());

                    ZipEntry entry = new ZipEntry(path.substring(path.lastIndexOf("/") + 1));
                    out.putNextEntry(entry);
                    int count;

                    while ((count = origin.read(data, 0, (int) imageFile.length())) != -1) {
                        out.write(data, 0, count);
                    }
                    origin.close();
                }

                out.close();
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }
}
