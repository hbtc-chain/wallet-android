package com.bhex.wallet.mnemonic.ui.fragment;


import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.FragmentManager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bhex.lib.uikit.util.ShapeUtils;
import com.bhex.network.mvx.base.BaseBottomSheetDialog;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.utils.ColorUtil;
import com.bhex.tools.utils.PixelUtils;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.ui.fragment.PasswordFragment;
import com.bhex.wallet.mnemonic.R;

/**
 * @author gongdongyang
 * 2020-3-13 15:14:52
 * 安全提醒
 */
public class SecureTipsFragment extends BaseBottomSheetDialog implements View.OnClickListener,PasswordFragment.PasswordClickListener {

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

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = dm.widthPixels;
        getDialog().getWindow().setAttributes(params);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        GradientDrawable drawable = ShapeUtils.getRoundRectTopDrawable(PixelUtils.dp2px(getContext(),6), ColorUtil.getColor(getContext(),R.color.app_bg),true,0);
        mRootView.setBackground(drawable);

        btn_at_once = mRootView.findViewById(R.id.btn_at_once);
        btn_later = mRootView.findViewById(R.id.btn_later);
        btn_at_once.setOnClickListener(this);
        btn_later.setOnClickListener(this);
        mRootView.findViewById(R.id.iv_close).setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn_at_once){
            //NavigateUtil.startActivity(getActivity(), BackupMnemonicActivity.class);
            PasswordFragment.showPasswordDialog(getChildFragmentManager(),
                    PasswordFragment.class.getName(),
                    SecureTipsFragment.this,0);
            return;
        }
        dismiss();
    }

    public static void showDialog(FragmentManager fm, String tag){
        SecureTipsFragment fragment = new SecureTipsFragment();
        fragment.show(fm,tag);

    }

    @Override
    public void confirmAction(String password, int position,int way) {
        ARouter.getInstance().build(ARouterConfig.MNEMONIC_BACKUP)
                .withString(BHConstants.INPUT_PASSWORD,password)
                .navigation();
        dismiss();
    }

}
