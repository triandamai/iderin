package com.pmo.iderin.Penjual;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pmo.iderin.Helpers.Alert;
import com.pmo.iderin.R;
import com.pmo.iderin.adapters.Adapter_transaksi;
import com.pmo.iderin.models.transaksi_model;
import com.todkars.shimmer.ShimmerRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class fragment_transaksi extends Fragment {


    @BindView(R.id.shimmer_recycler_transaksi)
    ShimmerRecyclerView shimmerRecyclerTransaksi;
    private Unbinder unbinder;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private List<transaksi_model> transaksi_models = new ArrayList<>();
    private Adapter_transaksi adapter_transaksi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_transaksi, container, false);
        unbinder = ButterKnife.bind(this,v);
        shimmerRecyclerTransaksi.showShimmer();
        getTransaksi();
        listenTransaksi();
        return v;
    }

    private void getTransaksi() {
        databaseReference.child(getResources().getString(R.string.CHILD_TRANSAKSI))
                .orderByChild(getResources().getString(R.string.ORDERBY_CHILD_TOKO))
                .equalTo(firebaseUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            transaksi_models.clear();
                            transaksi_model model = new transaksi_model();
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                model = data.getValue(transaksi_model.class);
                                model.setId(data.getKey());
                                assert model != null;
                                transaksi_models.add(model);
                                adapter_transaksi = new Adapter_transaksi(getContext(), transaksi_models);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
                                shimmerRecyclerTransaksi.setLayoutManager(layoutManager);
                                shimmerRecyclerTransaksi.setAdapter(adapter_transaksi);

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void listenTransaksi() {
        databaseReference
                .child(getResources().getString(R.string.CHILD_TRANSAKSI))
                .addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    transaksi_model model = new transaksi_model();
                    model = dataSnapshot.getValue(transaksi_model.class);
                    assert model != null;
                    if(model.getIdtoko().toString().equalsIgnoreCase(firebaseUser.getUid())){
                        new Alert(getContext());
                    }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
