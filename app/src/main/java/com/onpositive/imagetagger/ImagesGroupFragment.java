package com.onpositive.imagetagger;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.onpositive.imagetagger.models.TaggedImage;
import com.onpositive.imagetagger.models.TaggedImagesGroup;
import com.onpositive.imagetagger.presenters.ImagesGroupPresenter;
import com.onpositive.imagetagger.tools.Logger;
import com.onpositive.imagetagger.tools.Utils;
import com.onpositive.imagetagger.views.ImagesGroupView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

public class ImagesGroupFragment extends Fragment implements ImagesGroupView {

    public static final String ARG_SECTION_NUMBER = "section_number";
    public static final String ARG_CURRENT_PHOTO_PATH = "current_photo_path";
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int PHOTO_PERMISSION_REQUEST_CODE = 2;
    private static final int WRITE_REQUEST_CODE = 3;

    private static Logger log = new Logger(ImagesGroupFragment.class);
    @BindView(R.id.makeImageFAB)
    public FloatingActionButton makeImageFAB;
    @BindView(R.id.tagged_images_rv)
    RecyclerView taggedImagesRV;
    private String currentPhotoPath;
    private ImagesGroupPresenter presenter;
    private TaggedImagesGroup taggedImagesGroup;
    private TaggedImagesAdapter taggedImagesRVAdapter;

    public static ImagesGroupFragment newInstance(TaggedImagesGroup taggedImagesGroup, int sectionNumber) {
        ImagesGroupFragment fragment = new ImagesGroupFragment();
        fragment.taggedImagesGroup = taggedImagesGroup;
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        log.log("Created fragment on position: " + sectionNumber);
        return fragment;
    }

    @OnClick({R.id.makeImageFAB})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.makeImageFAB:
                presenter.onMakeImageClicked();
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tagged_images, container, false);
        ButterKnife.bind(this, rootView);
        if (savedInstanceState == null) {
            presenter = new ImagesGroupPresenter();
        } else {
            presenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);
        }
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        taggedImagesRV.setLayoutManager(new LinearLayoutManager(this.getContext()));
        taggedImagesRV.setItemAnimator(itemAnimator);
        presenter.bindView(this);
        presenter.onCreateView();
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        log.log("onCreate executed");
    }

    @Override
    public void onResume() {
        presenter.bindView(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.unbindView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        PresenterManager.getInstance().savePresenter(presenter, outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.images_group_fragment_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        log.log("onCreateOptionsMenu executed");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_and_send:
                log.log("onOptionsItemSelected. save_and_send");
                presenter.onSendImagesToEmail();
                return true;
            default:
                log.log("onOptionsItemSelected super.onOptionsItemSelected");
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.bindView(this);
        log.log("Request code: " + requestCode + ", resultCode: " + resultCode);
        if (resultCode != RESULT_OK) {
            log.log("Image tagging canceled or failed");
            return;
        }
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                log.log("Starting ImageTaggerActivity.class");
                Intent intent = new Intent(this.getContext(), ImageTaggerActivity.class);
                intent.putExtra(ARG_CURRENT_PHOTO_PATH, currentPhotoPath);
                startActivity(intent);
                break;
            case WRITE_REQUEST_CODE:
                presenter.saveZip(data.getData());
                log.log("Zip saved to the selected place");
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PHOTO_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    log.log("Photo permission granted");
                    dispatchTakePictureIntent();
                }
                break;
            }
        }
    }

    private void dispatchTakePictureIntent() {
        File photoFile = null;
        try {
            photoFile = Utils.createImageFile(getActivity());
            currentPhotoPath = photoFile.getAbsolutePath();
        } catch (IOException ex) {
            log.log("Image file creation failed");
        }
        log.log("Image file created");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri photoURI = FileProvider.getUriForFile(getActivity(),
                "com.onpositive.imagetagger.fileprovider",
                photoFile);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            log.log("Starting takePictureIntent");
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

//    private void showRemoveModelDialog() {
//        new AlertDialog.Builder(this.getActivity())
//                .setTitle(getResources().getString(R.string.remove_alert_title))
//                .setMessage(
//                        getResources().getString(R.string.remove_alert_message))
//                .setPositiveButton(
//                        getResources().getString(R.string.yes),
//                        new DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog,
//                                                int which) {
//                                removeImagesGroup(tfLiteItem);
//                            }
//                        })
//                .setNegativeButton(
//                        getResources().getString(R.string.cancel),
//                        new DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog,
//                                                int which) {
//                            }
//                        }).show();
//        log.log("RemoveModelDialog is active");
//    }

//    private void removeImagesGroup(TFLiteItem tfLiteItem) {
//        List<ResultItem> segmentItemList = MLDemoApp.getInstance().getDatabase().resultItemDao()
//                .getAllByParentTF(tfLiteItem.getTfFilePath());
//        for (ResultItem resultItem : segmentItemList) {
//            File resultItemFile = new File(resultItem.getFilePath());
//            if (resultItemFile.exists())
//                resultItemFile.delete();
//            MLDemoApp.getInstance().getDatabase().resultItemDao().delete(resultItem);
//        }
//
//        File modelFile = new File(tfLiteItem.getTfFilePath());
//        if (modelFile.exists())
//            modelFile.delete();
//        MLDemoApp.getInstance().getDatabase().tfLiteItemDao().delete(tfLiteItem);
//
//        ((MainActivity) getActivity()).getSectionsPagerAdapter().refreshDataSet();
//        ((MainActivity) getActivity()).getSectionsPagerAdapter().notifyDataSetChanged();
//      TODO fix groupRemove
//        log.log("TFLite Model deleted with its files");
//    }

    @Override
    public void showTaggedImages(List<TaggedImage> taggedImages) {
        try {
            taggedImagesRVAdapter = new TaggedImagesAdapter();
            taggedImagesRVAdapter.addAll(taggedImages);
            taggedImagesRV.setAdapter(taggedImagesRVAdapter);
            log.log("Tags Recycler View loaded.");
        } catch (Exception e) {
            log.log("Failed tagsRV data loading: " + e.getMessage());
        }
    }

    @Override
    public void startTaggedImageEditor(TaggedImage taggedImage) {
        log.log("startTaggedImageEditor(startTaggedImageEditor) Starting ImageTaggerActivity.class");
        Intent intent = new Intent(this.getContext(), ImageTaggerActivity.class);
        intent.putExtra(ARG_CURRENT_PHOTO_PATH, taggedImage.getImage().getImagePath());
        startActivity(intent);
    }

    @Override
    public void makeTaggedImage() {
        log.log("Photo button pressed");
        requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PHOTO_PERMISSION_REQUEST_CODE);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        String currentImageId = item.getIntent().getStringExtra(TaggedImageVH.IMAGE_PATH);
        int adapterPosition = item.getIntent().getIntExtra(TaggedImageVH.ADAPTER_POSITION, -1);
        if (currentImageId == "" || adapterPosition < 0) {
            return super.onContextItemSelected(item);
        }
        if (this.getContext().getResources().getString(R.string.edit).equals(item.getTitle())) {
            TaggedImage taggedImage = taggedImagesRVAdapter.getItem(adapterPosition);
            presenter.onEditImageSelected(taggedImage);
        } else if (getContext().getResources().getString(R.string.delete).equals(item.getTitle())) {
            presenter.onDeleteImageSelected(currentImageId);
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public File createTempFile(String fileName) {
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);

        return new File(storageDir, fileName);
    }

    @Override
    public void exportFile(File file) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);

        intent.addCategory(Intent.CATEGORY_OPENABLE);

        intent.setType("application/zip");
        intent.putExtra(Intent.EXTRA_TITLE, file.getName());
        startActivityForResult(intent, WRITE_REQUEST_CODE);
    }

    @Override
    public void writeFileContent(Uri uri, File contentFile) {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        ParcelFileDescriptor pfd = getActivity().getContentResolver().openFileDescriptor(uri, "w");
                        FileOutputStream fileOutputStream = new FileOutputStream(pfd.getFileDescriptor());
                        FileChannel src = new FileInputStream(contentFile).getChannel();
                        FileChannel dest = fileOutputStream.getChannel();
                        dest.transferFrom(src, 0, src.size());
                        src.close();
                        dest.close();
                        fileOutputStream.close();
                        pfd.close();
                        contentFile.delete();
                        log.log("ZIP file has been extracted. Temporary ZIP deleted.");
                    } catch (Exception e) {
                        log.log(e.getMessage());
                    }
                }
            }).start();
        } catch (Exception e) {
            log.log(e.getMessage());
        }
    }

    @Override
    public void removeTaggedImages(List<TaggedImage> removedTaggedImages) {
        for (TaggedImage taggedImage : removedTaggedImages) {
            taggedImagesRVAdapter.removeItem(taggedImage);
        }
    }
}
