package com.example.storagecp;

import android.content.Intent;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SharingActivity extends AppCompatActivity {
    private File savedPictureFile1, savedPictureFile2;
    private int REQUEST_PICTURE_CAPTURE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharing);

        findViewById(R.id.btn_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });

        findViewById(R.id.btn_share_pvt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareImagePrivate();
            }
        });

        findViewById(R.id.btn_share_int).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareImageInternal();
            }
        });
    }

    private void shareImageInternal() {
        if(savedPictureFile2==null){
            Toast.makeText(this, "Please Click a Picture First", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent shareIntent2 = new Intent();
        shareIntent2.setAction(Intent.ACTION_SEND);
        shareIntent2.setType("image/*");
        shareIntent2.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(this,
                getPackageName() + ".provider", savedPictureFile2));
        shareIntent2.putExtra(Intent.EXTRA_TEXT, "This image was saved inside 'Internal Memory'");
        shareIntent2.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent2, "Send to"));
    }

    private void shareImagePrivate() {
        if(savedPictureFile1==null){
            Toast.makeText(this, "Please Click a Picture First", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent shareIntent2 = new Intent();
        shareIntent2.setAction(Intent.ACTION_SEND);
        shareIntent2.setType("image/*");
        shareIntent2.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(this,
                getPackageName() + ".provider", savedPictureFile1));
        shareIntent2.putExtra(Intent.EXTRA_TEXT, "This image was inside 'App's Private Space'");
        shareIntent2.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent2, "Send to"));
    }

    private void openCamera() {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        savedPictureFile2 = getOutputMediaFile();
        pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                FileProvider.getUriForFile(this,
                        getPackageName() + ".provider", savedPictureFile2));
        startActivityForResult(pictureIntent, REQUEST_PICTURE_CAPTURE);
    }

    private File getOutputMediaFile() {
        File mediaStorageDir1 = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        if (!mediaStorageDir1.exists()) {
            if (!mediaStorageDir1.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir1.getPath() + File.separator +
                "Img_" + timeStamp + ".jpg");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICTURE_CAPTURE && resultCode == RESULT_OK) {
            Toast.makeText(this, "Image Saved!", Toast.LENGTH_SHORT).show();
            savedPictureFile1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/" + getPackageName() + "/images/" + savedPictureFile2.getName());
            try {
                FileUtils.copyFile(savedPictureFile2, savedPictureFile1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
