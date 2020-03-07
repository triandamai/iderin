package com.toko;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.core.models.barang_model;
import com.core.models.cart_model;
import com.core.models.detail_cart_model;
import com.core.models.satuan_model;
import com.core.models.toko_model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    @BindView(R.id.tv_jml_keranjang)
    TextView tvJmlKeranjang;
    @BindView(R.id.cv_to_cart)
    CardView cvToCart;
    private Context context = Toko.this;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private List<barang_model> barang_models = new ArrayList<>();
    private AdapterBarangUser adapter;
    private KirimkeBottomSheet kirimkeBottomSheet;
    private double total = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toko);
        ButterKnife.bind(this);
        getTransparentStatusBar(this);

        cvToCart.setVisibility(View.GONE);

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            String idtoko = intent.getStringExtra("idtoko");
            Log.e("IDERIN", idtoko);
            getData(idtoko);
            getCart(idtoko);
        }
    }

    private void getCart(String id) {
        databaseReference.child(getString(R.string.CHILD_ORDER))
                .child(getString(R.string.CHILD_ORDER_CART))
                .child(firebaseUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            cvToCart.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
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
                                model.setId(data.getKey());
                                assert model != null;
                                barang_models.add(model);

                            }
                            adapter = new AdapterBarangUser(context, barang_models, new AdapterBarangUser.onAdapterBaranglistener() {
                                @Override
                                public void onItemClick(barang_model model, int pos) {
                                    Log.e("IDERIN", "" + model.getId());
                                    BottomSheetBarangClicked bs = new BottomSheetBarangClicked();
                                    Bundle bundle = new Bundle();
                                    bundle.putString(getString(R.string.BUNDLE_NAMA_BARANG), model.getNama());
                                    bundle.putString(getString(R.string.BUNDLE_ID_BARANG), model.getId());
                                    bundle.putInt(getString(R.string.BUNDLE_POSISI), pos);
                                    bundle.putDouble(getString(R.string.BUNDLE_HARGA), model.getHarga());
                                    bundle.putString(getString(R.string.BUNDLE_ID_SATUAN), model.getIdsatuan());
                                    bs.setArguments(bundle);

                                    bs.setOnBottomSheetListener(new BottomSheetBarangClicked.BottomSheetListener() {
                                        @Override
                                        public void onOptionClick(String satuan, int jml, barang_model mod, int pos, double total) {
                                            cart_model modelcart = new cart_model();
                                            modelcart.setIdPembeli(firebaseUser.getUid());
                                            modelcart.setStatus("Belum bayar");
                                            modelcart.setTanggal(String.valueOf(new Date().getTime()));
                                            modelcart.setTimestamp(new Date().getTime());
                                            modelcart.setIdpenjual(model.getIdtoko());
                                            modelcart.setTotal(total);
                                            databaseReference.child(getString(R.string.CHILD_ORDER))
                                                    .child(getString(R.string.CHILD_ORDER_CART))
                                                    .child(firebaseUser.getUid())
                                                    .setValue(modelcart)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            detail_cart_model detailcart = new detail_cart_model();
                                                            detailcart.setIdBarang(mod.getId());
                                                            detailcart.setIddetail(firebaseUser.getUid());
                                                            detailcart.setJml(jml);
                                                            detailcart.setSatuan(satuan);
                                                            detailcart.setSubtotal(total);
                                                            databaseReference.child(getString(R.string.CHILD_ORDER))
                                                                    .child(getString(R.string.CHILD_ORDER_DETAIL_CART))
                                                                    .child(firebaseUser.getUid())
                                                                    .child(mod.getId())
                                                                    .setValue(detailcart);
                                                        }
                                                    });
                                            bs.dismiss();
                                        }

                                        @Override
                                        public void onCancel() {
                                            bs.dismiss();
                                        }
                                    });
                                    bs.show(getSupportFragmentManager(), "pick jml");
                                }
                            });
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

    public void setKirimkeBottom(KirimkeBottomSheet kirimkeBottom) {
        this.kirimkeBottomSheet = kirimkeBottom;
    }

    @OnClick({R.id.cv_detail, R.id.cv_to_cart})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cv_detail:
                break;
            case R.id.cv_to_cart:
                Intent intent = new Intent();
                intent.setClassName(context, "com.transaksi.Checkout");
                startActivity(intent);
                break;
        }
    }

    public interface KirimkeBottomSheet {
        void passingData(barang_model model_barang, satuan_model model_satuan);
    }
}
