package com.iderin;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.core.models.kategori_model;
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

public class AllKategori extends AppCompatActivity {
    @BindView(R.id.shimmer_recycler_kategori)
    ShimmerRecyclerView shimmerRecyclerKategori;
    private Context context = AllKategori.this;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    private String verificationid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_kategori);
        ButterKnife.bind(this);
        getKategori();
    }

    private void getKategori() {
        databaseReference.child(getResources().getString(R.string.CHILD_BARANG))
                .child(getResources().getString(R.string.CHILD_BARANG_KATEGORI))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            for (DataSnapshot data : dataSnapshot.getChildren()){
                                kategori_model model = new kategori_model();
                                model = data.getValue(kategori_model.class);
                                assert model != null;

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
