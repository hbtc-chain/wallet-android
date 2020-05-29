package com.bhex.wallet.mnemonic.ui.fragment;


import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.FragmentManager;

import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.bhex.lib.uikit.util.PixelUtils;
import com.bhex.network.app.BaseApplication;
import com.bhex.network.mvx.base.BaseDialogFragment;
import com.bhex.tools.utils.NavigateUtil;
import com.bhex.wallet.mnemonic.R;
import com.bhex.wallet.mnemonic.ui.activity.BackupMnemonicActivity;

/**
 * @author gongdongyang
 * 2020-3-13 15:14:52
 * 安全提醒
 */
public class SecureTipsFragment extends BaseDialogFragment implements View.OnClickListener {

    private AppCompatTextView btn_at_once;

    private AppCompatTextView btn_later;

    public SecureTipsFragment() {
        // Required empty public constructor
    }


    @Override
    public int getLayout() {
        return R.layout.fragment_secure_tips;
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
        //params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.width = dm.widthPixels;
        //params.height = PixelUtils.dp2px(BaseApplication.getInstance(), 328);
        window.setAttributes(params);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        btn_at_once = mRootView.findViewById(R.id.btn_at_once);
        btn_later = mRootView.findViewById(R.id.btn_later);
        btn_at_once.setOnClickListener(this);
        btn_later.setOnClickListener(this);
        mRootView.findViewById(R.id.iv_close).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn_at_once){
            NavigateUtil.startActivity(getActivity(), BackupMnemonicActivity.class);
        }
        dismiss();
    }

    public static void showDialog(FragmentManager fm, String tag){
        SecureTipsFragment fragment = new SecureTipsFragment();
        fragment.show(fm,tag);

    }
}
