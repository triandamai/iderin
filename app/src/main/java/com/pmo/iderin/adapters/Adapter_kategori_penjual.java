package com.pmo.iderin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.pmo.iderin.Helpers.Kategori;
import com.pmo.iderin.R;
import com.pmo.iderin.models.kategori_model;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class Adapter_kategori_penjual extends RecyclerView.Adapter<Adapter_kategori_penjual.MyViewHolder> {
    Unbinder unbinder;
    private Context context;
    private List<kategori_model> kategori_models;
    private Kategori kategorilistener;

    public Adapter_kategori_penjual(Context context, List<kategori_model> models,Kategori kategorilistener) {
        this.context = context;
        this.kategori_models = models;
        this.kategorilistener = kategorilistener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_kategori_bottom_sheet, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        kategori_model model = kategori_models.get(position);
        holder.tvNamaKategori.setText(model.getNama());
        holder.tvDeskripsi.setText(model.getDeskripsi());
        Picasso.get()
                .load(model.getFoto())
                .into(holder.ivKategori);
        holder.parentKategori.setOnClickListener(v -> {
            kategorilistener.selectedKategori(model);
        });

    }

    @Override
    public int getItemCount() {
        return kategori_models.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_kategori)
        ImageView ivKategori;
        @BindView(R.id.tv_nama_kategori)
        TextView tvNamaKategori;
        @BindView(R.id.tv_deskripsi)
        TextView tvDeskripsi;
        @BindView(R.id.parent_kategori)
        CardView parentKategori;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);
        }
    }
}
