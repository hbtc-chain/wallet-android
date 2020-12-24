package com.bhex.wallet.bh_main.ui.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bhex.lib.uikit.util.ShapeUtils;
import com.bhex.network.mvx.base.BaseBottomSheetDialog;
import com.bhex.tools.utils.PixelUtils;
import com.bhex.wallet.R;

/**
 * 风险提示
 */
public class RistFragment extends BaseBottomSheetDialog {

    private ItemClickListener mItemClickListener;

    @Override
    public void onStart() {
        super.onStart();

        Window window = getDialog().getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //window.getAttributes().windowAnimations = com.bhex.wallet.common.R.style.centerDialogStyle;

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;

        params.width = ViewGroup.LayoutParams.MATCH_PARENT;

        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);

        getDialog().setCanceledOnTouchOutside(false);

    }

    @Nullable
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        GradientDrawable drawable = ShapeUtils.getRoundRectTopDrawable(PixelUtils.dp2px(getContext(),6), Color.parseColor("#FFFFFF"),true,0);
        mRootView.setBackground(drawable);

        mRootView.findViewById(R.id.btn_exit).setOnClickListener(this::itemClick);
        mRootView.findViewById(R.id.btn_goto).setOnClickListener(this::itemClick);
    }

    private void itemClick(View view) {
        if(mItemClickListener==null){
            return;
        }
        dismissAllowingStateLoss();
        if(view.getId()==R.id.btn_exit){
            mItemClickListener.onItemClick(0);
        }else {
            mItemClickListener.onItemClick(1);
        }

    }

    @Override
    public int getLayout() {
        return R.layout.layout_risk;
    }

    public static RistFragment showRistFragment(ItemClickListener listener){
        RistFragment fragment = new RistFragment();
        fragment.mItemClickListener = listener;
        return  fragment;
    }

    public interface  ItemClickListener{
        public void onItemClick(int position);
    }
}
