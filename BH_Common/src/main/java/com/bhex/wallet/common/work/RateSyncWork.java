package com.bhex.wallet.common.work;

/*import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.RxWorker;
import androidx.work.WorkerParameters;

import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.common.model.BHRates;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.functions.Function;

*//**
 * @author gongdongyang
 * 2020-6-12 19:31:05
 *//*
public class RateSyncWork extends RxWorker {

    public RateSyncWork(@NonNull Context appContext, @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
    }

    @NonNull
    @Override
    public Single<Result> createWork() {
        Type type = (new TypeToken<List<BHRates>>() {}).getType();
        String balacne_list = BHUserManager.getInstance().getSymbolList();
        balacne_list = balacne_list.replace("_",",").toUpperCase();
        //return Single.fromObservable(BHttpApi.getService(BHttpApiInterface.class).loadRates(balacne_list)).
        return Observable.range(0, 100)
                .flatMap(new Function<Integer, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Integer integer) throws Exception {
                        return null;
                    }
                })
                .toList()
                .map(new Function<List<Object>, Result>() {
                    @Override
                    public Result apply(List<Object> objects) throws Exception {
                        return null;
                    }
                });
    }
}*/
