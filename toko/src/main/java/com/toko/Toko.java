package com.toko;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.todkars.shimmer.ShimmerRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.iderin.Helpers.windowManager.getTransparentStatusBar;

public class Toko extends AppCompatActivity {
    @BindView(R.id.iv_toko)
    ImageView ivToko;
    @BindView(R.id.fl_image)
    FrameLayout flImage;
    @BindView(R.id.tv_namatoko)
    TextView tvNamatoko;
    @BindView(R.id.tv_alamattoko)
    TextView tvAlamattoko;
    @BindView(R.id.btn_edit_toko)
    Button btnEditToko;
    @BindView(R.id.btn_add)
    Button btnAdd;
    @BindView(R.id.cv_detail)
    CardView cvDetail;
    @BindView(R.id.shimmer_recycler_toko)
    ShimmerRecyclerView shimmerRecyclerToko;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_toko);
        ButterKnife.bind(this);
        getTransparentStatusBar(this);
    }

    @OnClick({R.id.btn_edit_toko, R.id.btn_add, R.id.cv_detail})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_edit_toko:
                break;
            case R.id.btn_add:
                break;
            case R.id.cv_detail:
                break;
        }
    }
}
