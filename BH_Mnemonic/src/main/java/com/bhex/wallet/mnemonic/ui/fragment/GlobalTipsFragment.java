package com.bhex.wallet.mnemonic.ui.fragment;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CheckedTextView;

import com.bhex.lib.uikit.util.PixelUtils;
import com.bhex.network.app.BaseApplication;
import com.bhex.network.mvx.base.BaseDialogFragment;
import com.bhex.wallet.mnemonic.R;
import com.bhex.wallet.mnemonic.ui.activity.TrusteeshipActivity;

/**
 *
 */
public class GlobalTipsFragment extends BaseDialogFragment {

    private CheckedTextView check_agreement;

    public GlobalTipsFragment() {
        // Required empty public constructor
    }

    GlobalOnClickListenter globalOnClickListenter;

    private boolean isCheck;

    @Override
    public int getLayout() {
        return R.layout.fragment_global_tips;
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
        params.height = dm.heightPixels- PixelUtils.dp2px(BaseApplication.getInstance(),60);

        window.setAttributes(params);

    }

    public static void showDialog(FragmentManager fm, String tag,GlobalOnClickListenter globalOnClickListenter,Boolean isCheck){
        GlobalTipsFragment fragment = new GlobalTipsFragment();
        fragment.setGlobalOnClickListenter(globalOnClickListenter);
        fragment.isCheck = isCheck;
        fragment.show(fm,tag);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        check_agreement = mRootView.findViewById(R.id.check_agreement);
        check_agreement.setChecked(isCheck);
        addEvent();
    }

    /**
     * 添加事件
     */
    private void addEvent() {
        check_agreement.setOnClickListener(v -> {
            //ToastUtils.showToast("ischecked:"+check_agreement.isChecked());
            check_agreement.setChecked(!check_agreement.isChecked());
        });

        mRootView.findViewById(R.id.btn_confirm).setOnClickListener(v -> {
            /*if(check_agreement.isChecked()){
                //NavitateUtil.startActivity(getActivity(), TrusteeshipActivity.class);


            }*/
            dismiss();
            if(globalOnClickListenter!=null){
                globalOnClickListenter.onCheckClickListener(null,check_agreement.isChecked());
            }
        });
    }


    public GlobalOnClickListenter getGlobalOnClickListenter() {
        return globalOnClickListenter;
    }

    public void setGlobalOnClickListenter(GlobalOnClickListenter globalOnClickListenter) {
        this.globalOnClickListenter = globalOnClickListenter;
    }

    public interface GlobalOnClickListenter{
        public void onCheckClickListener(View view,boolean isCheck);
    }

}
