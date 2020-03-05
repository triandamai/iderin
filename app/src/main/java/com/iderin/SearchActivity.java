package com.iderin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.iderin.Helpers.windowManager;
import com.pmo.iderin.R;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        windowManager.getTransparentStatusBar(this);
    }
}
