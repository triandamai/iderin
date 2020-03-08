package com.mitra;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.core.models.transaksi_model;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterTransaksi extends RecyclerView.Adapter<AdapterTransaksi.MyViewHolder> {

    private Context context;
    private List<transaksi_model> transaksi_models = new ArrayList<>();

    public AdapterTransaksi(Context context, List<transaksi_model> data) {
        this.context = context;
        this.transaksi_models = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_transaksi, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        transaksi_model transaksiModel = transaksi_models.get(position);
        holder.tvTransaksi.setText("KOde:" + transaksiModel.getMetode_pembayaran());

        holder.lyParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, DetailTransaksiMitra.class).putExtra("idtransaksi", transaksiModel.getIdtransaksi()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return transaksi_models.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_transaksi)
        TextView tvTransaksi;
        @BindView(R.id.tv_penjual)
        TextView tvPenjual;
        @BindView(R.id.tv_metode)
        TextView tvMetode;
        @BindView(R.id.ly_parent)
        LinearLayout lyParent;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
