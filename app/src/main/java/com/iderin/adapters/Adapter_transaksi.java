package com.iderin.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.core.models.transaksi_model;

import java.util.ArrayList;
import java.util.List;

public class Adapter_transaksi extends RecyclerView.Adapter<Adapter_transaksi.MyViewHolder> {

    private List<transaksi_model> list = new ArrayList<>();
    private Context context;

    public Adapter_transaksi(Context context, List<transaksi_model> models) {
        this.context = context;
        this.list = models;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
