package com.koshy.patternstudio;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.Util;

public class Utils {
    public static final MediaType MEDIA_TYPE_IMAGE = MediaType.parse("image/*");
    public static final String URL = "https://average-turkey-8.loca.lt/upload";
    public static final String TAG = Utils.class.getSimpleName();


    public static Bitmap convertBase64ToBitmap(String b64Image) {
        Log.d(TAG, "convertBase64ToBitmap: " + b64Image);
        byte[] decodedString = Base64.decode(b64Image, Base64.DEFAULT);
        Log.d(TAG, "convertBase64ToBitmap: " + decodedString.length);

        Bitmap bmp = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        Log.d(TAG, "convertBase64ToBitmap: " + bmp.getHeight());
        return bmp;
    }

    public static byte[] compressBitmap(String path) {
        Bitmap original = BitmapFactory.decodeFile(path);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        original.compress(Bitmap.CompressFormat.JPEG, 30, out);
        return out.toByteArray();
    }
}
