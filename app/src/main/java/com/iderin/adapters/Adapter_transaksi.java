package com.iderin.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.core.models.toko_model;
import com.core.models.transaksi_model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iderin.DetailTransaksi;
import com.pmo.iderin.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Adapter_transaksi extends RecyclerView.Adapter<Adapter_transaksi.MyViewHolder> {


    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private List<transaksi_model> list = new ArrayList<>();
    private Context context;

    public Adapter_transaksi(Context context, List<transaksi_model> models) {
        this.context = context;
        this.list = models;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_transaksi, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        transaksi_model model = list.get(position);
        holder.tvTransaksi.setText("kode : " + model.getIdtransaksi());
        holder.tvMetode.setText(model.getMetode_pembayaran());

        databaseReference.child(context.getString(R.string.CHILD_AKUN))
                .child(context.getString(R.string.CHILD_AKUN_TOKO))
                .child(model.getIdpenjual())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            toko_model toko = new toko_model();
                            toko = dataSnapshot.getValue(toko_model.class);
                            holder.tvPenjual.setText(toko.getNamatoko());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        holder.lyParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, DetailTransaksi.class).putExtra("idtransaksi", model.getIdtransaksi()));
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_transaksi)
        TextView tvTransaksi;
        @BindView(R.id.tv_penjual)
        TextView tvPenjual;
        @BindView(R.id.tv_metode)
        TextView tvMetode;
        @BindView(R.id.ly_parent)
        LinearLayout lyParent;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
