package com.iderin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.core.models.barang_model;
import com.core.models.detail_cart_model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pmo.iderin.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Adapter_detail_transaksi extends RecyclerView.Adapter<Adapter_detail_transaksi.MyViewHolder> {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    private List<detail_cart_model> list = new ArrayList<>();
    private Context context;

    public Adapter_detail_transaksi(Context context, List<detail_cart_model> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_detail_transaksi, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        detail_cart_model model = list.get(position);

        databaseReference.child(context.getString(R.string.CHILD_BARANG))
                .child(context.getString(R.string.CHILD_BARANG_ALL))
                .child(model.getIdBarang())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            barang_model barang = new barang_model();
                            barang = dataSnapshot.getValue(barang_model.class);
                            holder.tvNama.setText(barang.getNama());
                            holder.tvHarga.setText(String.valueOf(barang.getHarga()));
                            Picasso.get().load(barang.getFoto()).into(holder.ivGambarbarang);


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        holder.tvSubtotal.setText("Rp " + model.getSubtotal());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_gambarbarang)
        ImageView ivGambarbarang;
        @BindView(R.id.tv_nama)
        TextView tvNama;
        @BindView(R.id.tv_harga)
        TextView tvHarga;
        @BindView(R.id.tv_subtotal)
        TextView tvSubtotal;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
