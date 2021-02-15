package com.koshy.patternstudio;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    public static final int IMAGE_REQ_CODE = 121;
    public static final int PATTERN_REQ_CODE = 122;
    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String IMAGE_EXTRA = "image";
    public static final String PATTERN_EXTRA = "pattern";
    String imagePath = null;
    String patternPath = null;
    Button imageButton;
    Button patternButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageButton = findViewById(R.id.image_select);
        imageButton.setOnClickListener(v -> getImage(IMAGE_REQ_CODE));
        patternButton = findViewById(R.id.pattern_select);
        patternButton.setOnClickListener(v -> getImage(PATTERN_REQ_CODE));
        Button proceedButton = findViewById(R.id.proceed);
        proceedButton.setOnClickListener(v -> {
            if (imagePath == null) {
                Toast.makeText(MainActivity.this, "Please select an image.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (patternPath == null) {
                Toast.makeText(MainActivity.this, "Please select a pattern.", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(MainActivity.this, ImageViewActivity.class);
            intent.putExtra(IMAGE_EXTRA, imagePath);
            intent.putExtra(PATTERN_EXTRA, patternPath);
            startActivity(intent);
        });

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
            ImagePicker.with(MainActivity.this).setMultipleMode(false).setRequestCode(code).start();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == Activity.RESULT_OK) {

            ArrayList<Image> images = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
            String imgPath = images.get(0).getPath();

            if (requestCode == IMAGE_REQ_CODE) {
                Log.d(TAG, "onActivityResult: Setting Image");
                imagePath = imgPath;
            } else {
                patternPath = imgPath;
            }
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}