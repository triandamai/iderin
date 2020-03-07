package com.toko.Adapter;

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
import com.toko.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterBarangUser extends RecyclerView.Adapter<AdapterBarangUser.MyViewHolder> {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    private List<barang_model> list;
    private Context context;
    private String namaKategori = "";
    satuan_model model_satuan = new satuan_model();
    private onAdapterBaranglistener onAdapterBaranglistener;

    public interface onAdapterBaranglistener {
        void onItemClick(barang_model model, int pos);
    }

    public AdapterBarangUser(Context context, List<barang_model> data, onAdapterBaranglistener onAdapterBaranglistener) {
        this.context = context;
        this.list = data;
        this.onAdapterBaranglistener = onAdapterBaranglistener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_barang_user, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        barang_model model_barang = list.get(position);


        holder.tvNamaBarang.setText(model_barang.getNama());
        Picasso.get().load(model_barang.getFoto()).into(holder.ivBarangGambar);
        databaseReference
                .child(context.getResources().getString(R.string.CHILD_BARANG))
                .child(context.getResources().getString(R.string.CHILD_BARANG_KATEGORI))
                .child(model_barang.getIdkategori().toString())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            barang_model model = new barang_model();
                            model = dataSnapshot.getValue(barang_model.class);
                            model.setId(dataSnapshot.getKey());
                            assert model != null;
                            namaKategori = model.getNama();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        databaseReference.child(context.getResources().getString(R.string.CHILD_BARANG))
                .child(context.getResources().getString(R.string.CHILD_BARANG_SATUAN))
                .child(model_barang.getIdsatuan())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            model_satuan = dataSnapshot.getValue(satuan_model.class);
                            model_satuan.setIdsatuan(dataSnapshot.getKey());
                            holder.tvHargaQty.setText("Rp " + model_barang.getHarga() + "/" + model_satuan.getNama());
                            holder.tvBtnEdit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    onAdapterBaranglistener.onItemClick(model_barang, position);
                                }
                            });
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
        @BindView(R.id.tv_nama_barang)
        TextView tvNamaBarang;
        @BindView(R.id.tv_harga_qty)
        TextView tvHargaQty;
        @BindView(R.id.tv_btn_edit)
        TextView tvBtnEdit;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
