package com.pmo.iderin.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pmo.iderin.R;
import com.pmo.iderin.models.alamat_model;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.pmo.iderin.Helpers.windowManager.getTranparentStatusBar;

public class AddAlamat extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_judul)
    EditText etJudul;
    @BindView(R.id.et_lengkap)
    EditText etLengkap;
    @BindView(R.id.btn_simpan)
    Button btnSimpan;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private String id = "";
    private boolean isEditmode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alamat);
        ButterKnife.bind(this);
        getTranparentStatusBar(this);

        if(firebaseUser != null){
            Intent intent = getIntent();
            if (intent.getExtras() != null) {
                id = intent.getStringExtra("id");
                String namalamat = intent.getStringExtra("namaalamat");
                String alamatlengkap = intent.getStringExtra("alamatlengkap");
                etJudul.setText(namalamat);
                etLengkap.setText(alamatlengkap);
                isEditmode = true;
            }else {
                isEditmode = false;
                id = databaseReference.push().getKey();
            }
        }else{

        }
    }

    public void simpan(){
        if(cekVal()) {
            alamat_model alamatmodel = new alamat_model();
            alamatmodel.setNamaalamat(etJudul.getText().toString());
            alamatmodel.setAlamatlengkap(etLengkap.getText().toString());
            alamatmodel.setLat("195546dummy");
            alamatmodel.setLng("455767dummy");

            databaseReference
                    .child(getResources().getString(R.string.CHILD_AKUN))
                    .child(getResources().getString(R.string.CHILD_ALAMAT))
                    .child(firebaseUser.getUid())
                    .child(id)
                    .setValue(alamatmodel).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        onBackPressed();
                        finish();
                    }
                }
            });
        }else {
            //suruh isi dulu
        }
    }

    private void isiVal() {
    }
    private boolean cekVal(){
        return !TextUtils.isEmpty(etJudul.getText().toString()) || !TextUtils.isEmpty(etLengkap.getText().toString());
    }
    @OnClick({R.id.toolbar, R.id.btn_simpan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar:
                break;
            case R.id.btn_simpan:
                simpan();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(cekVal()){
            //muncul dialog
        }else {
            super.onBackPressed();
            finish();
        }
    }
}
