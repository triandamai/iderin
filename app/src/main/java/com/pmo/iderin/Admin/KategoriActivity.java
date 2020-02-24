package com.pmo.iderin.Admin;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pmo.iderin.Helpers.Alert;
import com.pmo.iderin.R;
import com.pmo.iderin.adapters.Adapter_kategori_admin;
import com.pmo.iderin.models.kategori_model;
import com.todkars.shimmer.ShimmerRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class KategoriActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_kategori)
    Toolbar toolbarKategori;
    @BindView(R.id.shimmer_recycler_kategori)
    ShimmerRecyclerView shimmerRecyclerKategori;
    @BindView(R.id.btn_tambah)
    Button btnTambah;

    private Context context = KategoriActivity.this;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    List<kategori_model> list = new ArrayList<>();
    Adapter_kategori_admin adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategori);
        ButterKnife.bind(this);

        getData();
    }

    private void getData() {
        shimmerRecyclerKategori.showShimmer();
        databaseReference
                .child(getResources().getString(R.string.CHILD_BARANG))
                .child(getResources().getString(R.string.CHILD_BARANG_KATEGORI))
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    kategori_model model = new kategori_model();;
                    shimmerRecyclerKategori.hideShimmer();
                    list.clear();
                    for (DataSnapshot data: dataSnapshot.getChildren()){

                        model = data.getValue(kategori_model.class);
                        model.setId(data.getKey());
                        assert model!= null;
                        list.add(model);
                        //new Alert(context).toast(data.getKey().toString(),1);
                    }
                    adapter = new Adapter_kategori_admin(context,list);
                    shimmerRecyclerKategori.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @OnClick({R.id.toolbar_kategori, R.id.btn_tambah})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_kategori:
                break;
            case R.id.btn_tambah:
                startActivity(new Intent(context,FormKategori.class));
                break;
        }
    }
}
