package com.onpositive.imagetagger.views;

import android.net.Uri;

import com.onpositive.imagetagger.models.TaggedImage;

import java.io.File;
import java.util.List;

public interface ImagesGroupView {
    void showTaggedImages(List<TaggedImage> taggedImages);

    void makeTaggedImage();

    void startTaggedImageEditor(TaggedImage taggedImage);

    File createTempFile(String fileName);

    void exportFile(File file);

    void writeFileContent(Uri uri, File contentFile);
}
