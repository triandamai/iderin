package com.iderpay;

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
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.core.models.barang_model;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.iderin.Helpers.Alert;
import com.iderin.Helpers.Permissions;
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

public class KirimBukti extends AppCompatActivity {
    private static final int PICK_IMAGE_GALLERY_REQUEST = 1;
    private static final int PICK_IMAGE_CAMERA_REQUEST = 2;
    private final int ALL_PERMISSION = 999;


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.iv_bukti)
    ImageView ivBukti;
    @BindView(R.id.et_nominal)
    EditText etNominal;
    @BindView(R.id.btn_upload)
    Button btnUpload;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    private Context context = KirimBukti.this;
    private Uri filePath;
    private boolean isEditMode = false;
    private Camera camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kirim_bukti);
        ButterKnife.bind(this);
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

    public void upload() {
        if (filePath != null) {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            StorageReference myref = storageReference
                    .child(getResources().getString(R.string.CHILD_BARANG))
                    .child(getResources().getString(R.string.CHILD_BARANG_ALL))
                    .child("banner" + id);
            ivbarang.setDrawingCacheEnabled(true);
            ivbarang.buildDrawingCache();

            Bitmap bitmap = ((BitmapDrawable) ivbarang.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = myref.putBytes(data);
            Task<Uri> uriTask = uploadTask.continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return myref.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Uri donloadUri = task.getResult();

                    //aksi tambah ke db
                    barang_model toko = new barang_model();
                    toko.setDeskripsi(etDeskripsi.getText().toString());
                    toko.setFoto(donloadUri.toString());
                    toko.setIdkategori(idKategori);
                    toko.setIdtoko(firebaseUser.getUid());
                    toko.setNama(etNama.getText().toString());
                    toko.setCreated_at(new Date().getTime());
                    toko.setUpdated_at(new Date().getTime());
                    toko.setIdsatuan(idSatuan);

                    toko.setHarga(Double.parseDouble(etHarga.getText().toString()));

                    databaseReference
                            .child(getResources().getString(R.string.CHILD_BARANG))
                            .child(getResources().getString(R.string.CHILD_BARANG_ALL))
                            .child(id)
                            .setValue(toko)
                            .addOnCompleteListener(task1 -> new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();
                                    //startActivity(new Intent(context, MainActivity.class));
                                    finish();
                                }
                            }, 1000));
                } else {
                    //gagal
                    progressDialog.dismiss();
                }
            });
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
        uCrop.start(KirimBukti.this);
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
            new Alert(context).toast(e.getMessage(), 1);
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
                ivBukti.setImageBitmap(bitmap);

            } catch (IOException io) {
                io.printStackTrace();
            }

        } else if (requestCode == Camera.REQUEST_TAKE_PHOTO) {
            Uri imageUri = getImageUri(context, camera.getCameraBitmap());
            if (imageUri != null) {
                filePath = imageUri;
                startCrop(imageUri);

            } else {
                // new Bantuan(context).swal_error("Gagal mengambil gambar !");
            }
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            Uri hasilCrop = UCrop.getOutput(Objects.requireNonNull(data));
            if (hasilCrop != null) {
                ivBukti.setImageURI(hasilCrop);
                Picasso.get()
                        .load(hasilCrop)
                        .into(ivBukti);
            } else {
                //new Bantuan(context).swal_warning("gambar null hehe");
            }
        }
    }

    @OnClick({R.id.et_nominal, R.id.btn_upload, R.id.iv_bukti})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.et_nominal:
                break;
            case R.id.iv_bukti:
                String[] PERMISSIONS = {
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                };
                if (!Permissions.hasPermissions(this, PERMISSIONS)) {
                    ActivityCompat.requestPermissions(this, PERMISSIONS, ALL_PERMISSION);
                } else {
                    startCamera();
                }
                break;
            case R.id.btn_upload:
                if (!TextUtils.isEmpty(etNominal.getText()) || filePath != null) {
                    upload();
                } else {
                    new Alert(context).toast("Isis semua field", Toast.LENGTH_LONG);
                }
                break;
        }
    }
}
