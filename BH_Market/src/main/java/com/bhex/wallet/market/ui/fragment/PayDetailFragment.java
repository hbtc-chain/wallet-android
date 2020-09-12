package com.bhex.wallet.market.ui.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.bhex.lib.uikit.util.ColorUtil;
import com.bhex.lib.uikit.util.PixelUtils;
import com.bhex.network.mvx.base.BaseDialogFragment;
import com.bhex.tools.utils.ShapeUtils;
import com.bhex.wallet.market.R;

/**
 * @author gongdongyang
 * 支付详情页面
 * 2020-9-3 16:01:51
 */
public class PayDetailFragment extends BaseDialogFragment {

    @Override
    public int getLayout() {
        return R.layout.fragment_pay_detail;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.getAttributes().windowAnimations = R.style.bottomDialogStyle;

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = dm.widthPixels;
        window.setAttributes(params);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        GradientDrawable drawable = ShapeUtils.getRoundRectTopDrawable(PixelUtils.dp2px(getContext(), 6), ColorUtil.getColor(getContext(), R.color.app_bg), true, 0);
        mRootView.setBackground(drawable);
    }

    public static void showDialog(FragmentManager fm, String tag){
        PayDetailFragment fragment = new PayDetailFragment();
        fragment.show(fm,tag);
    }
}