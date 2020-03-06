package com.iderin;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.pmo.iderin.R;

import static com.iderin.Helpers.windowManager.getTransparentStatusBar;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getTransparentStatusBar(this);
    }
}
