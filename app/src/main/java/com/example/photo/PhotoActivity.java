package com.example.photo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myalldemo.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 2022年3月27日
 * 编写了拍照返回照片的操作
 * 注：不在UI线程中进行耗时操作，避免出现ANR
 */
public class PhotoActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtnTakePhoto;
    private Button mBtnChoosePhoto;
    private ImageView mImageGetPhoto;
    private static final int TAKE_PHOTO = 1;
    private Uri mImageUri;
    private Bitmap mBitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_activity);

        initView();

    }

    private void getPhoto() {

        File outPutImage = new File(getExternalCacheDir(), "output_image.ipg");

        try {
            if (outPutImage.exists()) {
                outPutImage.delete();
            }
            outPutImage.createNewFile();

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= 24) {
            mImageUri = FileProvider.getUriForFile(this,
                    "com.example.photo.fileprovider", outPutImage);
        } else {
            mImageUri = Uri.fromFile(outPutImage);
        }
    }

    Handler handler;

    {
        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                mImageGetPhoto.setImageBitmap(mBitmap);
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {

                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            //将拍摄的照片加载出来
                            try {
                                mBitmap = BitmapFactory.decodeStream(getContentResolver().
                                        openInputStream(mImageUri));
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            //模拟延迟
                            SystemClock.sleep(4000);

                            Message message = handler.obtainMessage();
                            handler.sendMessage(message);
                        }
                    };
                    thread.start();
                    thread = null;
                    //模拟延迟

                }
                break;
            default:
                break;
        }
    }

    private void initView() {

        mBtnTakePhoto = findViewById(R.id.btn_take_photo);
        mBtnChoosePhoto = findViewById(R.id.btn_choose_photo);
        mImageGetPhoto = findViewById(R.id.image_get_photo);
        mBtnChoosePhoto.setOnClickListener(this);
        mBtnTakePhoto.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_take_photo:
                getPhoto();
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                startActivityForResult(intent, TAKE_PHOTO);
                break;
            case R.id.btn_choose_photo:

                break;
            default:
        }
    }
}