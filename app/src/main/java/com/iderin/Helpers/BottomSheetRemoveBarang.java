package com.iderin.Helpers;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.pmo.iderin.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class BottomSheetRemoveBarang extends BottomSheetDialogFragment {

    Unbinder unbinder;
    @BindView(R.id.btn_pick_kamera)
    Button btnPickKamera;
    @BindView(R.id.btn_pick_gallery)
    Button btnPickGallery;
    @BindView(R.id.ly_btn_bottomsheet)
    LinearLayout lyBtnBottomsheet;
    private BottomSheetListener mBottomSheetListener;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_pickimage, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mBottomSheetListener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString());
        }
    }



    @OnClick({R.id.btn_pick_kamera, R.id.btn_pick_gallery})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_pick_kamera:
                mBottomSheetListener.onOptionClick("kamera");
                dismiss();
                break;
            case R.id.btn_pick_gallery:
                mBottomSheetListener.onOptionClick("gallery");
                dismiss();
                break;
        }
    }

    public interface BottomSheetListener {
        void onOptionClick(String text);
    }
}
