package com.pmo.iderin;

import android.content.Context;
import android.content.Intent;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pmo.iderin.Admin.AdminActivity;
import com.pmo.iderin.Helpers.Alert;
import com.pmo.iderin.Profile.AddProfil;
import com.pmo.iderin.models.profil_model;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.pmo.iderin.Helpers.windowManager.getTransparentStatusBar;

public class Auth_login extends AppCompatActivity {
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

    private boolean isLayoutNohp = true;


    private Context context = Auth_login.this;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    private String verificationid;
    private profil_model profil;
    private  boolean isExist = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_login);
        getTransparentStatusBar(this);
        ButterKnife.bind(this);
      //  getTransparentStatusBar(this);
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
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        databaseReference.child(getResources().getString(R.string.CHILD_AKUN))
                                .child(getResources().getString(R.string.CHILD_AKUN_PROFIL))
                                .child(firebaseAuth.getCurrentUser().getUid())
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){
                                            profil_model model = new profil_model();
                                            model = dataSnapshot.getValue(profil_model.class);
                                            if(model.getLevel().toString().equalsIgnoreCase("ADMIN")){
                                                profil = new profil_model();
                                                profil.setNohp(etNohp.getText().toString());
                                                profil.setUpdated_at(new Date().getTime());
                                                databaseReference
                                                        .child(getResources().getString(R.string.CHILD_AKUN))
                                                        .child(getResources().getString(R.string.CHILD_AKUN_PROFIL))
                                                        .child(firebaseAuth.getUid())
                                                        .setValue(profil)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                startActivity(new Intent(context, AdminActivity.class));
                                                                finish();
                                                            }
                                                        });
                                            }else if(model.getLevel().toString().equalsIgnoreCase("TOKO")) {
                                                profil = new profil_model();
                                                profil.setNohp(etNohp.getText().toString());
                                                profil.setUpdated_at(new Date().getTime());
                                                databaseReference
                                                        .child(getResources().getString(R.string.CHILD_AKUN))
                                                        .child(getResources().getString(R.string.CHILD_AKUN_PROFIL))
                                                        .child(firebaseAuth.getUid())
                                                        .setValue(profil)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                startActivity(new Intent(context, PenjualActivity.class));
                                                                finish();
                                                            }
                                                        });
                                            }else if( model.getLevel().toString().equalsIgnoreCase("USER")){
                                                profil = new profil_model();
                                                profil.setNohp(etNohp.getText().toString());
                                                profil.setUpdated_at(new Date().getTime());
                                                databaseReference
                                                        .child(getResources().getString(R.string.CHILD_AKUN))
                                                        .child(getResources().getString(R.string.CHILD_AKUN_PROFIL))
                                                        .child(firebaseAuth.getUid())
                                                        .setValue(profil)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                startActivity(new Intent(context, MainActivity.class));
                                                                finish();
                                                            }
                                                        });
                                            }else {

                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                    } else {
                        Toast.makeText(context, "gagal" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
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
            Auth_login.this.verificationid = s;
        }


    };

    private boolean isnohpexist(){

        databaseReference.child(getResources().getString(R.string.CHILD_AKUN))
                .child(getResources().getString(R.string.CHILD_AKUN_PROFIL))
                .orderByChild("nohp")
                .startAt(etNohp.getText().toString())
                .endAt(etNohp.getText().toString())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            isExist = true;
                        }else {
                            isExist = false;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        isExist = false;
                    }
                });
        return isExist;
    }
    @OnClick({R.id.btn_masuk, R.id.tv_toregister,R.id.btn_kode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_masuk:
                if(!TextUtils.isEmpty(etNohp.getText().toString())){
                   if(isnohpexist()){
                       lyKode.setVisibility(View.VISIBLE);
                       lyNohp.setVisibility(View.GONE);
                       sendAuthenticationCode(etNohp.getText().toString());
                       isLayoutNohp = false;
                   }else {
                       //akun tidak ditemukan
                        new Alert(context).toast("Akun tidak ada silahkan mendaftar",1);
                   }
                }else {
                    Toast.makeText(context,"Isi no hp!",Toast.LENGTH_LONG).show();
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
                }catch (NullPointerException x){
                    Toast.makeText(context,"Maaf gagal mengirim verifikasi",Toast.LENGTH_LONG).show();
                    x.printStackTrace();
                }
                break;
            case R.id.tv_toregister:
                startActivity(new Intent(context,Auth_Register.class));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(isLayoutNohp){
            finish();
            super.onBackPressed();
        }else {
            lyNohp.setVisibility(View.VISIBLE);
            lyKode.setVisibility(View.GONE);
            isLayoutNohp = true;
        }
    }
}
