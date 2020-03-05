package com.iderin.Helpers;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.core.models.kategori_model;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iderin.adapters.Adapter_kategori_penjual;
import com.pmo.iderin.R;
import com.todkars.shimmer.ShimmerRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BottomSheetTakeKategori extends BottomSheetDialogFragment {

    Unbinder unbinder;
    @BindView(R.id.shimmer_recycler_kategori)
    ShimmerRecyclerView shimmerRecyclerKategori;
    @BindView(R.id.ly_bottom_sheet)
    LinearLayout lyBottomSheet;

    private Kategori listener;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private Kategori mBottomSheetListener;
    private List<kategori_model> datalist = new ArrayList<>();
    private Adapter_kategori_penjual adapter;

    public void setOnkategoriSelected(Kategori listene){
        listener = listene;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottomsheet_kategori, container, false);
        unbinder = ButterKnife.bind(this, view);
            shimmerRecyclerKategori.setVisibility(View.VISIBLE);
            shimmerRecyclerKategori.showShimmer();
         getData();
        return view;
    }

    private void getData() {
        databaseReference
                .child(getResources().getString(R.string.CHILD_BARANG))
                .child(getResources().getString(R.string.CHILD_BARANG_KATEGORI))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            kategori_model model = new kategori_model();
                            datalist.clear();
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                model = data.getValue(kategori_model.class);
                                model.setId(data.getKey());
                                assert model != null;
                                datalist.add(model);
                            }
                            adapter = new Adapter_kategori_penjual(getContext(), datalist, new Kategori() {
                                @Override
                                public void selectedKategori(kategori_model kategori) {
                                    listener.selectedKategori(kategori);
                                }

                                @Override
                                public void addkategori(kategori_model kategori) {

                                }
                            });
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                            shimmerRecyclerKategori.setLayoutManager(layoutManager);
                            shimmerRecyclerKategori.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(dialogInterface -> {
            BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
            setupFullHeight(bottomSheetDialog);
        });
        return dialog;

    }

    private void setupFullHeight(BottomSheetDialog bottomSheetDialog) {
        FrameLayout bottomSheet = (FrameLayout) bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        //LinearLayout bottomSheet = (LinearLayout) bottomSheetDialog.findViewById(R.id.ly_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();

        int windowHeight = getWindowHeight();
        if (layoutParams != null) {
            layoutParams.height = windowHeight;
        }
        bottomSheet.setLayoutParams(layoutParams);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels /2;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }


}
