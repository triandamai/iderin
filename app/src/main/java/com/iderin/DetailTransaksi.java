package com.iderin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.pmo.iderin.R;
import com.todkars.shimmer.ShimmerRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailTransaksi extends AppCompatActivity {

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

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private String idtransaksi = "";
    private List<detail_cart_model> list = new ArrayList<>();
    private Context context = DetailTransaksi.this;
    private Adapter_detail_transaksi adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_transaksi);
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
        databaseReference.child(getString(R.string.CHILD_TRANSAKSI))
                .child(getString(R.string.CHILD_TRANSAKSI_DETAIL))
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
        databaseReference.child(getString(R.string.CHILD_TRANSAKSI))
                .child(getString(R.string.CHILD_TRANSAKSI))
                .child(idtransaksi)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            transaksi_model trmodel = dataSnapshot.getValue(transaksi_model.class);
                            tvTotal.setText("Total Pembayaran Rp " + trmodel.getTotal());
                            tvMetodePembayaran.setText("Metode pembayaran" + trmodel.getMetode_pembayaran());
                            tvStatus.setText(trmodel.getStatus());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }


}
