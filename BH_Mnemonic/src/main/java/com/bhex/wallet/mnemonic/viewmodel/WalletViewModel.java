package com.bhex.wallet.mnemonic.viewmodel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bhex.network.RxSchedulersHelper;
import com.bhex.network.app.BaseApplication;
import com.bhex.network.base.LoadDataModel;
import com.bhex.network.base.LoadModel;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.network.observer.ProgressDialogExtObserver;
import com.bhex.network.observer.SimpleObserver;
import com.bhex.tools.utils.LogUtils;
import com.bhex.wallet.common.db.AppDataBase;
import com.bhex.wallet.common.db.dao.BHWalletDao;
import com.bhex.wallet.common.db.entity.BHWalletExt;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.mnemonic.utils.BHWalletUtils;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/3/6
 * Time: 11:00
 */
public class WalletViewModel extends ViewModel {

    private BHWalletDao bhWalletDao;
    //private AppCompatActivity mContext;

    public MutableLiveData<String> mutableLiveData = new MutableLiveData<>();

    public MutableLiveData<LoadDataModel<List<BHWalletExt>>> mutableWallentLiveData = new MutableLiveData<>();

    public WalletViewModel() {
        bhWalletDao = AppDataBase.getInstance(BaseApplication.getInstance()).bhWalletDao();
    }


    /*public void setmContext(AppCompatActivity mContext) {
        this.mContext = mContext;
    }*/

    /**
     * 创建助记词并保存本地
     */
    public void generateMnemonic(BaseActivity activity,String name, String pwd){

        ProgressDialogExtObserver pbo = new ProgressDialogExtObserver<BHWalletExt>(activity) {
            @Override
            public void onSuccess(BHWalletExt bhWalletExt) {
                mutableLiveData.postValue("1");
            }

            @Override
            public void onError(Throwable e) {
                mutableLiveData.postValue("2");
                super.onError(e);
            }
        };

        Observable.create((emitter)->{
            BHWalletExt walletExt = BHWalletUtils.generateMnemonic(name,pwd);
            bhWalletDao.insert(walletExt);
            emitter.onNext(walletExt);
        }).compose(RxSchedulersHelper.io_main()).subscribe(pbo);

    }

    /**
     * 获取所有的钱包
     */
    public void loadWallet(AppCompatActivity activity){
        SimpleObserver<List<BHWalletExt>> observer = new SimpleObserver<List<BHWalletExt>>() {
            @Override
            public void onNext(List<BHWalletExt> bhWalletExts) {
                super.onNext(bhWalletExts);
                LoadDataModel<List<BHWalletExt>> loadDataModel = new LoadDataModel<>(bhWalletExts);
                mutableWallentLiveData.postValue(loadDataModel);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        };


        Observable.create(new ObservableOnSubscribe<List<BHWalletExt>>() {
            @Override
            public void subscribe(ObservableEmitter<List<BHWalletExt>> emitter) throws Exception {
                LogUtils.d("WalletViewModel==>:","======ObservableEmitter====="+bhWalletDao.toString());

                List<BHWalletExt> list = bhWalletDao.loadAll();
                if(list!=null && list.size()>0){
                    BHUserManager.getInstance().setBhWalletExt(list.get(0));
                    BHUserManager.getInstance().setAllWallet(list);
                }
                LogUtils.d("WalletViewModel==>:","======bhWalletDao====="+list.size());

                emitter.onNext(list);

            }
        }).subscribeOn(AndroidSchedulers.mainThread())
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(activity)))
                .subscribe(observer);

    }

}
