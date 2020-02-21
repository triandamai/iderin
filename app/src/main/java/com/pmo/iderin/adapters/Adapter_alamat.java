package com.pmo.iderin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pmo.iderin.R;
import com.pmo.iderin.models.alamat_model;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Adapter_alamat extends RecyclerView.Adapter<Adapter_alamat.MyViewHolder> {

    List<alamat_model> alamat_modelList = new ArrayList<>();
    Context context;


    public Adapter_alamat(Context context, List<alamat_model> data) {
        this.context = context;
        this.alamat_modelList = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_alamat, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvJudulAlamat.setText(alamat_modelList.get(position).getNamaalamat());
        holder.tvAlamatLengkap.setText(alamat_modelList.get(position).getAlamatlengkap());
    }

    @Override
    public int getItemCount() {
        return alamat_modelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_judul_alamat)
        TextView tvJudulAlamat;
        @BindView(R.id.tv_alamat_lengkap)
        TextView tvAlamatLengkap;
        @BindView(R.id.btn_ubah)
        Button btnUbah;
        @BindView(R.id.btn_hapus)
        Button btnHapus;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
