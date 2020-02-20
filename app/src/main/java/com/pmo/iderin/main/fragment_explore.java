package com.pmo.iderin.main;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.pmo.iderin.R;
import com.todkars.shimmer.ShimmerRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;


public class fragment_explore extends Fragment {
    @BindView(R.id.shimmer_recycler_kategori)
    ShimmerRecyclerView shimmerRecyclerKategori;
    @BindView(R.id.shimmer_recycler_terlaris)
    ShimmerRecyclerView shimmerRecyclerTerlaris;
    @BindView(R.id.shimmer_recycler_terdekat)
    ShimmerRecyclerView shimmerRecyclerTerdekat;
    // TODO: Rename parameter arguments, choose names that match


    public static fragment_explore newInstance(String param1, String param2) {
        fragment_explore fragment = new fragment_explore();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_explore, container, false);
        ButterKnife.bind(this,v);
        shimmerRecyclerKategori.showShimmer();
        shimmerRecyclerTerdekat.showShimmer();
        shimmerRecyclerTerlaris.showShimmer();
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


}
