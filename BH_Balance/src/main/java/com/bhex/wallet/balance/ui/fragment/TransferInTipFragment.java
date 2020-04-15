package com.bhex.wallet.balance.ui.fragment;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.bhex.lib.uikit.util.PixelUtils;
import com.bhex.network.app.BaseApplication;
import com.bhex.network.mvx.base.BaseDialogFragment;
import com.bhex.wallet.balance.R;

/**
 * @author gongdongyang
 * 2020-4-14 17:30:22
 * 收款提示
 */
public class TransferInTipFragment extends BaseDialogFragment {

    int way;
    @Override
    public int getLayout() {
        return R.layout.fragment_transfer_in_tip;
    }

    @Override
    public void onStart() {
        super.onStart();

        Window window = getDialog().getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.getAttributes().windowAnimations = R.style.centerDialogStyle;

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;

        params.width = ViewGroup.LayoutParams.MATCH_PARENT;

        params.width = dm.widthPixels - PixelUtils.dp2px(BaseApplication.getInstance(), 48);
        params.height = PixelUtils.dp2px(BaseApplication.getInstance(), 220);
        window.setAttributes(params);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRootView.findViewById(R.id.btn_ikonw).setOnClickListener(v->{
            dismiss();
        });

        AppCompatTextView tv_tip1 = mRootView.findViewById(R.id.tv_transfer_in_tips_1);
        AppCompatTextView tv_tip2 = mRootView.findViewById(R.id.tv_transfer_in_tips_2);

        if(way==1){
            tv_tip1.setText(getResources().getString(R.string.linkinner_deposit_tip_1));
            tv_tip2.setText(getResources().getString(R.string.linkinner_deposit_tip_2));
        }else if(way==2){
            tv_tip1.setText(getResources().getString(R.string.crosslink_deposit_tip_1));
            tv_tip2.setText(getResources().getString(R.string.crosslink_deposit_tip_2));
        }
    }


    /**
     *
     * @param fm
     * @param tag
     */
    public static void showDialog(FragmentManager fm, String tag,int way){
        TransferInTipFragment fragment = new TransferInTipFragment();
        fragment.way = way;
        fragment.show(fm,tag);
    }
}
