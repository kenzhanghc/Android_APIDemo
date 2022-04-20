package com.example.customizeview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.myalldemo.R;

public class ShowViewActivity extends AppCompatActivity {

    private MyRelative my_view;
    private CircleLoadingView circleloading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_view_activity);
        circleloading = findViewById(R.id.circleloading);
        circleloading.setAnimation(80, 5000);
        my_view = findViewById(R.id.my_view);
        my_view.setAnimator(100,80,5000);
    }
}