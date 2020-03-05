package com.kategori;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.core.models.kategori_model;
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

public class AllKategori extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.tv_deskripsi)
    TextView tvDeskripsi;
    @BindView(R.id.shimmer_recycler_semuakategori)
    ShimmerRecyclerView shimmerRecyclerSemuakategori;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private Context context = AllKategori.this;
    private List<kategori_model> list = new ArrayList<>();
    private AdapterKategori adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_kategori);
        ButterKnife.bind(this);
        getAllKategori();
    }

    private void getAllKategori() {
        databaseReference.child(getString(R.string.CHILD_BARANG))
                .child(getString(R.string.CHILD_BARANG_KATEGORI))
                .orderByChild("created_at")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            list.clear();
                            kategori_model model = new kategori_model();
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                model = data.getValue(kategori_model.class);
                                model.setId(data.getKey());
                                assert model != null;
                                list.add(model);

                                adapter = new AdapterKategori(context, list);
                                shimmerRecyclerSemuakategori.setAdapter(adapter);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
