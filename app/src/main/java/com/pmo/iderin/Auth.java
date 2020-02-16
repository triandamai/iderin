package com.pmo.iderin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Auth extends AppCompatActivity {
    private Context context = Auth.this;

    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_masuk)
    Button btnMasuk;
    @BindView(R.id.tv_toregister)
    TextView tvToregister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_masuk, R.id.tv_toregister})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_masuk:
                startActivity(new Intent(context,MainActivity.class));
                break;
            case R.id.tv_toregister:
                break;
        }
    }
}
