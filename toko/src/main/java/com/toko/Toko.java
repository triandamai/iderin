package com.toko;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.core.models.barang_model;
import com.core.models.toko_model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.todkars.shimmer.ShimmerRecyclerView;
import com.toko.Adapter.AdapterBarangUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.iderin.Helpers.windowManager.getTransparentStatusBar;

public class Toko extends AppCompatActivity {


    @BindView(R.id.iv_toko)
    ImageView ivToko;
    @BindView(R.id.fl_image)
    FrameLayout flImage;
    @BindView(R.id.tv_namatoko)
    TextView tvNamatoko;
    @BindView(R.id.tv_alamattoko)
    TextView tvAlamattoko;
    @BindView(R.id.cv_detail)
    CardView cvDetail;
    @BindView(R.id.shimmer_recycler_toko)
    ShimmerRecyclerView shimmerRecyclerToko;
    private Context context = Toko.this;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private List<barang_model> barang_models = new ArrayList<>();
    private AdapterBarangUser adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toko);
        ButterKnife.bind(this);
        getTransparentStatusBar(this);
        Intent intent = getIntent();

        if (intent.getExtras() != null) {
            String idtoko = intent.getStringExtra("idtoko");
            Log.e("IDERIN", idtoko);
            getData(idtoko);
        }
    }

    private void getData(String idtoko) {
        databaseReference.child(getString(R.string.CHILD_AKUN))
                .child(getString(R.string.CHILD_AKUN_TOKO))
                .child(idtoko)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Log.e("IDERIN", "ada");
                            toko_model model = new toko_model();
                            model = dataSnapshot.getValue(toko_model.class);
                            model.setId(dataSnapshot.getKey());
                            assert model != null;
                            setData(model);
                            getDataBarang(model.getId());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("IDERIN", databaseError.getMessage());
                    }
                });

    }

    private void setData(toko_model model) {
        Picasso.get().load(model.getBanner_toko()).into(ivToko);
        tvNamatoko.setText(model.getNamatoko());
        tvAlamattoko.setText(model.getAlamat());
    }


    private void getDataBarang(String id) {
        databaseReference.child(getString(R.string.CHILD_BARANG))
                .child(getString(R.string.CHILD_BARANG_ALL))
                .orderByChild("idtoko")
                .equalTo(id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            barang_models.clear();
                            barang_model model = new barang_model();
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                model = data.getValue(barang_model.class);
                                assert model != null;
                                barang_models.add(model);

                            }
                            adapter = new AdapterBarangUser(context, barang_models);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
                            shimmerRecyclerToko.setLayoutManager(layoutManager);
                            shimmerRecyclerToko.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

}
