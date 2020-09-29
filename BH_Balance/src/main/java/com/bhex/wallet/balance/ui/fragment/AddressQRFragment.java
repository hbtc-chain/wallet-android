package com.bhex.wallet.balance.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.FragmentManager;

import com.bhex.lib.uikit.util.ColorUtil;
import com.bhex.lib.uikit.util.PixelUtils;
import com.bhex.network.mvx.base.BaseDialogFragment;
import com.bhex.network.utils.ToastUtils;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.utils.QREncodUtil;
import com.bhex.tools.utils.ShapeUtils;
import com.bhex.tools.utils.ToolUtils;
import com.bhex.wallet.balance.R;
import com.bhex.wallet.balance.R2;

import butterknife.BindView;

/**
 * @author gongdongyang
 * 2020-9-4 16:08:31
 * 二维码地址
 */
public class AddressQRFragment extends BaseDialogFragment {

    @BindView(R2.id.tv_address_label)
    AppCompatTextView tv_address_label;
    private String tokenName;
    private String address;

    @Override
    public int getLayout() {
        return R.layout.fragment_address_qr;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        FrameLayout frame = view.findViewById(R.id.layout_index_1);
        GradientDrawable drawable = ShapeUtils.getOvalDrawable(36, ColorUtil.getColor(getContext(), R.color.token_icon_color), true, 0);
        frame.setBackground(drawable);

        AppCompatImageView iv_qr_code = view.findViewById(R.id.iv_qr_code);
        AppCompatTextView tv_token_address = view.findViewById(R.id.tv_token_address);
        Bitmap bitmap = QREncodUtil.createQRCode(address, PixelUtils.dp2px(getContext(), 181), PixelUtils.dp2px(getContext(), 181), null);
        iv_qr_code.setImageBitmap(bitmap);
        tv_token_address.setText(address);

        FrameLayout layout_token_icon = view.findViewById(R.id.layout_token_icon);
        GradientDrawable ringDrawable = ShapeUtils.getRingDrawable(42, ColorUtil.getColor(getContext(), R.color.white), 36, PixelUtils.dp2px(getContext(), 6));
        layout_token_icon.setBackground(ringDrawable);

        AppCompatTextView tv_short_name = view.findViewById(R.id.tv_short_name);
        char initial = tokenName.charAt(0);
        tv_short_name.setText(String.valueOf(initial).toUpperCase());

        view.findViewById(R.id.iv_close).setOnClickListener(v -> {
            if (!isHidden()) {
                dismiss();
            }
        });
        view.findViewById(R.id.btn_copy_address).setOnClickListener(v -> {
            if (TextUtils.isEmpty(address)) {
                return;
            }

            ToolUtils.copyText(address, getContext());
            ToastUtils.showToast(getString(R.string.copyed));
        });

        if(address.toLowerCase().startsWith(BHConstants.BHT_TOKEN)){
            tv_address_label.setText(BHConstants.BHT_TOKEN.toUpperCase()+getString(R.string.trusteeship_address));
        }else{
            tv_address_label.setText(tokenName.toUpperCase()+getString(R.string.address));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.getAttributes().windowAnimations = com.bhex.wallet.common.R.style.bottomDialogStyle;

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
    }

    public static AddressQRFragment showFragment(FragmentManager fm, String tag, String tokenName, String address) {
        AddressQRFragment fragment = new AddressQRFragment();
        fragment.address = address;
        fragment.tokenName = tokenName;
        fragment.show(fm, tag);
        return fragment;
    }
}