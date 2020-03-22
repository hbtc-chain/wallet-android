package com.bhex.network.observer;

import android.app.ProgressDialog;
import android.content.Context;

import com.bhex.network.R;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * created by gongdongyang
 * on 2020/2/26
 */
public abstract class ProgressDialogExtObserver<T> implements Observer<T> {

    private String LoadingText;

    private ProgressDialog loadingDialog = null;

    public Context context;

    public ProgressDialogExtObserver(Context context) {
        this(context, context.getString(R.string.http_loading));
    }

    public ProgressDialogExtObserver(Context context, String hint) {
        this.context = context;
        this.LoadingText = hint;
    }


    @Override
    public void onSubscribe(Disposable disposable) {
        showLoading();
    }

    @Override
    public void onNext(T t) {
        closeLoading();
        onSuccess(t);
    }

    protected abstract void onSuccess(T t);

    @Override
    public void onError(Throwable e) {
        closeLoading();
    }

    @Override
    public void onComplete() {
        closeLoading();
    }

    private void showLoading(){
        if (loadingDialog == null){
            loadingDialog = new ProgressDialog(this.context);
        }
        loadingDialog.setMessage(this.LoadingText);
        loadingDialog.show();
    }

    public void closeLoading() {
        if (loadingDialog!=null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }
}
