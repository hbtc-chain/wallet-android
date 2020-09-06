package com.bhex.wallet.market.ui.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.FragmentManager;

import com.bhex.network.mvx.base.BaseDialogFragment;
import com.bhex.wallet.common.manager.MMKVManager;
import com.bhex.wallet.market.R;
import com.bhex.wallet.market.R2;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author gongdongyang
 * 2020-9-5 19:53:21
 */
public class DexAuthorizeFragment extends BaseDialogFragment {

    @BindView(R2.id.btn_reject)
    AppCompatTextView btn_reject;

    @BindView(R2.id.btn_agree)
    AppCompatTextView btn_agree;

    private DexAuthorizeListener mDexAuthorizeListener;

    @Override
    public int getLayout() {
        return R.layout.fragment_dex_authorize;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
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
        window.setAttributes(params);
    }

    public static void showDialog(FragmentManager fm, String tag, DexAuthorizeListener listener) {
        DexAuthorizeFragment fragment = new DexAuthorizeFragment();
        fragment.mDexAuthorizeListener = listener;
        fragment.show(fm, tag);
    }

    @OnClick({R2.id.btn_reject, R2.id.btn_agree})
    public void onViewClicked(View view) {
        if(R.id.btn_reject==view.getId()){
            dismiss();
            return;
        }
        if(R.id.btn_agree==view.getId()){
            MMKVManager.getInstance().mmkv().encode("A",true);
        }
        if(mDexAuthorizeListener!=null){
            mDexAuthorizeListener.clickItem(1);
            dismiss();
        }
    }

    public interface DexAuthorizeListener {
        public void clickItem(int position);
    }

}