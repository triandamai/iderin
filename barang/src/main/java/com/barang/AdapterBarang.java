package com.barang;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.core.models.barang_model;
import com.core.models.satuan_model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class AdapterBarang extends RecyclerView.Adapter<AdapterBarang.MyViewHolder> {


    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private List<barang_model> list = new ArrayList<>();
    private Context context;
    private Unbinder unbinder;

    public AdapterBarang(Context context, List<barang_model> modelList) {
        this.context = context;
        this.list = modelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_barang_pembeli_toko, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        barang_model model = list.get(position);
        holder.tvBarangNama.setText(model.getNama().toString());
        Picasso.get().load(model.getFoto().toString()).into(holder.ivBarangGambar);
        databaseReference.child(context.getString(R.string.CHILD_BARANG))
                .child(context.getString(R.string.CHILD_BARANG_SATUAN))
                .child(model.getIdsatuan())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            satuan_model satuan = dataSnapshot.getValue(satuan_model.class);
                            holder.tvBarangHargaStok.setText("Rp " + model.getHarga() + "/" + satuan.getNama());
                        } else {
                            holder.tvBarangHargaStok.setText("Rp " + model.getHarga() + "tidak ada satuan");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_barang_gambar)
        ImageView ivBarangGambar;
        @BindView(R.id.tv_barang_nama)
        TextView tvBarangNama;
        @BindView(R.id.tv_barang_harga_stok)
        TextView tvBarangHargaStok;
        @BindView(R.id.add)
        TextView add;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);
        }
    }
}
