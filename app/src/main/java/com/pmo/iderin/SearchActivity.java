package com.pmo.iderin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import static com.pmo.iderin.Helpers.windowManager.getTranparentStatusBar;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getTranparentStatusBar(this);
    }
}
