package com.bhex.network.observer;

import android.app.ProgressDialog;
import android.content.Context;

import com.bhex.network.R;

import io.reactivex.disposables.Disposable;

/**
 * created by gongdongyang
 * on 2020/2/26
 */
public abstract class ProgressDialogObserver<T> extends BaseObserver<T> {

    private String LoadingText;

    private ProgressDialog loadingDialog = null;

    public Context context;

    public ProgressDialogObserver(Context context) {
        this(context, context.getString(R.string.http_loading));
    }

    public ProgressDialogObserver(Context context, String hint) {
        super(true);
        this.context = context;
        this.LoadingText = hint;
    }

    @Override
    public void onComplete() {
        super.onComplete();
        closeLoading();
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);

    }

    @Override
    public void onSubscribe(Disposable d) {
        super.onSubscribe(d);
        if (loadingDialog == null){
            loadingDialog = new ProgressDialog(this.context);
        }
        loadingDialog.setMessage(this.LoadingText);
        loadingDialog.show();
    }

    public void closeLoading() {
        if (this.context != null && loadingDialog!=null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }


}
