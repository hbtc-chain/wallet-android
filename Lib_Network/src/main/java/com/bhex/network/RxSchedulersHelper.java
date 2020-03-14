package com.bhex.network;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * created by gongdongyang
 * on 2020/2/24
 */
public class RxSchedulersHelper {

    private static final String TAG = "RxSchedulersHelper";

    public static <T> ObservableTransformer<T, T> io_main() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.computation())
                        //.doOnNext(sTokenChecker)
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}
