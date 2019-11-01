package com.onpositive.imagetagger;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.onpositive.imagetagger.models.Tag;
import com.onpositive.imagetagger.presenters.ImageTaggerPresenter;
import com.onpositive.imagetagger.tools.Logger;
import com.onpositive.imagetagger.views.ImageTaggerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ImageTaggerActivity extends AppCompatActivity implements ImageTaggerView {

    private static Logger log = new Logger(ImageTaggerActivity.class);
    @BindView(R.id.makeTagFAB)
    public FloatingActionButton makeTagFAB;
    @BindView(R.id.photo_iv)
    ImageView photoIV;
    @BindView(R.id.cancel_btn)
    Button cancelBtn;
    @BindView(R.id.save_close_btn)
    Button closeBtn;
    @BindView(R.id.tags_rv)
    RecyclerView tagsRV;
    private ImageTagsAdapter tagsRVAdapter;

    private ImageTaggerPresenter presenter;

    @OnClick({R.id.cancel_btn, R.id.save_close_btn, R.id.makeTagFAB})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel_btn:
                log.log("Cancel button clicked");
                onBackPressed();
                break;
            case R.id.save_close_btn:
                log.log("Close button clicked");
                presenter.onSaveButtonClicked();
                onBackPressed();
                break;
            case R.id.makeTagFAB:
                log.log("Make tag button clicked");
                presenter.onMakeTagClicked();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_tagger);
        ButterKnife.bind(this);
        if (savedInstanceState == null) {
            presenter = new ImageTaggerPresenter();
        } else {
            presenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);
        }
        Intent intent = getIntent();
        String currentPhotoPath = intent.getStringExtra(ImagesGroupFragment.ARG_CURRENT_PHOTO_PATH);
        photoIV.setImageBitmap(BitmapFactory.decodeFile(currentPhotoPath));
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        tagsRV.setLayoutManager(new LinearLayoutManager(this));
        tagsRV.setItemAnimator(itemAnimator);
        presenter.onCreate();
        log.log("onCreate executed.");
    }

    @Override
    protected void onResume() {
        super.onResume();

        presenter.bindView(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        presenter.unbindView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        PresenterManager.getInstance().savePresenter(presenter, outState);
    }

    @Override
    public void showTags(List<Tag> tags) {
        try {
            tagsRVAdapter = new ImageTagsAdapter();
            tagsRVAdapter.addAll(tags);
            tagsRV.setAdapter(tagsRVAdapter);
            log.log("Tags Recycler View loaded.");
        } catch (Exception e) {
            log.log("Failed tagsRV data loading: " + e.getMessage());
        }
    }

    @Override
    public List<Tag> getSelectedTags() {
        return tagsRVAdapter.getSelectedItems();
    }

    @Override
    public void showNewTagDialog() {
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.new_tag_dialog_title))
                .setView(input)
                .setPositiveButton(
                        getResources().getText(R.string.add),
                        (dialog, which) -> {
                            String tagLabel = input.getText().toString();
                            presenter.onNewTagInputed(tagLabel);
                            log.log("Add button clicked");
                        })
                .setNegativeButton(
                        getResources().getText(R.string.cancel),
                        (dialog, which) -> {
                            log.log("Cancel button clicked");
                        }
                );
        AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();
    }
}
