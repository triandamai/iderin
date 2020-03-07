package com.transaksi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.core.models.cart_model;
import com.core.models.detail_cart_model;
import com.core.models.transaksi_model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iderin.Helpers.Alert;
import com.todkars.shimmer.ShimmerRecyclerView;
import com.transaksi.Adapters.AdapterItemCart;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Checkout extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.shimmer_recycler_checkout)
    ShimmerRecyclerView shimmerRecyclerCheckout;
    @BindView(R.id.tv_total)
    TextView tvTotal;
    @BindView(R.id.tv_total_bayar)
    TextView tvTotalBayar;
    @BindView(R.id.tv_metode_bayar)
    TextView tvMetodeBayar;
    @BindView(R.id.iv_metode_bayar)
    ImageView ivMetodeBayar;
    @BindView(R.id.rl_metode)
    RelativeLayout rlMetode;
    @BindView(R.id.btn_proses)
    Button btnProses;

    private Context context = Checkout.this;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private double total = 0;
    private String method = "";
    cart_model cartmodel = new cart_model();
    private String idpenjual = "";
    private String idTransaksi = "";

    private List<detail_cart_model> list = new ArrayList<>();
    private AdapterItemCart adapterItemCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        ButterKnife.bind(this);
        shimmerRecyclerCheckout.showShimmer();
        getDetailCart();
        getItemCart();
    }

    private void getDetailCart() {
        databaseReference.child(getString(R.string.CHILD_ORDER))
                .child(getString(R.string.CHILD_ORDER_CART))
                .child(firebaseUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            cartmodel = dataSnapshot.getValue(cart_model.class);
                            cartmodel.setIdcart(dataSnapshot.getKey());
                            idpenjual = cartmodel.getIdpenjual();


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void getItemCart() {
        databaseReference.child(getString(R.string.CHILD_ORDER))
                .child(getString(R.string.CHILD_ORDER_DETAIL_CART))
                .child(firebaseUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            list.clear();
                            detail_cart_model modelcart = new detail_cart_model();
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                modelcart = data.getValue(detail_cart_model.class);
                                modelcart.setIddetail(data.getKey());

                                assert modelcart != null;
                                total += modelcart.getSubtotal();
                                list.add(modelcart);

                            }

                            adapterItemCart = new AdapterItemCart(context, list);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
                            shimmerRecyclerCheckout.setLayoutManager(layoutManager);
                            shimmerRecyclerCheckout.setAdapter(adapterItemCart);
                            tvTotalBayar.setText("Rp " + String.valueOf(total));
                            tvTotal.setText("Rp" + String.valueOf(total));

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @OnClick({R.id.rl_metode, R.id.btn_proses})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_metode:
                BottomSheetBarangClicked bs = new BottomSheetBarangClicked();
                Bundle bundle = new Bundle();
                bundle.putDouble("total", cartmodel.getTotal());

                bs.setOnBottomSheetListener(new BottomSheetBarangClicked.BottomSheetListener() {
                    @Override
                    public void onOptionClick(int tipebayar) {

                        if (tipebayar == 1) {
                            method = "Tunai";
                            tvMetodeBayar.setText("Bayar Tunai Rp" + cartmodel.getTotal());
                        } else {
                            method = "Ider pay";
                            tvMetodeBayar.setText("Saldo Iderpay");
                        }
                        bs.dismiss();
                    }

                    @Override
                    public void onCancel() {
                        bs.dismiss();
                    }
                });
                bs.show(getSupportFragmentManager(), "pick method");

                break;
            case R.id.btn_proses:
                if (method.toString().equalsIgnoreCase("")) {
                    new Alert(context).toast("Pilih metode pembayaran terlebih dahulu", Toast.LENGTH_LONG);
                } else {
                    idTransaksi = databaseReference.push().getKey();
                    transaksi_model transaksi_model = new transaksi_model();
                    transaksi_model.setIdpembeli(firebaseUser.getUid());
                    transaksi_model.setIdtransaksi(idTransaksi);
                    transaksi_model.setMetode_pembayaran(method);
                    transaksi_model.setTanggal(new Date().getTime());
                    transaksi_model.setStatus("dipesan");
                    transaksi_model.setTotal(total);
                    transaksi_model.setIdpenjual(cartmodel.getIdpenjual());
                    databaseReference.child(getString(R.string.CHILD_TRANSAKSI))
                            .child(getString(R.string.CHILD_TRANSAKSI))
                            .child(idTransaksi)
                            .setValue(transaksi_model).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            pindahKetransaksi(databaseReference
                                            .child(getString(R.string.CHILD_ORDER))
                                    , databaseReference
                                            .child(getString(R.string.CHILD_TRANSAKSI))
                                            .child(getString(R.string.CHILD_TRANSAKSI_DETAIL))
                                            .child(idTransaksi), idTransaksi);
                        }
                    });
                }

                break;
        }
    }

    private void pindahKetransaksi(DatabaseReference from, DatabaseReference to, String idTransaksi) {
        from.child(getString(R.string.CHILD_ORDER_DETAIL_CART)).child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    to.setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            from.child(getString(R.string.CHILD_ORDER_CART)).child(firebaseUser.getUid()).removeValue();
                            from.child(getString(R.string.CHILD_ORDER_DETAIL_CART)).child(firebaseUser.getUid()).removeValue();

                            Intent kebase = new Intent();
                            kebase.setClassName(context, "com.iderin.MainActivity");
                            startActivity(kebase);
                            finish();

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
