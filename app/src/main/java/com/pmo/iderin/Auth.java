package com.pmo.iderin;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.pmo.iderin.Helpers.windowManager.getTranparentStatusBar;

public class Auth extends AppCompatActivity {


    private Context context = Auth.this;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    private String verificationid;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        ButterKnife.bind(this);
      //  getTranparentStatusBar(this);
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

                            databaseReference
                                    .child(getResources().getString(R.string.CHILD_AKUN))
                                    .child(firebaseAuth.getUid())
                                    .child("PROFIL")
                                    .child("nohp")
                                    .setValue(etNohp.getText().toString())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    });
                        } else {
                            Toast.makeText(context, "gagal" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }
    //Interface untuk listener apakah ada kode masuk atau tidak
    PhoneAuthProvider.OnVerificationStateChangedCallbacks callback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        //jika da kode dan terbaca akan otomatis memverifikasi
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
            Auth.this.verificationid = s;
        }


    };

    @OnClick({R.id.btn_masuk, R.id.tv_toregister,R.id.btn_kode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_masuk:
                if(!TextUtils.isEmpty(etNohp.getText().toString())){
                    lyKode.setVisibility(View.VISIBLE);
                    lyNohp.setVisibility(View.GONE);
                    sendAuthenticationCode(etNohp.getText().toString());
                }
                break;
            case R.id.btn_kode:
                if (!verificationid.isEmpty() || verificationid != null) {
                    PhoneAuthCredential credential =
                            PhoneAuthProvider.getCredential(verificationid, etKode.getText().toString());
                    signInwithPhoneNumber(credential);
                }
                break;
            case R.id.tv_toregister:
                break;
        }
    }
}
