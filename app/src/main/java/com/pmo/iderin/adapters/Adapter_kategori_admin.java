package com.pmo.iderin.adapters;

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

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pmo.iderin.Admin.FormKategori;
import com.pmo.iderin.R;
import com.pmo.iderin.models.kategori_model;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Adapter_kategori_admin extends RecyclerView.Adapter<Adapter_kategori_admin.MyViewHolder> {


    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    private Context context;
    private List<kategori_model> list;

    public Adapter_kategori_admin(Context context, List<kategori_model> kategori) {
        this.context = context;
        this.list = kategori;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_kategori_admin, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        kategori_model kategori_model = list.get(position);
        holder.tvNamaKategori.setText(kategori_model.getNama());
        Picasso.get().load(kategori_model.getFoto()).into(holder.ivFotokategori);

        holder.tvBtnEdir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, FormKategori.class)
                .putExtra("id",kategori_model.getId())
                .putExtra("nama",kategori_model.getNama())
                .putExtra("foto",kategori_model.getFoto()));

            }
        });
        holder.tvBtnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context,R.style.dialog)
                        .setTitle("Hapus "+kategori_model.getNama()+"?")
                        .setMessage("Lanjutkan menghapus?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                databaseReference
                                        .child(context.getResources().getString(R.string.CHILD_BARANG))
                                        .child(context.getResources().getString(R.string.CHILD_BARANG_KATEGORI))
                                        .child(kategori_model.getId())
                                        .removeValue();
                                dialog.dismiss();
                                dialog.cancel();
                                notifyDataSetChanged();
                            }
                        }).setNegativeButton("BATAL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                             dialog.dismiss();
                             dialog.cancel();
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
        @BindView(R.id.iv_fotokategori)
        ImageView ivFotokategori;
        @BindView(R.id.tv_nama_kategori)
        TextView tvNamaKategori;
        @BindView(R.id.tv_btn_edit)
        TextView tvBtnEdir;
        @BindView(R.id.tv_btn_hapus)
        TextView tvBtnHapus;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
