package com.bhex.network.base.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bhex.network.base.LoadDataModel;
import com.bhex.network.observer.BaseObserver;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * created by gongdongyang
 * on 2020/2/24
 */
public class AutoDisposeViewModel extends ViewModel {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public void addDispos(Disposable disposable) {
        this.compositeDisposable.add(disposable);
    }

    public <T> BaseObserver<T> createObserver(final MutableLiveData<LoadDataModel<T>> liveData) {
        return new BaseObserver<T>() {
            @Override
            public void onSuccess(T t) {
                liveData.setValue(new LoadDataModel(t));
                onCleared();
            }


            @Override
            public void onFailure(int code, String errorMsg) {
                super.onFailure(code, errorMsg);
                onCleared();
                liveData.setValue(new LoadDataModel(code,errorMsg));
            }

            @Override
            public void onSubscribe(Disposable disposable) {
                super.onSubscribe(disposable);
                addDispos(disposable);
            }
        };
    }

    protected void dispose() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed())
            this.compositeDisposable.clear();
    }

    protected void onCleared() {
        super.onCleared();
        dispose();
    }
}
