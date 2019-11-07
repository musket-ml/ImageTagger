package com.onpositive.imagetagger;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
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
import java.io.IOException;
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
        super.onResume();
        presenter.bindView(this);
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
//        inflater.inflate(R.menu.model_fragment_menu, menu); TODO fix onCreateOptionsMenu
        super.onCreateOptionsMenu(menu, inflater);
        log.log("onCreateOptionsMenu executed");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.remove_current_tab:
//                log.log("onOptionsItemSelected. showRemoveModelDialog");
//                showRemoveModelDialog();
//                return true;
//            default:
//                log.log("onOptionsItemSelected super.onOptionsItemSelected");
//                return super.onOptionsItemSelected(item);
//        }
        return false; //TODO fix onOptionsItemSelected
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        log.log("onActivityResult. Request code: " + requestCode + ", resultCode: " + resultCode);
        if (resultCode != RESULT_OK) {
            log.log("onActivityResult image tagging canceled or failed");
            return;
        }
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                log.log("Starting ImageTaggerActivity.class");
                Intent intent = new Intent(this.getContext(), ImageTaggerActivity.class);
                intent.putExtra(ARG_CURRENT_PHOTO_PATH, currentPhotoPath);
                startActivity(intent);
                break;
        }
//        ImageClassifier classifier = null;
//        try {
//            classifier = new ImageClassifier(this.getActivity(), tfLiteItem);
//        } catch (IOException e) {
//            log.log("ImageClassifier creation failed: " + e.getMessage());
//        }
//        if (null != classifier) {
//            log.log("ImageClassifier created. ");
//            cat = new ClassificationAsyncTask(classifier, this);
//        } else
//            log.log("Classification failed. ImageClassifier object is null.");
//        if (resultCode != RESULT_OK) {
//            log.log("onActivityResult failed");
//            return;
//        }
//        switch (requestCode) {
//            case REQUEST_IMAGE_CAPTURE:
//                log.log(" got result from camera. currentPhotoPath: " + currentPhotoPath);
//                try {
//                    if (null != cat) {
//                        cat.execute(ContentType.IMAGE);
//                        log.log("Classification Async Task execution started");
//                    } else
//                        log.log("Classification failed. ClassificationAsyncTask object is null.");
//                } catch (Exception e) {
//                    log.log("Photo classification failed: " + e.getMessage());
//                }
//                break;
//        }
        //TODO fix onActivityResult
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
    public void makeTaggedImage() {
        log.log("Photo button pressed");
        requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PHOTO_PERMISSION_REQUEST_CODE);
    }
}