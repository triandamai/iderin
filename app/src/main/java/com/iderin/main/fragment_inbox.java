package com.iderin.main;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.pmo.iderin.R;
import com.todkars.shimmer.ShimmerRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;


public class fragment_inbox extends Fragment {

    @BindView(R.id.toolbar_alamat)
    Toolbar toolbarAlamat;
    @BindView(R.id.shimmer_recycler_inbox)
    ShimmerRecyclerView shimmerRecyclerInbox;
    @BindView(R.id.ly_kosong)
    LinearLayout lyKosong;

    public fragment_inbox() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static fragment_inbox newInstance(String param1, String param2) {
        fragment_inbox fragment = new fragment_inbox();
        Bundle args = new Bundle();

        fragment.setArguments(args);
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
        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_inbox, container, false);
        ButterKnife.bind(this,v);
        shimmerRecyclerInbox.showShimmer();
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
