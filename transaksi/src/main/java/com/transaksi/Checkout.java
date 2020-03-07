package com.transaksi;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.core.models.cart_model;
import com.core.models.detail_cart_model;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.todkars.shimmer.ShimmerRecyclerView;
import com.transaksi.Adapters.AdapterItemCart;

import java.util.ArrayList;
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
    cart_model cartmodel = new cart_model();

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
                break;
        }
    }
}
