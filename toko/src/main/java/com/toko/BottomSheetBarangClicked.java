package com.toko;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.core.models.barang_model;
import com.core.models.satuan_model;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class BottomSheetBarangClicked extends BottomSheetDialogFragment {

    Unbinder unbinder;
    @BindView(R.id.keterangan)
    TextView keterangan;
    @BindView(R.id.btn_min)
    TextView btnMin;
    @BindView(R.id.btn_jml)
    TextView btnJml;
    @BindView(R.id.btn_add)
    TextView btnAdd;
    @BindView(R.id.ly_qty)
    LinearLayout lyQty;
    @BindView(R.id.btn_250)
    TextView btn250;
    @BindView(R.id.btn_500)
    TextView btn500;
    @BindView(R.id.btn_750)
    TextView btn750;
    @BindView(R.id.btn_1000)
    TextView btn1000;
    @BindView(R.id.ly_satuanberat)
    LinearLayout lySatuanberat;
    @BindView(R.id.btn_tambahkan)
    TextView btnTambahkan;
    @BindView(R.id.btn_batal)
    TextView btnBatal;
    @BindView(R.id.ly_btn_bottomsheet)
    LinearLayout lyBtnBottomsheet;
    @BindView(R.id.tv_title_total)
    TextView tvTitleTotal;
    @BindView(R.id.tv_total)
    TextView tvTotal;

    private BottomSheetListener mBottomSheetListener;
    private int jmlQTY = 1;
    private double hargaAsli = 0;
    private double hargaTotal = 0;
    private int posisi;
    private barang_model barang_model;
    private satuan_model satuan_model;
    private boolean isSatuanBerat = false;
    private String satuantext;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_pickitem, container, false);
        unbinder = ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        if (bundle != null) {
            String nama = bundle.getString(getString(R.string.BUNDLE_NAMA_BARANG));
            String idbarang = bundle.getString(getString(R.string.BUNDLE_ID_BARANG));
            int posisis = bundle.getInt(getString(R.string.BUNDLE_POSISI));
            double harga = bundle.getDouble(getString(R.string.BUNDLE_HARGA));
            String idsatuan = bundle.getString(getString(R.string.BUNDLE_ID_SATUAN));
            keterangan.setText(nama);

            hargaAsli = harga;
            tvTotal.setText(String.valueOf(harga));
            databaseReference.child(getString(R.string.CHILD_BARANG))
                    .child(getString(R.string.CHILD_BARANG_ALL))
                    .child(idbarang)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                barang_model = dataSnapshot.getValue(com.core.models.barang_model.class);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
            databaseReference.child(getString(R.string.CHILD_BARANG))
                    .child(getString(R.string.CHILD_BARANG_SATUAN))
                    .child(idsatuan)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                satuan_model model = dataSnapshot.getValue(com.core.models.satuan_model.class);
                                if (model.getNama().toString().equalsIgnoreCase("ikat") || model.getNama().toString().equalsIgnoreCase("bungkus")) {
                                    lySatuanberat.setVisibility(View.GONE);
                                    lyQty.setVisibility(View.VISIBLE);
                                    isSatuanBerat = false;
                                } else {
                                    lySatuanberat.setVisibility(View.VISIBLE);
                                    lyQty.setVisibility(View.GONE);
                                    isSatuanBerat = true;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

        }


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        hargaTotal = 0;
        hargaAsli = 0;
        barang_model = null;
        satuan_model = null;

    }


    private void pilihBerat(int i1, int i) {
        switch (i) {
            case R.id.btn_250:
                btn250.setBackground(getContext().getResources().getDrawable(R.drawable.button_primary));
                btn250.setTextColor(getContext().getResources().getColor(R.color.background));
                btn500.setBackground(getContext().getResources().getDrawable(R.drawable.button_secondary));
                btn500.setTextColor(getContext().getResources().getColor(R.color.hijau));
                btn750.setBackground(getContext().getResources().getDrawable(R.drawable.button_secondary));
                btn750.setTextColor(getContext().getResources().getColor(R.color.hijau));
                btn1000.setBackground(getContext().getResources().getDrawable(R.drawable.button_secondary));
                btn1000.setTextColor(getContext().getResources().getColor(R.color.hijau));
                hargaTotal = hargaAsli / 4;
                tvTotal.setText("Rp " + hargaTotal);
                break;
            case R.id.btn_500:
                btn500.setBackground(getContext().getResources().getDrawable(R.drawable.button_primary));
                btn500.setTextColor(getContext().getResources().getColor(R.color.background));
                btn250.setBackground(getContext().getResources().getDrawable(R.drawable.button_secondary));
                btn250.setTextColor(getContext().getResources().getColor(R.color.hijau));
                btn750.setBackground(getContext().getResources().getDrawable(R.drawable.button_secondary));
                btn750.setTextColor(getContext().getResources().getColor(R.color.hijau));
                btn1000.setBackground(getContext().getResources().getDrawable(R.drawable.button_secondary));
                btn1000.setTextColor(getContext().getResources().getColor(R.color.hijau));
                hargaTotal = hargaAsli / 2;
                tvTotal.setText("Rp " + Math.round(hargaTotal));
                break;
            case R.id.btn_750:
                btn750.setBackground(getContext().getResources().getDrawable(R.drawable.button_primary));
                btn750.setTextColor(getContext().getResources().getColor(R.color.background));
                btn250.setBackground(getContext().getResources().getDrawable(R.drawable.button_secondary));
                btn250.setTextColor(getContext().getResources().getColor(R.color.hijau));
                btn500.setBackground(getContext().getResources().getDrawable(R.drawable.button_secondary));
                btn500.setTextColor(getContext().getResources().getColor(R.color.hijau));
                btn1000.setBackground(getContext().getResources().getDrawable(R.drawable.button_secondary));
                btn1000.setTextColor(getContext().getResources().getColor(R.color.hijau));

                hargaTotal = hargaAsli * 2 / 3;
                tvTotal.setText("Rp " + Math.round(hargaTotal));
                break;
            case R.id.btn_1000:
                btn1000.setBackground(getContext().getResources().getDrawable(R.drawable.button_primary));
                btn1000.setTextColor(getContext().getResources().getColor(R.color.background));
                btn250.setBackground(getContext().getResources().getDrawable(R.drawable.button_secondary));
                btn250.setTextColor(getContext().getResources().getColor(R.color.hijau));
                btn500.setBackground(getContext().getResources().getDrawable(R.drawable.button_secondary));
                btn500.setTextColor(getContext().getResources().getColor(R.color.hijau));
                btn750.setBackground(getContext().getResources().getDrawable(R.drawable.button_secondary));
                btn750.setTextColor(getContext().getResources().getColor(R.color.hijau));
                hargaTotal = hargaAsli;
                tvTotal.setText("Rp " + Math.round(hargaTotal));
                break;
        }
    }

    private void ubahQTY(tipeAdd kurang) {
        switch (kurang) {
            case KURANG:
                if (jmlQTY >= 30) {
                    btnJml.setText(String.valueOf(jmlQTY));
                    hargaTotal = hargaAsli * jmlQTY;
                    jmlQTY--;
                    tvTotal.setText("Rp " + Math.round(hargaTotal));
                } else if (jmlQTY <= 1) {
                    btnJml.setText(String.valueOf(jmlQTY));
                    hargaTotal = hargaAsli * jmlQTY;
                    jmlQTY = 1;
                    tvTotal.setText("Rp " + Math.round(hargaTotal));
                } else {
                    btnJml.setText(String.valueOf(jmlQTY));
                    hargaTotal = hargaAsli * jmlQTY;
                    jmlQTY = 1;
                    tvTotal.setText("Rp " + Math.round(hargaTotal));
                }
                break;
            case TAMBAH:
                if (jmlQTY >= 1) {
                    btnJml.setText(String.valueOf(jmlQTY));
                    hargaTotal = hargaAsli * jmlQTY;
                    jmlQTY++;
                    tvTotal.setText("Rp " + Math.round(hargaTotal));
                } else if (jmlQTY == 30) {
                    btnJml.setText(String.valueOf(jmlQTY));
                    hargaTotal = hargaAsli * jmlQTY;
                    jmlQTY = 30;
                    tvTotal.setText("Rp " + Math.round(hargaTotal));
                } else {
                    btnJml.setText(String.valueOf(jmlQTY));
                    hargaTotal = hargaAsli * jmlQTY;
                    jmlQTY = 1;
                    tvTotal.setText("Rp " + Math.round(hargaTotal));
                }
                break;
        }
    }

    enum satuan {
        BERAT,
        IKAT,
        BUNGKUS
    }

    enum tipeAdd {
        TAMBAH,
        KURANG
    }

    public void setOnBottomSheetListener(BottomSheetListener mBottomSheetListener) {
        this.mBottomSheetListener = mBottomSheetListener;
    }

    public interface BottomSheetListener {
        void onOptionClick(int jml, barang_model model, int pos, double total);
    }

    @OnClick({R.id.btn_min, R.id.btn_jml, R.id.btn_add, R.id.btn_250, R.id.btn_500, R.id.btn_750, R.id.btn_1000, R.id.btn_tambahkan, R.id.btn_batal})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_min:
                ubahQTY(tipeAdd.KURANG);
                break;
            case R.id.btn_jml:
                break;
            case R.id.btn_add:
                ubahQTY(tipeAdd.TAMBAH);
                break;
            case R.id.btn_250:
                pilihBerat(250, R.id.btn_250);
                break;
            case R.id.btn_500:
                pilihBerat(250, R.id.btn_500);
                break;
            case R.id.btn_750:
                pilihBerat(250, R.id.btn_750);
                break;
            case R.id.btn_1000:
                pilihBerat(250, R.id.btn_1000);
                break;
            case R.id.btn_tambahkan:
                mBottomSheetListener.onOptionClick(jmlQTY, barang_model, posisi, hargaTotal);
                break;
            case R.id.btn_batal:
                break;
        }
    }
}
