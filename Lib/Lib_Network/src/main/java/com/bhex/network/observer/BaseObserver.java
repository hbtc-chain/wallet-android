package com.bhex.network.observer;

import com.bhex.network.base.BaseResponse;
import com.bhex.network.exception.ApiException;
import com.bhex.network.exception.ExceptionEngin;
import com.bhex.network.utils.ToastUtils;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * created by gongdongyang
 * on 2020/2/24
 */
public abstract class BaseObserver<T> implements Observer<T> {

    private Disposable disposable;

    public boolean isNeedShowtoast = true;

    public BaseObserver(){}


    public BaseObserver(boolean showToast){this.isNeedShowtoast = showToast;}

    public void dispose() {
        if (disposable != null && !disposable.isDisposed()){
            disposable.dispose();
        }
    }

    public boolean isDisposed() {
        return (disposable != null && disposable.isDisposed());
    }

    @Override
    public void onSubscribe(Disposable d) {
        this.disposable = d;
    }

    @Override
    public void onNext(T t) {
        if(t instanceof BaseResponse){
            BaseResponse baseResponse = (BaseResponse)t;
            if(baseResponse.code==200){
                onSuccess(t);
            }else{
                onFailure(baseResponse.getCode(), baseResponse.getMessage());
            }
        }else{
            onSuccess(t);
        }
    }

    @Override
    public void onError(Throwable e) {
        //需要toast提示
        try{
            ApiException apiException = ExceptionEngin.handleException(e);
            onFailure(apiException.getCode(), apiException.getDisplayMessage());
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onComplete() {

    }

    public abstract void onSuccess(T t);

    public void onFailure(int code, String errorMsg){
        if (code == -1)
            return;
        //需要toast提示
        if (this.isNeedShowtoast && code != 1000){
            ToastUtils.showToast(errorMsg);
        }

    }
}
