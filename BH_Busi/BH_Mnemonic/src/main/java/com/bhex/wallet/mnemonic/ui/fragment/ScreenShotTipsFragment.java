package com.bhex.wallet.mnemonic.ui.fragment;


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
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.FragmentManager;

import com.bhex.tools.utils.ColorUtil;
import com.bhex.tools.utils.PixelUtils;
import com.bhex.lib.uikit.util.ShapeUtils;
import com.bhex.network.mvx.base.BaseDialogFragment;
import com.bhex.wallet.mnemonic.R;
import com.bhex.wallet.mnemonic.R2;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author gdy
 * 2020-3-10
 * 请勿截屏提示
 */
public class ScreenShotTipsFragment extends BaseDialogFragment {

    @BindView(R2.id.btn_close)
    AppCompatImageView btn_close;
    @BindView(R2.id.btn_i_know)
    AppCompatTextView btn_i_know;

    private IKnowListener mIKnowListener;

    @Override
    public int getLayout() {
        return R.layout.fragment_screen_shot_tips;
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

        params.width = ViewGroup.LayoutParams.MATCH_PARENT;

        params.width = dm.widthPixels;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//        params.height = PixelUtils.dp2px(BaseApplication.getInstance(), 248);

        window.setAttributes(params);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        GradientDrawable drawable = ShapeUtils.getRoundRectTopDrawable(PixelUtils.dp2px(getContext(),6), ColorUtil.getColor(getContext(),R.color.app_bg),true,0);
        mRootView.setBackground(drawable);
    }


    public static ScreenShotTipsFragment showDialog(FragmentManager fm, String tag) {
        ScreenShotTipsFragment fragment = new ScreenShotTipsFragment();
        fragment.show(fm, tag);
        return fragment;
    }

    public void setIKnowListener(IKnowListener mIKnowListener) {
        this.mIKnowListener = mIKnowListener;
    }

    @OnClick({R2.id.btn_close, R2.id.btn_i_know})
    public void onViewClicked(View view) {
        if(view.getId()==R.id.btn_i_know){
            if (mIKnowListener != null) {
                mIKnowListener.clickBtn();
            }
            dismiss();
        }

        if(view.getId()==R.id.btn_close){
            dismiss();
        }

    }

    /**
     * 点击i_know
     */
    public interface IKnowListener {
        public void clickBtn();
    }
}
