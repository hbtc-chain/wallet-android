package com.bhex.wallet.balance.ui.fragment;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
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
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        mRootView.findViewById(R.id.btn_ikonw).setOnClickListener(v->{
            dismiss();
        });

        AppCompatTextView tv_tip1 = mRootView.findViewById(R.id.tv_transfer_in_tips_1);
        AppCompatTextView tv_tip2 = mRootView.findViewById(R.id.tv_transfer_in_tips_2);
        ForegroundColorSpan foregroundColorSpan=new ForegroundColorSpan(ContextCompat.getColor(getContext(),R.color.red));

        if(way==1){
            String tip1 = getResources().getString(R.string.linkinner_deposit_tip_1);
            int index1 = tip1.indexOf("链内");
            SpannableString spannableStr1 = new SpannableString(tip1);
            spannableStr1.setSpan(foregroundColorSpan,index1,index1+"链内".length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv_tip1.setText(spannableStr1);

            String tip2 = getResources().getString(R.string.linkinner_deposit_tip_2);
            int index2 = tip2.indexOf("非跨链");
            SpannableString spannableStr2 = new SpannableString(tip2);
            spannableStr2.setSpan(foregroundColorSpan,index2,index2+"非跨链".length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv_tip2.setText(spannableStr2);

        }else if(way==2){
            String tip1 = getResources().getString(R.string.crosslink_deposit_tip_1);
            int index1 = tip1.indexOf("跨链");
            SpannableString spannableStr1 = new SpannableString(tip1);
            spannableStr1.setSpan(foregroundColorSpan,index1,index1+"跨链".length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv_tip1.setText(spannableStr1);

            String tip2 = getResources().getString(R.string.crosslink_deposit_tip_2);
            int index2 = tip2.indexOf("OMNI钱包");
            SpannableString spannableStr2 = new SpannableString(tip2);
            spannableStr2.setSpan(foregroundColorSpan,index2,index2+"OMNI钱包".length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv_tip2.setText(spannableStr2);
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
