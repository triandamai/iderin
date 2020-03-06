package com.iderin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.iderin.Helpers.windowManager;
import com.pmo.iderin.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Splash extends AppCompatActivity {
    @BindView(R.id.btn_masuk)
    Button btnMasuk;
    @BindView(R.id.btn_daftar)
    Button btnDaftar;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private Context context = Splash.this;

    public Context getContext() {
        return context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        windowManager.getTransparentStatusBar(this);
        if (firebaseUser != null) {
                    startActivity(new Intent(context, MainActivity.class));
                    finish();
        } else {

        }
    }

    @OnClick({R.id.btn_masuk, R.id.btn_daftar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_masuk:
                Intent login = new Intent();
                login.setClassName(context, "com.auth.Login");
                startActivity(login);
                finish();
                break;
            case R.id.btn_daftar:
                Intent register = new Intent();
                register.setClassName(context, "com.auth.Register");
                startActivity(register);
                finish();
                break;
        }
    }
}
