package com.pmo.iderin.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pmo.iderin.Profile.AddProfil;
import com.pmo.iderin.Profile.AddToko;
import com.pmo.iderin.Profile.ManageAlamatActivity;
import com.pmo.iderin.Profile.MyOrderActivity;
import com.pmo.iderin.Profile.MyToko;
import com.pmo.iderin.R;
import com.pmo.iderin.Splash;
import com.pmo.iderin.models.profil_model;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;


public class fragment_profil extends Fragment {


    @BindView(R.id.ly_btn_myorder)
    LinearLayout lyBtnMyorder;
    @BindView(R.id.ly_btn_manage_address)
    LinearLayout lyBtnManageAddress;
    @BindView(R.id.ly_btn_iderpay)
    LinearLayout lyBtnIderpay;
    @BindView(R.id.ly_btn_help)
    LinearLayout lyBtnHelp;
    @BindView(R.id.ly_btn_logout)
    LinearLayout lyBtnLogout;
    @BindView(R.id.tv_btn_edit_profil)
    TextView tvBtnEditProfil;
    @BindView(R.id.profile_image)
    CircleImageView profileImage;
    @BindView(R.id.tv_nama)
    TextView tvNama;
    @BindView(R.id.tv_detail)
    TextView tvDetail;
    @BindView(R.id.ly_btn_manage_toko)
    LinearLayout lyBtnManageToko;
    @BindView(R.id.tv_manage_toko)
    TextView tvManageToko;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private boolean hasToko = false;


    public fragment_profil() {
        // Required empty public constructor
    }


    public static fragment_profil newInstance(String param1, String param2) {
        fragment_profil fragment = new fragment_profil();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_profil, container, false);
        ButterKnife.bind(this, v);
        getProfil();
        cekToko();
        return v;
    }

    private void cekToko() {
        databaseReference
                .child(getResources().getString(R.string.CHILD_AKUN))
                .child(getResources().getString(R.string.CHILD_AKUN_TOKO))
                .child(firebaseUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            hasToko = true;
                            tvManageToko.setText("Managemen Toko");
                        } else {
                            tvManageToko.setText("Buka Toko");
                            hasToko = false;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }


    private void getProfil() {
        databaseReference
                .child(getResources().getString(R.string.CHILD_AKUN))
                .child(getResources().getString(R.string.CHILD_AKUN_PROFIL))
                .child(firebaseUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                           profil_model profil = new profil_model();
//                            profil.setNama(dataSnapshot.child("nama").getValue(String.class));
//                            profil.setAlamat(dataSnapshot.child("alamat").getValue(String.class));
//                            profil.setCreated_at(dataSnapshot.child("created_at").getValue(Long.class));
//                            profil.setFoto(dataSnapshot.child("foto").getValue(String.class));
//                            profil.setJenis_kelamin(dataSnapshot.child("jenis_kelamin").getValue(String.class));

                            profil = dataSnapshot.getValue(profil_model.class);
                            assert profil != null;
                            Picasso.get().load(profil.getFoto()).into(profileImage);
                            tvNama.setText(profil.getNama());
                            tvDetail.setText("+62" + profil.getNohp());

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    @OnClick({R.id.ly_btn_myorder,
            R.id.ly_btn_manage_toko,
            R.id.ly_btn_manage_address,
            R.id.ly_btn_iderpay,
            R.id.ly_btn_help,
            R.id.ly_btn_logout,
            R.id.tv_btn_edit_profil})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ly_btn_myorder:
                startActivity(new Intent(getContext(), MyOrderActivity.class));
                break;
            case R.id.ly_btn_manage_address:
                startActivity(new Intent(getContext(), ManageAlamatActivity.class));
                break;
            case R.id.ly_btn_manage_toko:
                if(hasToko) {
                    startActivity(new Intent(getContext(), MyToko.class));
                }else {
                    startActivity(new Intent(getContext(), AddToko.class));
                }
                break;
            case R.id.ly_btn_iderpay:
                break;
            case R.id.ly_btn_help:
                break;
            case R.id.ly_btn_logout:
                firebaseAuth.signOut();
                startActivity(new Intent(getContext(), Splash.class));
                getActivity().finish();
                break;
            case R.id.tv_btn_edit_profil:
                startActivity(new Intent(getContext(), AddProfil.class));
                break;
        }
    }
}
