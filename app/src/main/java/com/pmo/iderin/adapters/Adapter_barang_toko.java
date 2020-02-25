package com.pmo.iderin.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pmo.iderin.R;
import com.pmo.iderin.models.barang_model;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Adapter_barang_toko extends RecyclerView.Adapter<Adapter_barang_toko.MyViewHolder> {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    private List<barang_model> list;
    private Context context;

    public Adapter_barang_toko(Context context, List<barang_model> data) {
        this.context = context;
        this.list = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_barang_toko, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        barang_model barang_model = list.get(position);
        holder.tvNamaBarang.setText(barang_model.getNama());
        holder.tvHargaQty.setText("Rp."+barang_model.getHarga()+"Stok:"+barang_model.getStokasli());
        Picasso.get().load(barang_model.getFoto()).into(holder.ivBarangGambar);
        holder.tvBtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.btnTvHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context,R.style.dialog)
                        .setTitle("Hapus?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                    databaseReference
                                            .child(context.getResources().getString(R.string.CHILD_BARANG))
                                            .child(context.getResources().getString(R.string.CHILD_BARANG_ALL))
                                            .child(barang_model.getId())
                                            .removeValue();
                                    dialog.cancel();
                                    dialog.dismiss();

                            }
                        }).setNegativeButton("BATAL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.create();
                builder.show();
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
        @BindView(R.id.btn_tv_hapus)
        TextView btnTvHapus;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
