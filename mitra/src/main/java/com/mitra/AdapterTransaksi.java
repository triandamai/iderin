package com.mitra;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.core.models.transaksi_model;

import java.util.ArrayList;
import java.util.List;

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

    }

    @Override
    public int getItemCount() {
        return transaksi_models.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
