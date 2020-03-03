package com.pmo.iderin.Penjual;

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
import com.todkars.shimmer.ShimmerRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class fragment_beranda extends Fragment {

    @BindView(R.id.shimmer_recycler_barang)
    ShimmerRecyclerView shimmerRecyclerBarang;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private Unbinder unbinder;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_beranda, container, false);
        unbinder = ButterKnife.bind(this,v);
        getBarang();
        return v;
    }

    private void getBarang() {
        shimmerRecyclerBarang.showShimmer();
        databaseReference.child(getResources().getString(R.string.CHILD_BARANG))
                .child(getResources().getString(R.string.CHILD_BARANG_ALL))
                .orderByChild("idpenjual")
                .equalTo(firebaseUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            shimmerRecyclerBarang.hideShimmer();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        shimmerRecyclerBarang.hideShimmer();
                    }
                });
    }
}
