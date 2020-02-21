package com.pmo.iderin.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pmo.iderin.R;
import com.pmo.iderin.adapters.Adapter_alamat;
import com.pmo.iderin.models.alamat_model;
import com.todkars.shimmer.ShimmerRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.pmo.iderin.Helpers.windowManager.getTranparentStatusBar;

public class ManageAlamatActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_alamat)
    Toolbar toolbarAlamat;
    @BindView(R.id.shimmer_recycler_alamat)
    ShimmerRecyclerView shimmerRecyclerAlamat;
    @BindView(R.id.btn_add_alamat)
    Button btnAddAlamat;
    @BindView(R.id.ly_kosong)
    LinearLayout lyKosong;

    private Context context = ManageAlamatActivity.this;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private List<alamat_model> alamat_modelList = new ArrayList<>();
    private Adapter_alamat adapter_alamat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_alamat);
        ButterKnife.bind(this);
        getTranparentStatusBar(this);
        shimmerRecyclerAlamat.showShimmer();
        getDataALamat();
    }

    public void getDataALamat(){
        databaseReference
                .child(getResources().getString(R.string.CHILD_AKUN))
                .child(getResources().getString(R.string.CHILD_ALAMAT))
                .child(firebaseUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            shimmerRecyclerAlamat.hideShimmer();
                            for (DataSnapshot data : dataSnapshot.getChildren()){
                                alamat_model alamat;
                                alamat = data.getValue(alamat_model.class);
                                assert alamat != null;
                                alamat_modelList.add(alamat);
                            }
                            adapter_alamat = new Adapter_alamat(context,alamat_modelList);
                            shimmerRecyclerAlamat.setAdapter(adapter_alamat);
                        }else {
                            lyKosong.setVisibility(View.VISIBLE);
                            shimmerRecyclerAlamat.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
    @OnClick({R.id.btn_add_alamat})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.btn_add_alamat:
                startActivity(new Intent(context,AddAlamat.class));
                break;
        }
    }
}
