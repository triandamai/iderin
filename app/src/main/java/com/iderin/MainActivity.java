package com.iderin;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.iderin.Helpers.windowManager;
import com.iderin.main.fragment_explore;
import com.iderin.main.fragment_profil;
import com.iderin.main.fragment_shop;
import com.pmo.iderin.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.ibrahimsn.lib.OnItemReselectedListener;
import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class MainActivity extends AppCompatActivity implements OnItemReselectedListener, OnItemSelectedListener {
    @BindView(R.id.bottom)
    SmoothBottomBar bottom;
    @BindView(R.id.fl_container)
    FrameLayout flContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        windowManager.getTransparentStatusBar(this);
        bottom.setOnItemReselectedListener(this);
        bottom.setOnItemSelectedListener(this);

        loadFragment(new fragment_explore());
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
                fragment = new fragment_explore();
                break;
            case 1:
                fragment = new fragment_shop();


                break;
//            case 2:
//                fragment = new fragment_shop();
//                break;
            case 2:
                fragment = new fragment_profil();
                break;

        }
        loadFragment(fragment);

    }

    @Override
    public void onItemSelect(int i) {
        Fragment fragment = null;
        switch (i) {
            case 0:
                fragment = new fragment_explore();
                break;
            case 1:
                fragment = new fragment_shop();


                break;
//            case 2:
//                fragment = new fragment_shop();
//                break;
            case 2:
                fragment = new fragment_profil();
                break;

        }
        loadFragment(fragment);
    }
}
