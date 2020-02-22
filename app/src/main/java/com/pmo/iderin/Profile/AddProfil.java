package com.pmo.iderin.Profile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pmo.iderin.MainActivity;
import com.pmo.iderin.R;
import com.pmo.iderin.models.profil_model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.pmo.iderin.Helpers.windowManager.getTranparentStatusBar;

public class AddProfil extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 23;
    @BindView(R.id.iv_fotoprofil)
    ImageView ivFotoprofil;
    @BindView(R.id.et_nama)
    EditText etNama;
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.rg_jk)
    RadioGroup rgJk;
    @BindView(R.id.btn_simpan)
    Button btnSimpan;

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private StorageReference storageReference = firebaseStorage.getReference();
    private Uri filePath;
    private Context context = AddProfil.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil);
        ButterKnife.bind(this);
        getTranparentStatusBar(this);

    }

    private void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "select image from here.."),
                PICK_IMAGE_REQUEST);
    }

    private void upload(){
        if(filePath != null){
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            StorageReference myref = storageReference
                    .child(getResources().getString(R.string.CHILD_AKUN))
                    .child(getResources().getString(R.string.CHILD_AKUN_PROFIL))
                    .child("images"+firebaseAuth.getCurrentUser().getUid());
            ivFotoprofil.setDrawingCacheEnabled(true);
            ivFotoprofil.buildDrawingCache();

            Bitmap bitmap = ((BitmapDrawable) ivFotoprofil.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,80,baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = myref.putBytes(data);
            Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return  myref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                      if(task.isSuccessful()){
                          Uri donloadUri = task.getResult();

                          //aksi tambah ke db

                          final String value =
                                  ((RadioButton)findViewById(rgJk.getCheckedRadioButtonId()))
                                          .getText().toString();
                          profil_model profil = new profil_model();
                          profil.setFoto(donloadUri.toString());
                          profil.setAlamat("");
                          profil.setJenis_kelamin(value);
                          profil.setNama(etNama.getText().toString());
                          profil.setUpdated_at(new Date().getTime());
                          profil.setUsername(etUsername.getText().toString());
                          databaseReference
                                  .child(getResources().getString(R.string.CHILD_AKUN))
                                  .child(getResources().getString(R.string.CHILD_AKUN_PROFIL))
                                  .child(firebaseAuth.getCurrentUser().getUid())
                                  .setValue(profil)
                                  .addOnCompleteListener(new OnCompleteListener<Void>() {
                                      @Override
                                      public void onComplete(@NonNull Task<Void> task) {
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    progressDialog.dismiss();
                                                    startActivity(new Intent(context, MainActivity.class));
                                                    finish();
                                                }
                                            },1000);
                                      }
                                  });
                      }else {
                          //gagal
                          progressDialog.dismiss();
                      }
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //cek request
        if(requestCode == PICK_IMAGE_REQUEST &&
                resultCode == RESULT_OK &&
                data != null &&
                data.getData() != null){
                filePath = data.getData();
                try {
                    Bitmap bitmap = MediaStore
                            .Images
                            .Media
                            .getBitmap(getContentResolver(),filePath);
                    ivFotoprofil.setImageBitmap(bitmap);

                }catch (IOException io){
                    io.printStackTrace();
                }

        }
    }

    @OnClick({R.id.iv_fotoprofil, R.id.btn_simpan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_fotoprofil:
                selectImage();
                break;
            case R.id.btn_simpan:
                upload();
                break;
        }
    }
}
