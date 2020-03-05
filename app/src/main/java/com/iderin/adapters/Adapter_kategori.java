package com.iderin.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.core.models.kategori_model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pmo.iderin.BuildConfig;
import com.pmo.iderin.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Adapter_kategori extends RecyclerView.Adapter<Adapter_kategori.MyViewHolder> {



    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    private Context context;
    private List<kategori_model> list;

    public Adapter_kategori(Context context, List<kategori_model> kategori) {
        this.context = context;
        this.list = kategori;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_kategori, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        kategori_model kategori_model = list.get(position);
        holder.tvNama.setText(kategori_model.getNama());
        Picasso.get().load(kategori_model.getFoto()).into(holder.ivFotokategori);
        holder.lyKategori.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClassName(BuildConfig.APPLICATION_ID, "com.barang.All");
            intent.putExtra(context.getString(R.string.INTENT_PUT_IDKATEGORI), kategori_model.getId());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ly_kategori)
        LinearLayout lyKategori;
        @BindView(R.id.iv_fotokategori)
        ImageView ivFotokategori;
        @BindView(R.id.tv_nama)
        TextView tvNama;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
