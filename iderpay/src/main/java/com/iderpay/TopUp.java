package com.iderpay;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TopUp extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.tv)
    TextView tv;
    @BindView(R.id.tv_penjelasan)
    TextView tvPenjelasan;
    @BindView(R.id.tv_penjelasan2)
    TextView tvPenjelasan2;
    @BindView(R.id.btn_kirim_bukti)
    TextView btnKirimBukti;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private Context context = TopUp.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv, R.id.btn_kirim_bukti})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv:
                break;
            case R.id.btn_kirim_bukti:
                startActivity(new Intent(context, KirimBukti.class));
                break;
        }
    }
}
