package com.pmo.iderin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import static com.pmo.iderin.Helpers.windowManager.getTranparentStatusBar;

public class Activity_checkout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        getTranparentStatusBar(this);
    }
}
