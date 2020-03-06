package com.iderin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.core.models.profil_model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iderin.Helpers.windowManager;
import com.pmo.iderin.BuildConfig;
import com.pmo.iderin.R;

public class Init extends AppCompatActivity {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private Context context = Init.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        windowManager.getTransparentStatusBar(this);

        if (firebaseUser != null) {
          databaseReference.child(getResources().getString(R.string.CHILD_AKUN))
                  .child(getResources().getString(R.string.CHILD_AKUN_PROFIL))
                  .child(firebaseUser.getUid())
                  .addValueEventListener(new ValueEventListener() {
                      @Override
                      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                          if(dataSnapshot.exists()){
                              profil_model model = new profil_model();
                              model = dataSnapshot.getValue(profil_model.class);
                              assert model != null;
                              if(model.getLevel().toString().equalsIgnoreCase("ADMIN")){
                                  Intent admin = new Intent();
                                  admin.setClassName(BuildConfig.APPLICATION_ID, "com.admin.Admin");
                                  startActivity(admin);
                                  finish();
                              }else if(model.getLevel().toString().equalsIgnoreCase("USER")){
                                  startActivity(new Intent(context, MainActivity.class));
                                  finish();
                              }else if(model.getLevel().toString().equalsIgnoreCase("TOKO")){
                                  startActivity(new Intent(context, PenjualActivity.class));
                                  finish();
                              }
                          }
                      }

                      @Override
                      public void onCancelled(@NonNull DatabaseError databaseError) {

                      }
                  });


        } else {
            startActivity(new Intent(context, Auth_login.class));
            finish();
        }
    }
}
