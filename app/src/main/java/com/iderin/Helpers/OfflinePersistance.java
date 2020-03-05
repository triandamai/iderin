package com.iderin.Helpers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.database.FirebaseDatabase;
import com.pmo.iderin.R;

public class OfflinePersistance extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_persistance);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
