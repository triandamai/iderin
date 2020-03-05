package com.iderin.Profile;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.core.models.barang_model;
import com.core.models.kategori_model;
import com.core.models.satuan_model;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mindorks.paracamera.Camera;
import com.iderin.Helpers.Alert;
import com.iderin.Helpers.BottomSheetTakeKategori;
import com.iderin.Helpers.BottomSheetTakePict;
import com.iderin.Helpers.BottomSheetTakeSatuan;
import com.iderin.Helpers.Kategori;
import com.iderin.Helpers.Permissions;
import com.iderin.Helpers.Satuan;
import com.pmo.iderin.R;
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

public class Addbarang extends AppCompatActivity implements BottomSheetTakePict.BottomSheetListener {
    private static final int PICK_IMAGE_GALLERY_REQUEST = 1;
    private static final int PICK_IMAGE_CAMERA_REQUEST = 2;
    private final int ALL_PERMISSION = 999;

    @BindView(R.id.ivbarang)
    ImageView ivbarang;
    @BindView(R.id.et_kategori)
    EditText etKategori;
    @BindView(R.id.et_harga)
    EditText etHarga;
    @BindView(R.id.et_deskripsi)
    EditText etDeskripsi;
    @BindView(R.id.btn_simpan)
    Button btnSimpan;
    @BindView(R.id.et_nama)
    EditText etNama;
    @BindView(R.id.et_satuan)
    EditText etSatuan;

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private StorageReference storageReference = firebaseStorage.getReference();
    private Context context = Addbarang.this;
    private Uri filePath;
    private String id = "";
    private String idtoko = "";
    private String idKategori = "";
    private String idSatuan = "";
    private boolean isEditMode = false;
    private Camera camera;
    private kategori_model kategori_model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbarang);
        ButterKnife.bind(this);
        getTransparentStatusBar(this);


        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            String foto = intent.getStringExtra("gambar");
            String idkategori = intent.getStringExtra("idkategori");
            String kategori = intent.getStringExtra("kategori");
            String nama = intent.getStringExtra("nama");
            String des = intent.getStringExtra("deskripsi");
            String harga = intent.getStringExtra("harga");
            String idb = intent.getStringExtra("id");
            id = idb;
            etNama.setText(nama);
            etKategori.setText(kategori);
            etDeskripsi.setText(des);
            etHarga.setText(harga);
            Picasso.get().load(foto).into(ivbarang);
            idKategori = idkategori;
            isEditMode = true;
        } else {
            isEditMode = false;
            id = databaseReference.push().getKey();
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

    private boolean cekVal() {
        if (isEditMode) {
            return !TextUtils.isEmpty(etDeskripsi.getText().toString()) || !TextUtils.isEmpty(etKategori.getText().toString()) || !TextUtils.isEmpty(etNama.getText().toString()) || !TextUtils.isEmpty(etSatuan.getText().toString());
        } else {
            return !TextUtils.isEmpty(etDeskripsi.getText().toString()) || !TextUtils.isEmpty(etKategori.getText().toString()) || !TextUtils.isEmpty(etNama.getText().toString()) || !TextUtils.isEmpty(etSatuan.getText().toString()) || filePath != null;
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
        uCrop.start(Addbarang.this);
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
                ivbarang.setImageBitmap(bitmap);

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
                ivbarang.setImageURI(hasilCrop);
                Picasso.get()
                        .load(hasilCrop)
                        .into(ivbarang);
            } else {
                //new Bantuan(context).swal_warning("gambar null hehe");
            }
        }
    }


    @OnClick({R.id.ivbarang, R.id.et_kategori, R.id.btn_simpan,R.id.et_satuan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivbarang:
                BottomSheetTakePict bs = new BottomSheetTakePict();
                bs.show(getSupportFragmentManager(), "Ambil Foto Konter");
                break;
            case R.id.et_kategori:
                BottomSheetTakeKategori kat = new BottomSheetTakeKategori();
                kat.setOnkategoriSelected(new Kategori() {
                    @Override
                    public void selectedKategori(kategori_model kategori) {
                        kategori_model = kategori;
                        idKategori = kategori.getId();
                        etKategori.setText(kategori.getNama());
                        kat.dismiss();

                    }

                    @Override
                    public void addkategori(kategori_model kategori) {

                    }
                });
                kat.show(getSupportFragmentManager(), "Ambil kategori");

                break;
            case R.id.btn_simpan:
                if (cekVal()) {
                    upload();
                } else {
                    new Alert(context).toast("Isi Semua Field", 1);
                }
                break;
            case R.id.et_satuan:
                BottomSheetTakeSatuan bts = new BottomSheetTakeSatuan();
                bts.setOnSatuanSelected(new Satuan() {
                    @Override
                    public void selectedSatuan(satuan_model model) {
                        etSatuan.setText(model.getNama());
                        idSatuan = model.getIdsatuan();
                        bts.dismiss();

                    }

                    @Override
                    public void addSatuan(satuan_model model) {

                    }
                });
                bts.show(getSupportFragmentManager(),"Ambil satuan");

                break;
        }
    }
}
