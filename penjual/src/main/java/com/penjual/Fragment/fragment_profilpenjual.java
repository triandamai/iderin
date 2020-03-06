package com.penjual.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.core.models.profil_model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.penjual.Adapter.AdapterBarang;
import com.penjual.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;


public class fragment_profilpenjual extends Fragment {


    @BindView(R.id.profile_image)
    CircleImageView profileImage;
    @BindView(R.id.tv_nama)
    TextView tvNama;
    @BindView(R.id.tv_btn_edit_profil)
    TextView tvBtnEditProfil;
    @BindView(R.id.tv_detail)
    TextView tvDetail;
    @BindView(R.id.ly_btn_manage_address)
    LinearLayout lyBtnManageAddress;
    @BindView(R.id.ly_btn_iderpay)
    LinearLayout lyBtnIderpay;
    @BindView(R.id.ly_btn_help)
    LinearLayout lyBtnHelp;
    @BindView(R.id.ly_btn_logout)
    LinearLayout lyBtnLogout;
    @BindView(R.id.ly_btn_mytoko)
    LinearLayout lyBtnMytoko;
    private Unbinder unbinder;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private AdapterBarang adapter;


    public fragment_profilpenjual() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profilpenjual, container, false);
        unbinder = ButterKnife.bind(this, v);
        getProfil();
        return v;
    }

    private void getProfil() {
        databaseReference.child(getResources().getString(R.string.CHILD_AKUN))
                .child(getResources().getString(R.string.CHILD_AKUN_PROFIL))
                .child(firebaseUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            profil_model model = new profil_model();
                            model = dataSnapshot.getValue(profil_model.class);
                            model.setUid(dataSnapshot.getKey());
                            assert model != null;
                            tvNama.setText(model.getNama().toString());
                            tvDetail.setText("+62" + model.getNohp());
                            Picasso.get().load(model.getFoto().toString()).into(profileImage);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @OnClick({R.id.profile_image,
            R.id.ly_btn_mytoko,
            R.id.tv_btn_edit_profil,
            R.id.ly_btn_manage_address,
            R.id.ly_btn_iderpay,
            R.id.ly_btn_help, R.id.ly_btn_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.profile_image:
                break;
            case R.id.tv_btn_edit_profil:
                Intent addprofil = new Intent();
                addprofil.setClassName(getContext(), "com.profil.AddProfil");
                getContext().startActivity(addprofil);

                break;
            case R.id.ly_btn_manage_address:
                break;
            case R.id.ly_btn_iderpay:
                break;
            case R.id.ly_btn_help:
                break;
            case R.id.ly_btn_logout:
                break;
            case R.id.ly_btn_mytoko:
                Intent addtoko = new Intent();
                addtoko.setClassName(getContext(), "com.profil.AddToko");
                getContext().startActivity(addtoko);
                break;

        }
    }
}
