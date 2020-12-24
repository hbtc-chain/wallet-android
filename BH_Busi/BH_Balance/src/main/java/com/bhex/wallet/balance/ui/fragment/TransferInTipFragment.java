package com.bhex.wallet.balance.ui.fragment;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.bhex.tools.utils.ColorUtil;
import com.bhex.tools.utils.PixelUtils;
import com.bhex.lib.uikit.util.ShapeUtils;
import com.bhex.network.app.BaseApplication;
import com.bhex.network.mvx.base.BaseDialogFragment;
import com.bhex.wallet.balance.R;
import com.bhex.wallet.common.enums.BH_BUSI_TYPE;

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
    public void initStart() {
        setStyle(DialogFragment.STYLE_NO_TITLE, STYLE_NO_TITLE);
        Window window = getDialog().getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.getAttributes().windowAnimations = R.style.centerDialogStyle;

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        params.width = dm.widthPixels - PixelUtils.dp2px(BaseApplication.getInstance(), 48);
        window.setAttributes(params);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        mRootView.findViewById(R.id.btn_ikonw).setOnClickListener(v->{
            dismiss();
        });

        GradientDrawable drawable = ShapeUtils.getRoundRectDrawable(16, ColorUtil.getColor(getContext(), R.color.white));
        mRootView.setBackground(drawable);

        AppCompatTextView tv_tip1 = mRootView.findViewById(R.id.tv_transfer_in_tips_1);
        AppCompatTextView tv_tip2 = mRootView.findViewById(R.id.tv_transfer_in_tips_2);
        ForegroundColorSpan foregroundColorSpan=new ForegroundColorSpan(ContextCompat.getColor(getContext(),R.color.alarm_highlight_text_color));

        if(way== BH_BUSI_TYPE.链内转账.getIntValue()){
            String tip1 = getResources().getString(R.string.linkinner_deposit_tip_1);
            String linkInner = getResources().getString(R.string.linkinner);
            int index1 = tip1.indexOf(linkInner);
            if(index1>=0){
                SpannableString spannableStr1 = new SpannableString(tip1);
                spannableStr1.setSpan(foregroundColorSpan,index1,index1+linkInner.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv_tip1.setText(spannableStr1);
            }

            String tip2 = getResources().getString(R.string.linkinner_deposit_tip_2);
            String noCrossLink = getResources().getString(R.string.no_cross_link);
            int index2 = tip2.indexOf(noCrossLink);
            if(index2>=0){
                SpannableString spannableStr2 = new SpannableString(tip2);
                spannableStr2.setSpan(foregroundColorSpan,index2,index2+noCrossLink.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv_tip2.setText(spannableStr2);
            }

        }else if(way==BH_BUSI_TYPE.跨链转账.getIntValue()){
            String tip1 = getResources().getString(R.string.crosslink_deposit_tip_1);
            String crosslink = getResources().getString(R.string.crosslink);
            int index1 = tip1.indexOf(crosslink);
            if(index1>=0){
                SpannableString spannableStr1 = new SpannableString(tip1);
                spannableStr1.setSpan(foregroundColorSpan,index1,index1+crosslink.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv_tip1.setText(spannableStr1);
            }

            String tip2 = getResources().getString(R.string.crosslink_deposit_tip_2);
            String nativelink = getResources().getString(R.string.nativelink);
            int index2 = tip2.indexOf(nativelink);
            if(index2>=0){
                SpannableString spannableStr2 = new SpannableString(tip2);
                spannableStr2.setSpan(foregroundColorSpan,index2,index2+nativelink.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv_tip2.setText(spannableStr2);
            }
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
