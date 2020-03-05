package com.toko;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.core.models.toko_model;

import java.util.ArrayList;
import java.util.List;

public class AdapterToko extends RecyclerView.Adapter<AdapterToko.MyViewHolder> {
    private Context context;
    private List<toko_model> list = new ArrayList<>();

    public AdapterToko(Context context, List<toko_model> list) {
        this.context = context;
        this.list = list;
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
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
