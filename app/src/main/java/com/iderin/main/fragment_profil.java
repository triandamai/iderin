package com.iderin.main;

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

import com.core.models.profil_model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iderin.Splash;
import com.pmo.iderin.BuildConfig;
import com.pmo.iderin.R;
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
        return v;
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
                            profil = dataSnapshot.getValue(profil_model.class);
                            assert profil != null;
                            Picasso.get().load(profil.getFoto()).into(profileImage);
                            tvNama.setText(profil.getNama());
                            tvDetail.setText(profil.getNohp());

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
            R.id.ly_btn_manage_address,
            R.id.ly_btn_iderpay,
            R.id.ly_btn_help,
            R.id.ly_btn_logout,
            R.id.tv_btn_edit_profil})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ly_btn_myorder:
                Intent order = new Intent();
                order.setClassName(getContext(), "com.profil.MyOrder");
                startActivity(order);
                break;
            case R.id.ly_btn_manage_address:
                Intent alamat = new Intent();
                alamat.setClassName(getContext(), "com.profil.AturAlamat");
                startActivity(alamat);
                break;
            case R.id.ly_btn_iderpay:
                Intent keiderpay = new Intent();
                keiderpay.setClassName(BuildConfig.APPLICATION_ID, "com.iderpay.Iderpay");
                startActivity(keiderpay);
                break;
            case R.id.ly_btn_help:
                break;
            case R.id.ly_btn_logout:
                firebaseAuth.signOut();
                startActivity(new Intent(getContext(), Splash.class));
                getActivity().finish();
                break;
            case R.id.tv_btn_edit_profil:
                Intent profil = new Intent();
                profil.setClassName(getContext(), "com.profil.AddProfil");
                startActivity(profil);
                break;


        }
    }


}
