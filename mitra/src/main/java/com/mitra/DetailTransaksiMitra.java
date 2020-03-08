package com.mitra;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.core.models.detail_cart_model;
import com.core.models.transaksi_model;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iderin.adapters.Adapter_detail_transaksi;
import com.todkars.shimmer.ShimmerRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailTransaksiMitra extends AppCompatActivity {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.shimmer_recycler_transaksi)
    ShimmerRecyclerView shimmerRecyclerTransaksi;
    @BindView(R.id.tv_metode_pembayaran)
    TextView tvMetodePembayaran;
    @BindView(R.id.tv_total)
    TextView tvTotal;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.tv_btn_selesai)
    TextView tvBtnSelesai;


    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private String idtransaksi = "";
    private List<detail_cart_model> list = new ArrayList<>();
    private Context context = DetailTransaksiMitra.this;
    private Adapter_detail_transaksi adapter;
    private int status = 1; //1 == dipesan 2 = fiterima 3 = selesa

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.mitra.R.layout.activity_detail_transasksi_mitra);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            idtransaksi = intent.getStringExtra("idtransaksi");
            getDataTrans();
            getItemTransaksi();
        } else {
            finish();
        }
        Log.e("IDERIN :", idtransaksi);
    }

    private void getItemTransaksi() {
        databaseReference.child(getString(com.pmo.iderin.R.string.CHILD_TRANSAKSI))
                .child(getString(com.pmo.iderin.R.string.CHILD_TRANSAKSI_DETAIL))
                .child(idtransaksi)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            detail_cart_model modelcart = new detail_cart_model();
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                modelcart = data.getValue(detail_cart_model.class);
                                modelcart.setIddetail(data.getKey());
                                assert modelcart != null;
                                list.add(modelcart);
                            }
                            adapter = new Adapter_detail_transaksi(context, list);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
                            shimmerRecyclerTransaksi.setLayoutManager(layoutManager);
                            shimmerRecyclerTransaksi.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void getDataTrans() {
        databaseReference.child(getString(com.pmo.iderin.R.string.CHILD_TRANSAKSI))
                .child(getString(com.pmo.iderin.R.string.CHILD_TRANSAKSI))
                .child(idtransaksi)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            transaksi_model trmodel = dataSnapshot.getValue(transaksi_model.class);
                            tvTotal.setText("Total Pembayaran =  Rp " + trmodel.getTotal());
                            tvMetodePembayaran.setText("Metode pembayaran = " + trmodel.getMetode_pembayaran());

                            if (trmodel.getStatus().toString().equalsIgnoreCase("dipesan")) {
                                tvStatus.setText("Pesanan Baru");
                                status = 1;
                                tvBtnSelesai.setText("Proses Pesananan");
                            } else if (trmodel.getStatus().toString().equalsIgnoreCase("diterima")) {
                                tvStatus.setText("Sedang Diprooses");
                                tvBtnSelesai.setText("Selesaikan Pesanan");
                                status = 2;
                            } else if (trmodel.getStatus().toString().equalsIgnoreCase("selesai")) {
                                tvStatus.setText("Selesai");
                                tvBtnSelesai.setVisibility(View.GONE);
                                status = 3;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }


    @OnClick({R.id.tv_btn_selesai})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_btn_selesai:
                if (status == 1) {
                    Log.e("IDERIN", "status 1");
                    databaseReference.child(getString(R.string.CHILD_TRANSAKSI))
                            .child(getString(R.string.CHILD_TRANSAKSI))
                            .child(idtransaksi)
                            .child("status")
                            .setValue("diterima");
                    status = 2;
                } else if (status == 2) {
                    Log.e("IDERIN", "2");
                    startActivity(new Intent(context, Selesaikan_Transaksi.class));

                } else if (status == 3) {

                }
                break;

        }
    }
}
