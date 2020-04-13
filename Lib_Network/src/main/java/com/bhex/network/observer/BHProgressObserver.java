package com.bhex.network.observer;

import android.app.ProgressDialog;
import android.content.Context;

import com.bhex.network.R;

import io.reactivex.disposables.Disposable;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/4/6
 * Time: 20:39
 */
public abstract class BHProgressObserver<T> extends BHBaseObserver<T> {

    private String loadingText;

    private ProgressDialog loadingDialog = null;

    public BHProgressObserver(Context context) {
        this(context, context.getString(R.string.http_loading));
    }

    public BHProgressObserver(Context context, String hint) {
        this.context = context;
        this.loadingText = hint;
    }

    @Override
    protected void onSuccess(T t) {
        closeLoading();
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        closeLoading();
    }

    @Override
    public void onSubscribe(Disposable d) {
        showLoading();
    }

    @Override
    public void onComplete() {
        closeLoading();
    }


    public void showLoading(){
        if (loadingDialog == null){
            loadingDialog = new ProgressDialog(this.context);
        }
        loadingDialog.setMessage(this.loadingText);
        loadingDialog.show();
    }

    public void closeLoading() {
        if (this.context != null && loadingDialog!=null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

}
