package com.bhex.wallet.mnemonic.viewmodel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bhex.network.RxSchedulersHelper;
import com.bhex.network.app.BaseApplication;
import com.bhex.network.base.LoadDataModel;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.network.observer.ProgressDialogExtObserver;
import com.bhex.network.observer.SimpleObserver;
import com.bhex.network.utils.ToastUtils;
import com.bhex.tools.utils.LogUtils;
import com.bhex.wallet.common.db.AppDataBase;
import com.bhex.wallet.common.db.dao.BHWalletDao;
import com.bhex.wallet.common.db.entity.BHWallet;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.mnemonic.utils.BHWalletUtils;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/3/6
 * Time: 11:00
 */
public class WalletViewModel extends ViewModel {

    private BHWalletDao bhWalletDao;

    //public MutableLiveData<LoadDataModel> mutableLiveData = new MutableLiveData<>();

    public MutableLiveData<LoadDataModel> mutableLiveData  = new MutableLiveData<>();

    public MutableLiveData<LoadDataModel<List<BHWallet>>> mutableWallentLiveData = new MutableLiveData<>();

    public WalletViewModel() {
        bhWalletDao = AppDataBase.getInstance(BaseApplication.getInstance()).bhWalletDao();
    }


    /**
     * 创建助记词并保存本地
     */
    public void generateMnemonic(BaseActivity activity,String name, String pwd){

        ProgressDialogExtObserver pbo = new ProgressDialogExtObserver<BHWallet>(activity) {
            @Override
            public void onSuccess(BHWallet bhWalletExt) {
                LoadDataModel loadDataModel = new LoadDataModel("");
                mutableLiveData.postValue(loadDataModel);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                LoadDataModel loadDataModel = new LoadDataModel();
                mutableLiveData.postValue(loadDataModel);

            }
        };

        Observable.create((emitter)->{
            try{
                BHWallet walletExt = BHWalletUtils.generateMnemonic(name,pwd);
                int maxId = bhWalletDao.loadMaxId();
                //LogUtils.d("WalletViewModel==>:","maxId=="+maxId);
                walletExt.id = maxId+1;
                int id = bhWalletDao.insert(walletExt).intValue();

                emitter.onNext(walletExt);
            }catch (Exception e){
                e.printStackTrace();
            }

        }).compose(RxSchedulersHelper.io_main()).subscribe(pbo);

    }

    /**
     * 获取所有的钱包
     */
    public void loadWallet(AppCompatActivity activity){
        SimpleObserver<List<BHWallet>> observer = new SimpleObserver<List<BHWallet>>() {
            @Override
            public void onNext(List<BHWallet> bhWalletList) {
                super.onNext(bhWalletList);
                LoadDataModel<List<BHWallet>> loadDataModel = new LoadDataModel<>(bhWalletList);
                mutableWallentLiveData.postValue(loadDataModel);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        };


        Observable.create(new ObservableOnSubscribe<List<BHWallet>>() {
            @Override
            public void subscribe(ObservableEmitter<List<BHWallet>> emitter) throws Exception {
                List<BHWallet> list = bhWalletDao.loadAll();
                if(list!=null && list.size()>0){

                    BHUserManager.getInstance().setBhWalletExt(list.get(0));
                    BHUserManager.getInstance().setAllWallet(list);
                }
                emitter.onNext(list);

            }
        }).subscribeOn(AndroidSchedulers.mainThread())
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(activity)))
                .subscribe(observer);

    }

    /**
     * 更新钱包的状态
     */
    public void updateWallet(AppCompatActivity activity,int bh_id,int isDefault){
        ProgressDialogExtObserver pbo = new ProgressDialogExtObserver<String>(activity) {
            @Override
            protected void onSuccess(String str) {
                ToastUtils.showToast(str);
                LoadDataModel loadDataModel = new LoadDataModel("");
                mutableLiveData.postValue(loadDataModel);


            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                LoadDataModel loadDataModel = new LoadDataModel();
                mutableLiveData.postValue(loadDataModel);
            }
        };


        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                //把所有设置非默认
                bhWalletDao.updateNoDefault(0);
                //把bh_id设置默认
                bhWalletDao.update(bh_id,isDefault);
                emitter.onNext("1");
                emitter.onComplete();
            }
        }).flatMap(new Function<String, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(String s) throws Exception {
                List<BHWallet> list = bhWalletDao.loadAll();
                return  Observable.just("apply");
            }
        }).compose(RxSchedulersHelper.io_main())
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(activity)))
                .subscribe(pbo);

    }

}
