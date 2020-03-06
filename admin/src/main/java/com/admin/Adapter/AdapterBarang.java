package com.admin.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.admin.R;
import com.core.models.barang_model;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterBarang extends RecyclerView.Adapter<AdapterBarang.MyViewHolder> {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    private List<barang_model> list;
    private Context context;
    private String namaKategori = "";

    public AdapterBarang(Context context, List<barang_model> data) {
        this.context = context;
        this.list = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_barang_admin, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        barang_model barang_model = list.get(position);
        holder.tvNamaBarang.setText(barang_model.getNama());
        holder.tvHargaQty.setText("Rp."+barang_model.getHarga());
        Picasso.get().load(barang_model.getFoto()).into(holder.ivBarangGambar);
//        databaseReference
//                .child(context.getResources().getString(R.string.CHILD_BARANG))
//                .child(context.getResources().getString(R.string.CHILD_BARANG_KATEGORI))
//                .child(barang_model.getIdkategori().toString())
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        if (dataSnapshot.exists()){
//                            barang_model model = new barang_model();
//                            model = dataSnapshot.getValue(barang_model.class);
//                            assert model != null;
//                            namaKategori = model.getNama();
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
        holder.tvBtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addbarang = new Intent();
                addbarang.setClassName(context, "com.barang.Addbarang")
                        .putExtra("gambar",barang_model.getFoto())
                        .putExtra("idkategori",barang_model.getIdkategori())
                        .putExtra("kategori",namaKategori)
                        .putExtra("nama",barang_model.getNama())
                        .putExtra("deskripsi",barang_model.getDeskripsi())
                        .putExtra("harga",barang_model.getHarga())
                        .putExtra("id", barang_model.getId());
                context.startActivity(addbarang);
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
