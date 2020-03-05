package com.iderin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.core.models.satuan_model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.iderin.Helpers.Satuan;
import com.pmo.iderin.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class Adapter_satuan_penjual extends RecyclerView.Adapter<Adapter_satuan_penjual.MyViewHolder> {


    private Unbinder unbinder;
    private Context context;
    private List<satuan_model> satuan_models;
    private Satuan listener;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private StorageReference storageReference = firebaseStorage.getReference();

    public Adapter_satuan_penjual(Context context, List<satuan_model> models, Satuan listener) {
        this.context = context;
        this.satuan_models = models;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_satuan_bottom_sheet, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        satuan_model model = satuan_models.get(position);
        holder.tvNamaKategori.setText(model.getNama());
        holder.tvDeskripsi.setText(model.getDeskripsi());


        holder.parentKategori.setOnClickListener(v -> {
            listener.selectedSatuan(model);
        });

    }

    @Override
    public int getItemCount() {
        return satuan_models.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
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
