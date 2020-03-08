package com.iderin.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.core.models.barang_model;
import com.core.models.kategori_model;
import com.core.models.toko_model;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iderin.adapters.AdapterToko;
import com.iderin.adapters.Adapter_barang;
import com.iderin.adapters.Adapter_kategori;
import com.pmo.iderin.BuildConfig;
import com.pmo.iderin.R;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;
import com.todkars.shimmer.ShimmerRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class fragment_explore extends Fragment implements ImageListener {
    @BindView(R.id.shimmer_recycler_kategori)
    ShimmerRecyclerView shimmerRecyclerKategori;
    @BindView(R.id.shimmer_recycler_terlaris)
    ShimmerRecyclerView shimmerRecyclerTerlaris;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.carouselView)
    CarouselView carouselView;
    @BindView(R.id.tv_btn_semua_kategori)
    TextView tvBtnSemuaKategori;
    @BindView(R.id.tv_btn_semua_barang)
    TextView tvBtnSemuatoko;
    // TODO: Rename parameter arguments, choose names that match
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private boolean hasToko = false;
    private List<kategori_model> listkategori = new ArrayList<>();
    private List<barang_model> listbarang = new ArrayList<>();
    private List<toko_model> listtoko = new ArrayList<>();
    private Adapter_kategori adapter;
    private Adapter_barang adapter_barang;
    private AdapterToko adaptertoko;
    private int[] sampleImages = {R.drawable.bg_auth_jpg, R.drawable.bg_auth_jpg};
    private Unbinder unbinder;

    public static fragment_explore newInstance(String param1, String param2) {
        fragment_explore fragment = new fragment_explore();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_explore, container, false);
        unbinder = ButterKnife.bind(this, v);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Beranda");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("Cari sayur");


        shimmerRecyclerKategori.showShimmer();
        //   shimmerRecyclerToko.showShimmer();
        shimmerRecyclerTerlaris.showShimmer();

        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(this);
        getKategori();
        getBarang();

        return v;
    }


    private void getBarang() {
        databaseReference
                .child(getResources().getString(R.string.CHILD_BARANG))
                .child(getResources().getString(R.string.CHILD_BARANG_ALL))
                .limitToFirst(6)
                .orderByChild("created_at")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            listbarang.clear();
                            barang_model barang_model = new barang_model();
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                barang_model = data.getValue(barang_model.class);
                                listbarang.add(barang_model);

                            }
                            adapter_barang = new Adapter_barang(getContext(), listbarang);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                            shimmerRecyclerTerlaris.setLayoutManager(layoutManager);
                            shimmerRecyclerTerlaris.setAdapter(adapter_barang);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    private void getKategori() {
        databaseReference.child(getResources().getString(R.string.CHILD_BARANG))
                .child(getResources().getString(R.string.CHILD_BARANG_KATEGORI))
                .limitToFirst(6)
                .orderByChild("created_at")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            listkategori.clear();
                            kategori_model model = new kategori_model();
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                model = data.getValue(kategori_model.class);
                                model.setId(data.getKey());
                                assert model != null;
                                listkategori.add(model);
                            }
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
                            adapter = new Adapter_kategori(getContext(), listkategori);
                            shimmerRecyclerKategori.setLayoutManager(layoutManager);
                            shimmerRecyclerKategori.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        unbinder.unbind();
    }


    @Override
    public void setImageForPosition(int position, ImageView imageView) {
        imageView.setImageResource(sampleImages[position]);
    }

    @OnClick({R.id.tv_btn_semua_kategori, R.id.tv_btn_semua_barang})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_btn_semua_kategori:
                Intent kekategori = new Intent();
                kekategori.setClassName(BuildConfig.APPLICATION_ID, "com.kategori.AllKategori");
                startActivity(kekategori);
                break;
            case R.id.tv_btn_semua_barang:
                Intent keall = new Intent();
                keall.setClassName(BuildConfig.APPLICATION_ID, "com.barang.AllBarang");
                startActivity(keall);
                break;
//            case R.id.tv_btn_semuatoko:
//                Intent ketoko = new Intent();
//                ketoko.setClassName(BuildConfig.APPLICATION_ID,"com.toko.AllToko");
//                startActivity(ketoko);
//                break;
        }
    }
}
