package com.bhex.network.mvx.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bhex.network.R;

import butterknife.ButterKnife;

/**
 * created by gongdongyang
 * on 2020/2/24
 */
public abstract class BaseFragment<T extends BasePresenter> extends Fragment {

    protected static final String TAG = BaseFragment.class.getSimpleName();

    protected View mRootView  ;

    private BaseActivity mActivity;

    private ProgressDialog mProgressDialog;

    protected T mPresenter;

    private int dialogRef;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPresenter();
    }

    protected void initPresenter() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        if(mRootView==null){
            mRootView = inflater.inflate(getLayoutId(), container, false);
            ButterKnife.bind(this,mRootView);
            initView();
        }else{
            if(mRootView.getParent()!=null){
                ((ViewGroup) mRootView.getParent()).removeView(mRootView);
            }
        }
        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addEvent();
    }

    protected abstract void addEvent();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mActivity = (BaseActivity) context;
    }

    public abstract int getLayoutId();

    protected abstract void initView();

    public BaseActivity getYActivity() {
        return mActivity;
    }


    /**
     * 显示对话框
     */
    public void showProgressbarLoading() {
        showProgressbarLoading("",getResources().getString(R.string.http_loading));
    }

    public void showProgressbarLoading(String title, String hint) {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            return;
        this.mProgressDialog = ProgressDialog.show(getYActivity(), title, hint);
        this.mProgressDialog.setCancelable(true);
        this.mProgressDialog.setCanceledOnTouchOutside(false);
    }

    /**
     * 隐藏对话框
     */
    public void dismissProgressDialog() {
        if (mProgressDialog == null)
            return;

        if (mProgressDialog.isShowing()  && --dialogRef <= 0){
            this.mProgressDialog.dismiss();
        }

    }

}
