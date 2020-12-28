package com.bhex.wallet.mnemonic.ui.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.bhex.network.app.BaseApplication;
import com.bhex.network.mvx.base.BaseDialogFragment;
import com.bhex.tools.utils.PixelUtils;
import com.bhex.wallet.mnemonic.R;
import com.google.android.material.button.MaterialButton;

/**
 * @author gongdongyang
 * 2020-12-5 21:58:18
 */
public class DeleteTipFragment extends BaseDialogFragment {

    private FragmentItemListener mItemListener;
    private int mPosition;

    @Override
    public int getLayout() {
        return R.layout.fragment_delete_tip;
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

        params.width = dm.widthPixels- PixelUtils.dp2px(BaseApplication.getInstance(),32);
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;

        window.setAttributes(params);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        MaterialButton btn_cancel = mRootView.findViewById(R.id.btn_cancel);
        MaterialButton btn_confirm = mRootView.findViewById(R.id.btn_confirm);
        btn_cancel.setOnClickListener(v -> {
            dismiss();
            if(mItemListener==null){
                return;
            }
        });

        btn_confirm.setOnClickListener(v -> {
            dismiss();
            if(mItemListener==null){
                return;
            }
            mItemListener.clickItemAction(1,mPosition);
        });
    }

    public static DeleteTipFragment showFragment(int position,FragmentItemListener listener){
        DeleteTipFragment fragment = new DeleteTipFragment();
        fragment.mPosition = position;
        fragment.mItemListener = listener;
        return fragment;
    }

    public interface FragmentItemListener{
        public void clickItemAction(int index,int postion);
    }
}