package com.iderin.main;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.core.models.transaksi_model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pmo.iderin.R;
import com.todkars.shimmer.ShimmerRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class fragment_shop extends Fragment {

    @BindView(R.id.toolbar_alamat)
    Toolbar toolbarAlamat;
    @BindView(R.id.shimmer_recycler_shop)
    ShimmerRecyclerView shimmerRecyclerShop;

    private List<transaksi_model> list = new ArrayList<>();


    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    public fragment_shop() {
        // Required empty public constructor
    }


    public static fragment_shop newInstance(String param1, String param2) {
        fragment_shop fragment = new fragment_shop();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    int c = 60;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_shop, container, false);
        ButterKnife.bind(this, v);
        shimmerRecyclerShop.showShimmer();
        getTransaksi();

        return v;
    }

    private void getTransaksi() {
        databaseReference.child(getString(R.string.CHILD_TRANSAKSI))
                .child(getString(R.string.CHILD_TRANSAKSI))
                .orderByChild("idpembeli")
                .equalTo(firebaseUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            list.clear();
                            transaksi_model transaksiModel = new transaksi_model();
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                transaksiModel = data.getValue(transaksi_model.class);
                                transaksiModel.setIdtransaksi(data.getKey());
                                assert transaksiModel != null;
                                list.add(transaksiModel);
                            }
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
