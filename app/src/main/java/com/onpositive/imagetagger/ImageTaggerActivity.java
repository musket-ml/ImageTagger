package com.onpositive.imagetagger;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.onpositive.imagetagger.tools.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ImageTaggerActivity extends AppCompatActivity {

    private static Logger log = new Logger(ImageTaggerActivity.class);
    @BindView(R.id.photo_iv)
    ImageView photoIV;
    @BindView(R.id.close_btn)
    Button closeBtn;
    @BindView(R.id.tags_rv)
    RecyclerView tagsRV;

    @OnClick({R.id.close_btn})
    public void onCick(View view) {
        switch (view.getId()) {
            case R.id.close_btn:
                log.log("Close button clicked");
                onBackPressed();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_tagger);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String currentPhotoPath = intent.getStringExtra(TaggedImagesFragment.ARG_CURRENT_PHOTO_PATH);
        photoIV.setImageBitmap(BitmapFactory.decodeFile(currentPhotoPath));
        System.out.println();
        log.log("onCreate executed.");
    }
}
