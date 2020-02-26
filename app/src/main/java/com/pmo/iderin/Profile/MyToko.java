package com.pmo.iderin.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pmo.iderin.R;
import com.pmo.iderin.adapters.Adapter_barang_toko;
import com.pmo.iderin.models.barang_model;
import com.todkars.shimmer.ShimmerRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.pmo.iderin.Helpers.windowManager.getTransparentStatusBar;

public class MyToko extends AppCompatActivity {


    @BindView(R.id.shimmer_recycler_toko)
    ShimmerRecyclerView shimmerRecyclerToko;
    @BindView(R.id.fl_image)
    FrameLayout flImage;
    @BindView(R.id.btn_add)
    Button btnAdd;
    @BindView(R.id.cv_detail)
    CardView cvDetail;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    private Context context = MyToko.this;
    private List<barang_model> list = new ArrayList<>();
    private Adapter_barang_toko adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_toko);
        ButterKnife.bind(this);
        getTransparentStatusBar(this);
         getBarangToko();

    }

    private void getBarangToko() {
        databaseReference
                .child(getResources().getString(R.string.CHILD_BARANG))
                .child(getResources().getString(R.string.CHILD_BARANG_ALL))
                .orderByChild("idtoko")
                .equalTo(firebaseUser.getUid().toString())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    try {
                        list.clear();
                        barang_model barang_model = new barang_model();
                        for (DataSnapshot datas : dataSnapshot.getChildren()) {
                            barang_model = datas.getValue(barang_model.class);
                            barang_model.setId(datas.getKey());
                            assert barang_model != null;
                            list.add(barang_model);
                        }
                        adapter = new Adapter_barang_toko(context, list);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
                        shimmerRecyclerToko.setLayoutManager(layoutManager);
                        shimmerRecyclerToko.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @OnClick({R.id.btn_add, R.id.cv_detail})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                startActivity(new Intent(context,Addbarang.class));
                break;
            case R.id.cv_detail:
                break;
        }
    }
}
