package com.iderin.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.core.models.toko_model;
import com.pmo.iderin.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AdapterToko extends RecyclerView.Adapter<AdapterToko.MyViewHolder> {

    private Context context;
    private List<toko_model> list = new ArrayList<>();
    private Unbinder unbinder;

    public AdapterToko(Context context, List<toko_model> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_toko, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        toko_model model = list.get(position);
        holder.tvJudulToko.setText(model.getNamatoko());
        holder.tvAlamatLengkap.setText(model.getAlamat());
        Picasso.get().load(model.getBanner_toko());
        Log.e("IDERIN", "ada kok");

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_judul_toko)
        TextView tvJudulToko;
        @BindView(R.id.tv_alamat_lengkap)
        TextView tvAlamatLengkap;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);
        }
    }
}
