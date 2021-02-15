package com.koshy.patternstudio;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.Util;

public class ImageViewActivity extends AppCompatActivity {

    public static final String TAG = ImageViewActivity.class.getSimpleName();
    private ScaleGestureDetector scaleGestureDetector;
    private float mScaleFactor = 1.0f;
    ImageView imageResultView;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        imageResultView = findViewById(R.id.image_result);
        progressBar = findViewById(R.id.progress_bar);
        String imagePath = getIntent().getStringExtra(MainActivity.IMAGE_EXTRA);
        String patternPath = getIntent().getStringExtra(MainActivity.PATTERN_EXTRA);

        uploadFile(imagePath, patternPath);
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

    }


    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        scaleGestureDetector.onTouchEvent(motionEvent);
        return true;
    }
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            if (imageResultView.getBackground() == null)
                return false;
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
            imageResultView.setScaleX(mScaleFactor);
            imageResultView.setScaleY(mScaleFactor);
            return true;
        }
    }

    public Boolean uploadFile(String imagePath, String patternPath) {
        try {
            Log.d(Utils.TAG, "uploadFile: " + imagePath);
            Log.d(Utils.TAG, "uploadFile: " + patternPath);

            byte[] imageBytes = Utils.compressBitmap(imagePath);
            byte[] patternBytes = Utils.compressBitmap(patternPath);

            RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("image", new File(imagePath).getName(),
                            RequestBody.create(imageBytes, Utils.MEDIA_TYPE_IMAGE))
                    .addFormDataPart("pattern", new File(imagePath).getName(),
                            RequestBody.create(patternBytes, Utils.MEDIA_TYPE_IMAGE))
                    .build();

            Request request = new Request.Builder()
                    .url(Utils.URL)
                    .post(requestBody)
                    .build();
            OkHttpClient client = new OkHttpClient.Builder().protocols(Util.immutableListOf(Protocol.HTTP_1_1)).build();

            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(final Call call, final IOException e) {
                    // Handle the error
                    Log.e(Utils.TAG, "onFailure: Failed" + call.request().tag() + " " + e.toString());
                }

                @Override
                public void onResponse(final Call call, final Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        // Handle the error
                        Log.e(Utils.TAG, "onResponse: " + "Failed network call");
                    }
                    String jsonData = null;
                    try {
                        jsonData = response.body().string();
//                        Log.d(TAG, "onResponse: " + jsonData);

                        JSONArray result = new JSONObject(jsonData).getJSONArray("result");
                        Bitmap map = Utils.convertBase64ToBitmap(result.get(0).toString());
                        // Upload successful
                        Log.d(Utils.TAG, "onResponse: " + map.getWidth());
                        runOnUiThread(() -> {
                            progressBar.setVisibility(View.INVISIBLE);

                            imageResultView.setImageBitmap(map);
                        });

                    } catch (IOException | JSONException e) {
                        Log.d(Utils.TAG, "onResponse: " + e.toString());
                    }

                }
            });

            return true;
        } catch (Exception ex) {
            // Handle the error
        }
        return false;
    }

}