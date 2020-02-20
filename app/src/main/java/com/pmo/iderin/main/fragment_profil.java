package com.pmo.iderin.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pmo.iderin.Profile.EditProfilActivity;
import com.pmo.iderin.Profile.ManageAlamatActivity;
import com.pmo.iderin.Profile.MyOrderActivity;
import com.pmo.iderin.R;
import com.pmo.iderin.Splash;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


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

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


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
        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    @OnClick({R.id.ly_btn_myorder, R.id.ly_btn_manage_address, R.id.ly_btn_iderpay, R.id.ly_btn_help, R.id.ly_btn_logout,R.id.tv_btn_edit_profil})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ly_btn_myorder:
                startActivity(new Intent(getContext(), MyOrderActivity.class));
                break;
            case R.id.ly_btn_manage_address:
                startActivity(new Intent(getContext(), ManageAlamatActivity.class));
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
                startActivity(new Intent(getContext(), EditProfilActivity.class));
                break;
        }
    }
}
