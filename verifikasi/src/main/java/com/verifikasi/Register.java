package com.verifikasi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.core.models.profil_model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.iderin.Helpers.windowManager;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Register extends AppCompatActivity {

    @BindView(R.id.btn_masuk)
    Button btnMasuk;
    @BindView(R.id.tv_toregister)
    TextView tvToregister;
    @BindView(R.id.et_nohp)
    EditText etNohp;
    @BindView(R.id.ly_nohp)
    LinearLayout lyNohp;
    @BindView(R.id.et_kode)
    EditText etKode;
    @BindView(R.id.ly_kode)
    LinearLayout lyKode;
    @BindView(R.id.btn_kode)
    Button btnKode;
    @BindView(R.id.progress)
    ProgressBar progress;
    @BindView(R.id.tv_timer)
    TextView tvTimer;
    @BindView(R.id.ly_login)
    LinearLayout lyLogin;

    private boolean isLayoutNohp = true;


    private Context context = Register.this;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    private String verificationid;
    private profil_model profil;
    private int waktu = 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        windowManager.getTransparentStatusBar(this);
        ButterKnife.bind(this);
        isLayoutNohp = true;
        lyNohp.setVisibility(View.VISIBLE);
        lyKode.setVisibility(View.GONE);
    }

    public void sendAuthenticationCode(String phonenumber) {
        Toast.makeText(context, "Mengirim", Toast.LENGTH_LONG).show();
        PhoneAuthProvider.getInstance().verifyPhoneNumber("+62" + phonenumber, 60, TimeUnit.SECONDS, this, callback);
    }


    public void signInwithPhoneNumber(PhoneAuthCredential phoneAuthCredential) {
        Toast.makeText(context, "Memverifikasi..", Toast.LENGTH_LONG).show();
        firebaseAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(context, "Berhasil", Toast.LENGTH_LONG).show();
                            profil = new profil_model();
                            profil.setNohp(etNohp.getText().toString());
                            profil.setCreated_at(new Date().getTime());
                            profil.setUpdated_at(new Date().getTime());
                            profil.setLevel(getResources().getString(R.string.LEVEL_USER));
                            profil.setNama("Pengguna baru");

                            databaseReference
                                    .child(getResources().getString(R.string.CHILD_AKUN))
                                    .child(getResources().getString(R.string.CHILD_AKUN_PROFIL))
                                    .child(firebaseAuth.getUid())
                                    .setValue(profil)
                                    .addOnSuccessListener(aVoid -> {
                                        Intent profil = new Intent();
                                        profil.setClassName(context, "com.profil.AddProfil");
                                        startActivity(profil);
                                        finish();
                                    });
                        } else {
                            Toast.makeText(context, "gagal" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks callback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                etKode.setText(code);

                signInwithPhoneNumber(phoneAuthCredential);
            }

        }

        //ketika gagal
        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(context, "Verifikasi gagal" + e.toString(), Toast.LENGTH_LONG).show();
        }

        //jika code berhasi dikirim
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            Register.this.verificationid = s;
        }


    };


    @OnClick({R.id.btn_masuk, R.id.tv_toregister, R.id.btn_kode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_masuk:
                if (!TextUtils.isEmpty(etNohp.getText().toString())) {
                    lyKode.setVisibility(View.VISIBLE);
                    lyNohp.setVisibility(View.GONE);
                    lyLogin.setVisibility(View.GONE);
                    sendAuthenticationCode(etNohp.getText().toString());
                    isLayoutNohp = false;
                    new CountDownTimer(60000, 1000) {

                        @Override
                        public void onTick(long millisUntilFinished) {
                            if (waktu == 60) {
                                tvTimer.setText("01 : 00");
                            } else {
                                tvTimer.setText("00 : " + String.valueOf(waktu));
                            }

                            waktu--;
                        }

                        @Override
                        public void onFinish() {
                            waktu = 60;
                            tvTimer.setText("Kirim Ulang ?");
                            tvTimer.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    lyKode.setVisibility(View.GONE);
                                    lyNohp.setVisibility(View.VISIBLE);
                                    progress.setVisibility(View.GONE);
                                    lyLogin.setVisibility(View.VISIBLE);
                                    isLayoutNohp = true;
                                }
                            });
                        }
                    }.start();
                } else {
                    Toast.makeText(context, "Isi no hp!", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btn_kode:
                try {
                    if (!verificationid.isEmpty()) {
                        PhoneAuthCredential credential =
                                PhoneAuthProvider.getCredential(verificationid, etKode.getText().toString());
                        signInwithPhoneNumber(credential);
                        isLayoutNohp = false;
                    }
                } catch (NullPointerException x) {
                    Toast.makeText(context, "Maaf gagal mengirim verifikasi", Toast.LENGTH_LONG).show();
                    Log.e("IDERIN", x.getMessage());
                    x.printStackTrace();
                }
                break;
            case R.id.tv_toregister:
                startActivity(new Intent(context, Login.class));
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (isLayoutNohp) {
            finish();
            super.onBackPressed();
        } else {
            lyNohp.setVisibility(View.VISIBLE);
            lyKode.setVisibility(View.GONE);
            waktu = 60;
            isLayoutNohp = true;
        }
    }
}
