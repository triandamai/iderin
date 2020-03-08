package com.mitra;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.ibrahimsn.lib.OnItemReselectedListener;
import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

import static com.iderin.Helpers.windowManager.getTransparentStatusBar;

public class Mitra extends AppCompatActivity implements OnItemReselectedListener, OnItemSelectedListener,fragment_profilpenjual.onProfilListener{
    @BindView(R.id.fl_container)
    FrameLayout flContainer;
    @BindView(R.id.bottom)
    SmoothBottomBar bottom;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private fragment_beranda beranda;
    private fragment_profilpenjual profil;
    private fragment_transaksi transaksi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mitra);
        ButterKnife.bind(this);

        getTransparentStatusBar(this);
        bottom.setOnItemReselectedListener(this);
        bottom.setOnItemSelectedListener(this);

        beranda = new fragment_beranda();
        profil = new fragment_profilpenjual();
        transaksi = new fragment_transaksi();
        profil.setOnProfillistener(this);
        loadFragment(beranda);
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public void onItemReselect(int i) {
        Fragment fragment = null;
        switch (i) {
            case 0:
                fragment = beranda;
                break;
            case 1:
                fragment = transaksi;
                break;
            case 2:
                fragment = profil;
                break;

        }
        loadFragment(fragment);

    }

    @Override
    public void onItemSelect(int i) {
        Fragment fragment = null;
        switch (i) {
            case 0:
                fragment = beranda;
                break;
            case 1:
                fragment = transaksi;
                break;
            case 2:
                fragment = profil;

                break;

        }
        loadFragment(fragment);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onSignout() {
        firebaseAuth.signOut();
        Intent login = new Intent();
            login.setClassName(Mitra.this,"com.verifikasi.Login");
            startActivity(login);
            finish();
    }
}
