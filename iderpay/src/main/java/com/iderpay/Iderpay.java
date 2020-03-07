package com.iderpay;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Iderpay extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.tv_totalsaldo)
    TextView tvTotalsaldo;
    @BindView(R.id.tv_btn_topup)
    TextView tvBtnTopup;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private Context context = Iderpay.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iderpay);
        ButterKnife.bind(this);
        getDataSaldo();
    }

    private void getDataSaldo() {
        databaseReference.child(getString(R.string.CHILD_AKUN))
                .child(getString(R.string.CHILD_AKUN_IDERPAY))
                .child(firebaseUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            double saldo = dataSnapshot.getValue(double.class);
                            double saldosementara = dataSnapshot.getValue(double.class);
                            tvTotalsaldo.setText("Rp " + String.valueOf(saldo));
                        } else {
                            tvTotalsaldo.setText("Rp 000");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @OnClick({R.id.tv_totalsaldo, R.id.tv_btn_topup})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_totalsaldo:

                break;
            case R.id.tv_btn_topup:
                startActivity(new Intent(context, TopUp.class));

                break;
        }
    }
}
