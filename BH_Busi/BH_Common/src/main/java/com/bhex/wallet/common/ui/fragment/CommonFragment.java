package com.bhex.wallet.common.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.DialogFragment;

import com.bhex.network.mvx.base.BaseDialogFragment;
import com.bhex.tools.utils.PixelUtils;
import com.bhex.wallet.common.R;

/**
 *
 */
public class CommonFragment extends BaseDialogFragment {

    public Activity mContext;

    private Params mParams;

    @Override
    public int getLayout() {
        return R.layout.fragment_common;
    }

    public CommonFragment(Activity activity, Params params){
        this.mContext = activity;
        this.mParams = params;
    }

    public void initStart(){
        setStyle(DialogFragment.STYLE_NO_TITLE, STYLE_NO_TITLE);
        Window window = getDialog().getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.getAttributes().windowAnimations = R.style.centerDialogStyle;

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;

        params.width = dm.widthPixels - PixelUtils.dp2px(mContext,40);
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        window.setAttributes(params);
    }

    @Override
    protected void initView() {
        super.initView();
        //标题
        AppCompatTextView tv_title = mRootView.findViewById(R.id.tv_title);
        if(!TextUtils.isEmpty(mParams.title)){
            tv_title.setText(mParams.title);
        }
        //内容
        AppCompatTextView tv_content = mRootView.findViewById(R.id.tv_content);
        if(!TextUtils.isEmpty(mParams.message)){
            tv_content.setText(mParams.message);
        }

        //左边按钮
        AppCompatTextView btn_cancel = mRootView.findViewById(R.id.btn_cancel);
        if(!TextUtils.isEmpty(mParams.leftText)){
            btn_cancel.setText(mParams.leftText);
        }

        //右边按钮
        AppCompatTextView btn_sure = mRootView.findViewById(R.id.btn_sure);
        if(!TextUtils.isEmpty(mParams.rightText)){
            btn_sure.setText(mParams.rightText);
        }

        btn_cancel.setOnClickListener(v -> {
            dismiss();
            if(mParams.mOnActionClickListener!=null){
                mParams.mOnActionClickListener.onClick(v);
            }
        });
        btn_sure.setOnClickListener(v->{
            dismiss();
            if(mParams.mOnActionClickListener!=null){
                mParams.mOnActionClickListener.onClick(v);
            }
        });
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog =  super.onCreateDialog(savedInstanceState);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        return dialog;
    }

    public static Builder builder(Activity activity){
        return new Builder(activity);
    }

    public static class Builder{
        private Params params = new Params();

        public Activity context;

        public Builder(Activity activity) {
            this.context = activity;
        }

        public Builder setTitle(String title) {
            params.title = title;
            return this;
        }

        public Builder setMessage(String message) {
            params.message = message;
            return this;
        }

        public Builder setRightText(String text){
            params.rightText = text;
            return this;
        }

        public Builder setLeftText(String text){
            params.leftText = text;
            return this;
        }

        public Builder setAction(View.OnClickListener onActionClickListener) {
            params.mOnActionClickListener = onActionClickListener;
            return this;
        }

        public CommonFragment create(){
            CommonFragment fragment = new CommonFragment(context,params);
            return  fragment;
        }

    }

    public static class Params{
        public String title;
        public String message;

        public String leftText;
        public String rightText;

        public View.OnClickListener mOnActionClickListener;
    }

    public interface DialogItemClickListener{
        public void dialogItemClick(View view, int position);
    }

}