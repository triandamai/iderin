package com.barang;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.core.models.barang_model;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.todkars.shimmer.ShimmerRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllBarang extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.tv_deskripsi)
    TextView tvDeskripsi;
    @BindView(R.id.shimmer_recycler_semuabarang)
    ShimmerRecyclerView shimmerRecyclerSemuabarang;

    private List<barang_model> modelList = new ArrayList<>();
    private AdapterBarang adapter;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private Context context = AllBarang.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_barang);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Semua Sayuran");
        shimmerRecyclerSemuabarang.showShimmer();

        Intent intent = getIntent();

        if (intent.getExtras() != null) {
            if (intent.getStringExtra(getResources().getString(R.string.INTENT_PUT_IDKATEGORI)) == null) {

                getAllBarang();
            } else {
                String idkategori = intent.getStringExtra(getResources().getString(R.string.INTENT_PUT_IDKATEGORI));
                getBarangByKategori(idkategori);
            }
        } else {
            getAllBarang();
        }

    }

    private void getBarangByKategori(String idkategori) {
        databaseReference
                .child(getString(R.string.CHILD_BARANG))
                .child(getString(R.string.CHILD_BARANG_ALL))
                .orderByChild("idkategori")
                .equalTo(idkategori)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            barang_model model = new barang_model();
                            modelList.clear();
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                model = data.getValue(barang_model.class);
                                assert model != null;
                                modelList.add(model);
                            }
                            adapter = new AdapterBarang(context, modelList);
                            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 2);
                            shimmerRecyclerSemuabarang.setLayoutManager(layoutManager);
                            shimmerRecyclerSemuabarang.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void getAllBarang() {
        databaseReference
                .child(getString(R.string.CHILD_BARANG))
                .child(getString(R.string.CHILD_BARANG_ALL))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            barang_model model = new barang_model();
                            modelList.clear();
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                model = data.getValue(barang_model.class);
                                assert model != null;
                                modelList.add(model);

                            }
                            adapter = new AdapterBarang(context, modelList);
                            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 2);
                            shimmerRecyclerSemuabarang.setLayoutManager(layoutManager);
                            shimmerRecyclerSemuabarang.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
