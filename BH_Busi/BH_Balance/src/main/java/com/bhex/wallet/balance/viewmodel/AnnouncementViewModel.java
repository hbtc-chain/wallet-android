package com.bhex.wallet.balance.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.bhex.network.RxSchedulersHelper;
import com.bhex.network.base.LoadDataModel;
import com.bhex.tools.utils.LogUtils;
import com.bhex.wallet.common.base.BaseActivity;
import com.bhex.network.observer.BHProgressObserver;
import com.bhex.network.observer.BaseObserver;
import com.bhex.network.utils.JsonUtils;
import com.bhex.network.utils.ToastUtils;
import com.bhex.wallet.balance.R;
import com.bhex.wallet.balance.model.AnnouncementItem;
import com.bhex.wallet.common.api.BHttpApi;
import com.bhex.wallet.common.api.BHttpApiInterface;
import com.bhex.wallet.common.manager.BHUserManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import java.util.List;

import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author gongdongyang
 * 2020-12-11 22:56:20
 * 公告
 */
public class AnnouncementViewModel extends AndroidViewModel {

    public MutableLiveData<LoadDataModel> mutableLiveData  = new MutableLiveData<>();

    public AnnouncementViewModel(@NonNull Application application) {
        super(application);
    }


    /**
     * @param context
     */
    public void loadAnnouncement(FragmentActivity context){
        BaseObserver<JsonArray> observer = new BaseObserver<JsonArray>() {
            @Override
            public void onSuccess(JsonArray jsonArray) {
                if(jsonArray!=null && jsonArray.isJsonArray()){
                    //
                    List<AnnouncementItem> list = JsonUtils.getListFromJson(jsonArray.toString(),AnnouncementItem.class);
                    LoadDataModel ldm = new LoadDataModel(list);
                    mutableLiveData.postValue(ldm);
                }else{
                    LoadDataModel ldm = new LoadDataModel(LoadDataModel.ERROR,"");
                    mutableLiveData.postValue(ldm);
                }
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                super.onFailure(code, errorMsg);
                LoadDataModel ldm = new LoadDataModel(LoadDataModel.ERROR,errorMsg);
                mutableLiveData.postValue(ldm);
            }
        };
        BHttpApi.getService(BHttpApiInterface.class).loadAnnouncement()
                .observeOn(Schedulers.io())
                .compose(RxSchedulersHelper.io_main())
                .subscribe(observer);
    }
}
