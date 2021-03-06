package com.admin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.iderin.Helpers.windowManager.getTransparentStatusBar;

public class Admin extends AppCompatActivity {

    @BindView(R.id.ly_manage_kategori)
    LinearLayout lyManageKategori;
    @BindView(R.id.ly_manage_barang)
    LinearLayout lyManageBarang;
    @BindView(R.id.ly_manage_user)
    LinearLayout lyManageUser;

    private Context context = Admin.this;

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
                startActivity(new Intent(context, Kategori.class));
                break;
            case R.id.ly_manage_barang:
                startActivity(new Intent(context,ManageBarang.class));
                break;
            case R.id.ly_manage_user:
                startActivity(new Intent(context,ManageUser.class));
                break;
        }
    }
}
