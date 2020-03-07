package com.transaksi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
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
import butterknife.Unbinder;

public class BottomSheetBarangClicked extends BottomSheetDialogFragment {

    Unbinder unbinder;
    @BindView(R.id.ly_tunai)
    LinearLayout lyTunai;
    @BindView(R.id.ly_btn_bottomsheet)
    LinearLayout lyBtnBottomsheet;
    @BindView(R.id.tv_saldo)
    TextView tvSaldo;


    private BottomSheetListener mBottomSheetListener;
    private double saldo = 0;
    private double saldosementara = 0;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_pickitem, container, false);
        unbinder = ButterKnife.bind(this, view);

        databaseReference.child(getString(R.string.CHILD_AKUN))
                .child(getString(R.string.CHILD_IDERPAY))
                .child(firebaseUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            saldo = dataSnapshot.getValue(double.class);
                            saldosementara = dataSnapshot.getValue(double.class);
                            tvSaldo.setText("Rp " + String.valueOf(saldo));


                        } else {
                            saldo = 0;
                            saldosementara = 0;
                            tvSaldo.setText("Rp 0000");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void setOnBottomSheetListener(BottomSheetListener mBottomSheetListener) {
        this.mBottomSheetListener = mBottomSheetListener;
    }

    @OnClick({R.id.ly_tunai, R.id.ly_btn_bottomsheet})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ly_tunai:
                break;
            case R.id.ly_btn_bottomsheet:
                if (saldo == 0) {

                } else if (saldo <= 0) {

                } else {
                    mBottomSheetListener.onOptionClick(0);
                }
                break;
        }
    }

    public interface BottomSheetListener {
        void onOptionClick(int tipebayar);

        void onCancel();
    }


}
