package com.koshy.patternstudio;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.dhaval2404.imagepicker.ImagePicker;


public class MainActivity extends AppCompatActivity {

    public static final int IMAGE_REQ_CODE = 121;
    public static final int PATTERN_REQ_CODE = 122;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button imageButton = findViewById(R.id.image_select);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage(IMAGE_REQ_CODE);
            }
        });
        Button patternButton = findViewById(R.id.pattern_select);
        patternButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage(PATTERN_REQ_CODE);
            }
        });
        Button proceedButton = findViewById(R.id.proceed);

    }

    public void getImage(int code) {
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) & (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) & (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED)){
            // Permission is not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 100);
        } else {
            ImagePicker.Companion.with(this).start(code);        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == IMAGE_REQ_CODE) {
                
            } else {

            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}