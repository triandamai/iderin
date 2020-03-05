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
import com.pmo.iderin.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class Adapter_barang extends RecyclerView.Adapter<Adapter_barang.MyViewHolder> {

    private List<barang_model> list;
    private Context context;
    private Unbinder unbinder;

    public Adapter_barang(Context context, List<barang_model> data) {
        this.context = context;
        this.list = data;
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
        holder.tvBarangNama.setText(model.getNama());
        Picasso.get().load(model.getFoto().toString()).into(holder.ivBarangGambar);

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


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this,itemView);
        }
    }
}
