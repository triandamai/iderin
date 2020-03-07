package com.kategori;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.core.models.kategori_model;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AdapterKategori extends RecyclerView.Adapter<AdapterKategori.MyViewHolder> {

    private Unbinder unbinder;
    private Context context;
    private List<kategori_model> list = new ArrayList<>();

    public AdapterKategori(Context context, List<kategori_model> list) {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_kategori, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final kategori_model mode = list.get(position);
        holder.tvNamaKategori.setText(mode.getNama());
        Picasso.get().load(mode.getFoto().toString())
                .into(holder.ivKategori);
        holder.cvParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent kebarang = new Intent();
                kebarang.setClassName(context, "com.barang.AllBarang");
                kebarang.putExtra(context.getString(R.string.INTENT_PUT_IDKATEGORI), mode.getId().toString());
                context.startActivity(kebarang);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_kategori)
        ImageView ivKategori;
        @BindView(R.id.tv_nama_kategori)
        TextView tvNamaKategori;
        @BindView(R.id.cv_parent)
        CardView cvParent;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);
        }
    }
}
