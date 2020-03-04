package com.pmo.iderin.Admin;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pmo.iderin.R;
import com.pmo.iderin.adapters.Adapter_barang_admin;
import com.pmo.iderin.adapters.Adapter_kategori_admin;
import com.pmo.iderin.models.barang_model;
import com.pmo.iderin.models.kategori_model;
import com.todkars.shimmer.ShimmerRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ManageBarang extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.shimmer_recycler_barang)
    ShimmerRecyclerView shimmerRecyclerBarang;

    private Context context = ManageBarang.this;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    List<barang_model> list = new ArrayList<>();
    Adapter_barang_admin adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_barang);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("Semua Barang");
        getBarang();

    }

    private void getBarang() {
        shimmerRecyclerBarang.showShimmer();
        databaseReference.child(getResources().getString(R.string.CHILD_BARANG))
                .child(getResources().getString(R.string.CHILD_BARANG_ALL))
                .orderByChild(getString(R.string.ORDERBY_CREATED))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            list.clear();
                            barang_model model = new barang_model();
                            for (DataSnapshot data : dataSnapshot.getChildren()){

                                model = dataSnapshot.getValue(barang_model.class);
                                model.setId(dataSnapshot.getKey());
                                assert model != null;
                                list.add(model);
                                adapter = new Adapter_barang_admin(context,list);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context,RecyclerView.VERTICAL,false);
                                shimmerRecyclerBarang.setLayoutManager(layoutManager);
                                shimmerRecyclerBarang.setAdapter(adapter);

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
