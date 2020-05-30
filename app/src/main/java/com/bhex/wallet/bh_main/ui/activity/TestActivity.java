package com.bhex.wallet.bh_main.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.bhex.network.RxSchedulersHelper;
import com.bhex.network.cache.RxCache;
import com.bhex.network.cache.data.CacheResult;
import com.bhex.network.cache.stategy.CacheStrategy;
import com.bhex.tools.utils.LogUtils;
import com.bhex.wallet.R;
import com.bhex.wallet.common.api.BHttpApiInterface;
import com.bhex.wallet.common.model.BHToken;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class TestActivity extends AppCompatActivity {
    public static final String CACHE_KEY = "SymbolCache";
    BHttpApiInterface api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);

        api = new Retrofit.Builder()
                .baseUrl("http://public-chain-mainnet-631149863.ap-northeast-1.elb.amazonaws.com:26657/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(new OkHttpClient.Builder().build())
                .build()
                .create(BHttpApiInterface.class);

    }

    public void loadNetwork(View view) {
        Type type = (new TypeToken<JsonObject>() {}).getType();

        api.loadSymbol(1,200)
                .compose(RxSchedulersHelper.io_main())
                .compose(RxCache.getDefault().transformObservable(CACHE_KEY, type, CacheStrategy.cacheAndRemote()))
                .map(new CacheResult.MapFunc())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogUtils.d("symbolcache==","onSubscribe==");
                    }

                    @Override
                    public void onNext(JsonObject jsonObject) {
                        LogUtils.d("symbolcache==","onNext=="+jsonObject.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.d("symbolcache==","onError=="+e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void loadCache(View view) {
        Type type = (new TypeToken<JsonObject>() {}).getType();

        api.loadSymbol(1,200)
                .compose(RxSchedulersHelper.io_main())
                .compose(RxCache.getDefault().transformObservable(CACHE_KEY, type, CacheStrategy.onlyCache()))
                .map(new CacheResult.MapFunc())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogUtils.d("symbolcache==","onSubscribe==");
                    }

                    @Override
                    public void onNext(JsonObject jsonObject) {
                        LogUtils.d("symbolcache==","onNext=="+jsonObject.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.d("symbolcache==","onError=="+e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
