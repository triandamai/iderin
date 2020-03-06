package com.mitra;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.ibrahimsn.lib.OnItemReselectedListener;
import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

import static com.iderin.Helpers.windowManager.getTransparentStatusBar;

public class Mitra extends AppCompatActivity implements OnItemReselectedListener, OnItemSelectedListener {
    @BindView(R.id.fl_container)
    FrameLayout flContainer;
    @BindView(R.id.bottom)
    SmoothBottomBar bottom;

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
}
