package com.pmo.iderin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.pmo.iderin.Helpers.windowManager.getTransparentStatusBar;

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
        getTransparentStatusBar(this);
        if (firebaseUser != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    databaseReference.child(getResources().getString(R.string.CHILD_AKUN))
                            .child("nama").setValue(firebaseAuth.getCurrentUser().getUid());
                    startActivity(new Intent(context, MainActivity.class));
                    finish();
                }
            }, 200);
        } else {

        }
    }

    @OnClick({R.id.btn_masuk, R.id.btn_daftar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_masuk:
                startActivity(new Intent(context, Auth_login.class));
                finish();
                break;
            case R.id.btn_daftar:

                break;
        }
    }
}
