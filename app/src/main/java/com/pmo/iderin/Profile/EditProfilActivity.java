package com.pmo.iderin.Profile;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pmo.iderin.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.pmo.iderin.Helpers.windowManager.getTranparentStatusBar;

public class EditProfilActivity extends AppCompatActivity {

    @BindView(R.id.iv_fotoprofil)
    ImageView ivFotoprofil;
    @BindView(R.id.et_nama)
    EditText etNama;
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_foto)
    EditText etFoto;
    @BindView(R.id.rg_jk)
    RadioGroup rgJk;
    @BindView(R.id.btn_simpan)
    Button btnSimpan;

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil);
        ButterKnife.bind(this);
        getTranparentStatusBar(this);
    }

    @OnClick({R.id.iv_fotoprofil, R.id.btn_simpan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_fotoprofil:
                break;
            case R.id.btn_simpan:
                break;
        }
    }
}
