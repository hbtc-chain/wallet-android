package com.bhex.wallet.mnemonic.ui.fragment;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
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
import android.widget.CheckedTextView;

import com.bhex.tools.utils.ColorUtil;
import com.bhex.tools.utils.PixelUtils;
import com.bhex.lib.uikit.util.ShapeUtils;
import com.bhex.network.app.BaseApplication;
import com.bhex.network.mvx.base.BaseBottomSheetDialog;
import com.bhex.tools.language.LocalManageUtil;
import com.bhex.tools.utils.FileUtils;
import com.bhex.wallet.mnemonic.R;

import java.util.Locale;

/**
 *
 */
public class GlobalTipsFragment extends BaseBottomSheetDialog {

    private CheckedTextView check_agreement;
    private AppCompatTextView tv_agreement;

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

        //params.width = dm.widthPixels;
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
        GradientDrawable drawable = ShapeUtils.getRoundRectTopDrawable(PixelUtils.dp2px(getContext(),6), ColorUtil.getColor(getContext(),R.color.app_bg),true,0);
        mRootView.setBackground(drawable);
        check_agreement = mRootView.findViewById(R.id.check_agreement);
        tv_agreement = mRootView.findViewById(R.id.tv_agreement);
        check_agreement.setChecked(isCheck);
        Locale locale = LocalManageUtil.getSetLanguageLocale(getActivity());
        if(locale!=null && locale.getLanguage().contains("zh")){
            String agreement = FileUtils.loadStringByAssets(BaseApplication.getInstance(),"zh.txt").replace("\\n", "\n");
            tv_agreement.setText(agreement);
        }else{
            String agreement = FileUtils.loadStringByAssets(BaseApplication.getInstance(),"en.txt").replace("\\n", "\n");
            tv_agreement.setText(agreement);
        }
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
                //NavigateUtil.startActivity(getActivity(), TrusteeshipActivity.class);
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
