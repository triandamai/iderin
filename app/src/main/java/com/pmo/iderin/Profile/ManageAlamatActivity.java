package com.pmo.iderin.Profile;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.pmo.iderin.R;
import com.todkars.shimmer.ShimmerRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.pmo.iderin.Helpers.windowManager.getTranparentStatusBar;

public class ManageAlamatActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_alamat)
    Toolbar toolbarAlamat;
    @BindView(R.id.shimmer_recycler_alamat)
    ShimmerRecyclerView shimmerRecyclerAlamat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_alamat);
        ButterKnife.bind(this);
        getTranparentStatusBar(this);
        shimmerRecyclerAlamat.showShimmer();
    }
}
