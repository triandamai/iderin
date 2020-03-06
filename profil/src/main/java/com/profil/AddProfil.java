package com.profil;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import androidx.core.app.ActivityCompat;

import com.core.models.profil_model;
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
import com.iderin.Helpers.Alert;
import com.iderin.Helpers.BottomSheetTakePict;
import com.iderin.Helpers.Permissions;
import com.iderin.MainActivity;
import com.mindorks.paracamera.Camera;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.iderin.Helpers.windowManager.getTransparentStatusBar;

public class AddProfil extends AppCompatActivity  implements BottomSheetTakePict.BottomSheetListener{

    private static final int PICK_IMAGE_GALLERY_REQUEST = 1;
    private static final int PICK_IMAGE_CAMERA_REQUEST = 2;
    private final int ALL_PERMISSION = 999;
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
    private String id = "";
    private boolean isEditMode = false;
    private Camera camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil);
        ButterKnife.bind(this);
        getTransparentStatusBar(this);

    }

    private void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "select image from here.."),
                PICK_IMAGE_GALLERY_REQUEST);
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case ALL_PERMISSION:
                if (grantResults.length > 0) {
                    boolean bolehSemua = false;
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            bolehSemua = true;
                        } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                            bolehSemua = false;
                            break;
                        }
                    }
                    if (bolehSemua) {
                        startCamera();
                    } else {
                        // new Bantuan(context).swal_error("Akses Di Tolak !");
                    }
                }
        }
    }
    private Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void startCrop(Uri uri) {
        String tujuan = String.valueOf(System.currentTimeMillis() % 1000);
        tujuan += ".jpg";
        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), tujuan)));
        uCrop.withAspectRatio(16, 9);
        uCrop.withMaxResultSize(1600, 1600);
        uCrop.withOptions(getCropOption());
        uCrop.start(AddProfil.this);
    }

    private UCrop.Options getCropOption() {
        UCrop.Options options = new UCrop.Options();
        options.setCompressionQuality(80);
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(false);
        options.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        options.setToolbarColor(getResources().getColor(R.color.colorPrimary));
        options.setToolbarTitle("Crop Gambar");
        return options;
    }
    @Override
    public void onOptionClick(String text) {
        if (text.equalsIgnoreCase("gallery")) {
            startActivityForResult(new Intent()
                    .setAction(Intent.ACTION_GET_CONTENT)
                    .setType("image/*"), PICK_IMAGE_GALLERY_REQUEST);
        } else {
            String[] PERMISSIONS = {
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
            if (!Permissions.hasPermissions(this, PERMISSIONS)) {
                ActivityCompat.requestPermissions(this, PERMISSIONS, ALL_PERMISSION);
            } else {
                startCamera();
            }
        }
    }
    private void startCamera() {
        camera = new Camera.Builder()
                .resetToCorrectOrientation(true)// it will rotate the camera bitmap to the correct orientation from meta data
                .setTakePhotoRequestCode(PICK_IMAGE_CAMERA_REQUEST)
                .setDirectory("pics")
                .setName("karyawan_" + System.currentTimeMillis())
                .setImageFormat(Camera.IMAGE_JPEG)
                .setCompression(75)
                .setImageHeight(1000)// it will try to achieve this height as close as possible maintaining the aspect ratio;
                .build(this);
        try {
            camera.takePicture();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(context).toast(e.getMessage(),1);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_GALLERY_REQUEST &&
                resultCode == RESULT_OK &&
                data != null &&
                data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(getContentResolver(), filePath);
                ivFotoprofil.setImageBitmap(bitmap);

            } catch (IOException io) {
                io.printStackTrace();
            }

        } else if (requestCode == Camera.REQUEST_TAKE_PHOTO) {
            Uri imageUri = getImageUri(context, camera.getCameraBitmap());
            if (imageUri != null) {
                startCrop(imageUri);
            } else {
                // new Bantuan(context).swal_error("Gagal mengambil gambar !");
            }
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            Uri hasilCrop = UCrop.getOutput(Objects.requireNonNull(data));
            if (hasilCrop != null) {
                ivFotoprofil.setImageURI(hasilCrop);
                Picasso.get()
                        .load(hasilCrop)
                        .into(ivFotoprofil);
            } else {
                //new Bantuan(context).swal_warning("gambar null hehe");
            }
        }
    }


    @OnClick({R.id.iv_fotoprofil, R.id.btn_simpan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_fotoprofil:
                BottomSheetTakePict bs = new BottomSheetTakePict();
                bs.show(getSupportFragmentManager(), "Ambil Foto Konter");
                break;
            case R.id.btn_simpan:
                upload();
                break;
        }
    }


}
