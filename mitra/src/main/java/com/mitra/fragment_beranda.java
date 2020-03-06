package com.mitra;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.core.models.barang_model;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import butterknife.OnClick;
import butterknife.Unbinder;


public class fragment_beranda extends Fragment {

    @BindView(R.id.shimmer_recycler_barang)
    ShimmerRecyclerView shimmerRecyclerBarang;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.fab_add_barang)
    FloatingActionButton fabAddBarang;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private Unbinder unbinder;
    private AdapterBarang adapter;
    private List<barang_model> barang_modelList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_beranda, container, false);
        unbinder = ButterKnife.bind(this, v);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Beranda");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("Daftar barag di toko anda");

        getBarang();

        return v;
    }

    private void getBarang() {
        shimmerRecyclerBarang.showShimmer();
        databaseReference.child(getResources().getString(R.string.CHILD_BARANG))
                .child(getResources().getString(R.string.CHILD_BARANG_ALL))
                .orderByChild(getString(R.string.ORDERBY_CHILD_TOKO))
                .equalTo(firebaseUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            shimmerRecyclerBarang.hideShimmer();
                            barang_modelList.clear();
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                barang_model model = new barang_model();
                                model = data.getValue(barang_model.class);
                                model.setId(data.getKey());
                                assert model != null;
                                barang_modelList.add(model);
                                adapter = new AdapterBarang(getContext(), barang_modelList);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                                shimmerRecyclerBarang.setLayoutManager(layoutManager);
                                shimmerRecyclerBarang.setAdapter(adapter);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        shimmerRecyclerBarang.hideShimmer();
                    }
                });
    }


    @OnClick({R.id.fab_add_barang})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fab_add_barang:
                Intent addbarang = new Intent();
                addbarang.setClassName(getContext(), "com.barang.Addbarang");
                getActivity().startActivity(addbarang);
                break;

        }
    }
}
