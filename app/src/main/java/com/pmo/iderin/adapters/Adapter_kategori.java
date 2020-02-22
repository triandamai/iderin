package com.pmo.iderin.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pmo.iderin.Helpers.Kategori;
import com.pmo.iderin.models.kategori_model;

import java.util.List;

public class Adapter_kategori extends RecyclerView.Adapter<Adapter_kategori.MyViewHolder> {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    private Kategori listener;
    private Context context;
    private List<kategori_model> list;

    public Adapter_kategori(Context context, List<kategori_model> kategori, Kategori kategorilistener){
        this.context = context;
        this.list = kategori;
        this.listener = kategorilistener;
    }

    @NonNull
    @Override
    public Adapter_kategori.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_kategori.MyViewHolder holder, int position) {
        kategori_model kategori_model = list.get(position);

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
