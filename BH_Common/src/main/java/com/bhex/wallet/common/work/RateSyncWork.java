package com.bhex.wallet.common.work;

import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.work.ListenableWorker;
import androidx.work.RxWorker;
import androidx.work.WorkerParameters;

import com.bhex.wallet.common.api.BHttpApi;
import com.bhex.wallet.common.api.BHttpApiInterface;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.common.model.BHRates;
import com.google.common.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import io.reactivex.Single;

/**
 * @author gongdongyang
 * 2020-6-12 19:31:05
 */
public class RateSyncWork extends RxWorker {

    public RateSyncWork(@NonNull Context appContext, @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public Single<ListenableWorker.Result> createWork() {
        Type type = (new TypeToken<List<BHRates>>() {}).getType();
        String balacne_list = BHUserManager.getInstance().getSymbolList();
        balacne_list = balacne_list.replace("_",",").toUpperCase();
        //return Single.fromObservable(BHttpApi.getService(BHttpApiInterface.class).loadRates(balacne_list));
        return null;

    }
}
