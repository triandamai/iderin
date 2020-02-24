package com.pmo.iderin.main;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pmo.iderin.R;
import com.pmo.iderin.adapters.Adapter_kategori;
import com.pmo.iderin.models.kategori_model;
import com.todkars.shimmer.ShimmerRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class fragment_explore extends Fragment {
    @BindView(R.id.shimmer_recycler_kategori)
    ShimmerRecyclerView shimmerRecyclerKategori;
    @BindView(R.id.shimmer_recycler_terlaris)
    ShimmerRecyclerView shimmerRecyclerTerlaris;
    @BindView(R.id.shimmer_recycler_terdekat)
    ShimmerRecyclerView shimmerRecyclerTerdekat;
    // TODO: Rename parameter arguments, choose names that match
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private boolean hasToko = false;
    private List<kategori_model> listkategori = new ArrayList<>();
    private Adapter_kategori adapter;

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
        ButterKnife.bind(this,v);
        shimmerRecyclerKategori.showShimmer();
        shimmerRecyclerTerdekat.showShimmer();
        shimmerRecyclerTerlaris.showShimmer();
        getKategori();
        return v;
    }

    private void getKategori() {
        databaseReference.child(getResources().getString(R.string.CHILD_BARANG))
                .child(getResources().getString(R.string.CHILD_BARANG_KATEGORI))
                .orderByChild("created_at")
                .limitToLast(10)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            listkategori.clear();
                            kategori_model model = new kategori_model();
                            for (DataSnapshot data : dataSnapshot.getChildren()){
                                model = data.getValue(kategori_model.class);
                                assert model != null;
                                listkategori.add(model);
                            }
                            adapter = new Adapter_kategori(getContext(),listkategori);
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

    }


}
