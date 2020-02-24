package com.pmo.iderin.Admin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.pmo.iderin.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.pmo.iderin.Helpers.windowManager.getTransparentStatusBar;

public class AdminActivity extends AppCompatActivity {

    @BindView(R.id.ly_manage_kategori)
    LinearLayout lyManageKategori;
    @BindView(R.id.ly_manage_barang)
    LinearLayout lyManageBarang;
    @BindView(R.id.ly_manage_user)
    LinearLayout lyManageUser;

    private Context context = AdminActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        ButterKnife.bind(this);
        getTransparentStatusBar(this);
    }

    @OnClick({R.id.ly_manage_kategori, R.id.ly_manage_barang, R.id.ly_manage_user})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ly_manage_kategori:
                startActivity(new Intent(context,KategoriActivity.class));
                break;
            case R.id.ly_manage_barang:
                break;
            case R.id.ly_manage_user:
                break;
        }
    }
}
