package com.bhex.wallet.common.viewmodel;

import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bhex.network.RxSchedulersHelper;
import com.bhex.network.app.BaseApplication;
import com.bhex.network.base.LoadDataModel;
import com.bhex.network.base.LoadingStatus;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.network.observer.BHProgressObserver;
import com.bhex.network.observer.SimpleObserver;
import com.bhex.tools.crypto.CryptoUtil;
import com.bhex.tools.crypto.HexUtils;
import com.bhex.tools.utils.MD5;
import com.bhex.wallet.common.db.AppDataBase;
import com.bhex.wallet.common.db.dao.BHWalletDao;
import com.bhex.wallet.common.db.entity.BHWallet;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.common.utils.BHWalletUtils;
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

    public MutableLiveData<LoadDataModel> mutableLiveData  = new MutableLiveData<>();

    public MutableLiveData<LoadDataModel<List<BHWallet>>> mutableWallentLiveData = new MutableLiveData<>();

    public MutableLiveData<LoadDataModel<BHWallet>> walletLiveData = new MutableLiveData<>();

    public WalletViewModel() {
        bhWalletDao = AppDataBase.getInstance(BaseApplication.getInstance()).bhWalletDao();
    }


    /**
     * 创建助记词并保存本地
     */
    public void generateMnemonic(BaseActivity activity,String name, String pwd){

        BHProgressObserver pbo = new BHProgressObserver<BHWallet>(activity) {
            @Override
            public void onSuccess(BHWallet bhWallet) {
                LoadDataModel loadDataModel = new LoadDataModel("");
                BHUserManager.getInstance().setCurrentBhWallet(bhWallet);
                mutableLiveData.postValue(loadDataModel);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                LoadDataModel loadDataModel = new LoadDataModel(LoadingStatus.ERROR,"");
                mutableLiveData.postValue(loadDataModel);

            }
        };

        Observable.create((emitter)->{
            try{
                BHWallet bhWallet = BHWalletUtils.generateMnemonic(name,pwd);
                int maxId = bhWalletDao.loadMaxId();
                if(maxId==0){
                    bhWallet.isDefault = 1;
                }
                bhWallet.id = maxId+1;
                int id = bhWalletDao.insert(bhWallet).intValue();
                bhWallet.id = id;
                BHUserManager.getInstance().setCurrentBhWallet(bhWallet);
                emitter.onNext(bhWallet);
                emitter.onComplete();
            }catch (Exception e){
                e.printStackTrace();
                emitter.onError(e);
            }
        }).compose(RxSchedulersHelper.io_main())
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(activity)))
                .subscribe(pbo);
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
                    BHUserManager.getInstance().setAllWallet(list);
                    BHUserManager.getInstance().setCurrentBhWallet(list.get(0));
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
    public void updateWallet(AppCompatActivity activity,BHWallet bhWallet,int bh_id,int isDefault){
        BHProgressObserver pbo = new BHProgressObserver<String>(activity) {
            @Override
            protected void onSuccess(String str) {
                LoadDataModel loadDataModel = new LoadDataModel("");
                bhWallet.isDefault = 1;
                BHUserManager.getInstance().setCurrentBhWallet(bhWallet);
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
                int res = bhWalletDao.updateNoDefault(0);
                //把bh_id设置默认
                res = bhWalletDao.update(bh_id,isDefault);

                emitter.onNext("");
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

    /**
     *
     * @param activity
     * @param bh_id
     */
    public void deleteWallet(AppCompatActivity activity,int bh_id){
        BHProgressObserver pbo = new BHProgressObserver<String>(activity) {
            @Override
            protected void onSuccess(String str) {
                //ToastUtils.showToast(str);
                LoadDataModel loadDataModel = new LoadDataModel("");
                mutableLiveData.postValue(loadDataModel);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                LoadDataModel loadDataModel = new LoadDataModel(LoadingStatus.ERROR,"");
                mutableLiveData.postValue(loadDataModel);
            }
        };

        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                //把所有设置非默认
                bhWalletDao.deleteWallet(bh_id);
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

    /**
     * @param activity
     * @param name
     * @param pwd
     */
    public void importMnemonic(BaseActivity activity,List<String> words, String name, String pwd){
        BHProgressObserver pbo = new BHProgressObserver<BHWallet>(activity) {
            @Override
            public void onSuccess(BHWallet bhWallet) {
                LoadDataModel loadDataModel = new LoadDataModel("");
                BHUserManager.getInstance().setCurrentBhWallet(bhWallet);
                mutableLiveData.postValue(loadDataModel);
            }

            @Override
            public void onNext(BHWallet bhWallet) {
                super.onNext(bhWallet);

            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                LoadDataModel loadDataModel = new LoadDataModel(LoadingStatus.ERROR,"");
                mutableLiveData.postValue(loadDataModel);

            }
        };

        Observable.create((emitter)->{
            try{
                BHWallet walletExt = BHWalletUtils.importMnemonic(BHWalletUtils.BH_CUSTOM_TYPE,words,name,pwd);
                int maxId = bhWalletDao.loadMaxId();
                if(maxId==0){
                    walletExt.isDefault = 1;
                }
                walletExt.id = maxId+1;
                int id = bhWalletDao.insert(walletExt).intValue();
                emitter.onNext(walletExt);
                emitter.onComplete();
            }catch (Exception e){
                e.printStackTrace();
                emitter.onError(e);
            }

        }).compose(RxSchedulersHelper.io_main())
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(activity)))
                .subscribe(pbo);
    }

    /**
     * 导入私钥
     * @param name
     * @param pwd
     */
    public void importPrivateKey(BaseActivity activity,String name, String pwd) {
        BHProgressObserver pbo = new BHProgressObserver<BHWallet>(activity) {
            @Override
            public void onSuccess(BHWallet bhWalletExt) {
                LoadDataModel loadDataModel = new LoadDataModel("");
                mutableLiveData.postValue(loadDataModel);
            }

            @Override
            public void onNext(BHWallet bhWallet) {
                super.onNext(bhWallet);

            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                LoadDataModel loadDataModel = new LoadDataModel(LoadingStatus.ERROR,"");
                mutableLiveData.postValue(loadDataModel);

            }
        };

        Observable.create((emitter)->{
            try{
                String privateKey = BHUserManager.getInstance().getTmpBhWallet().getPrivateKey();
                BHWallet walletExt = BHWalletUtils.importPrivateKey(privateKey,name,pwd);
                int maxId = bhWalletDao.loadMaxId();
                if(maxId==0){
                    walletExt.setIsDefault(1);
                }
                walletExt.id = maxId+1;
                int id = bhWalletDao.insert(walletExt).intValue();

                emitter.onNext(walletExt);
                emitter.onComplete();
            }catch (Exception e){
                e.printStackTrace();
                emitter.onError(e);
            }

        }).compose(RxSchedulersHelper.io_main())
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(activity)))
                .subscribe(pbo);
    }

    /**
     * 修改密码
     * @param newPwd
     */
    public void updatePassword(BaseActivity activity,String newPwd,BHWallet bhWallet) {
        BHProgressObserver pbo = new BHProgressObserver<BHWallet>(activity) {
            @Override
            public void onSuccess(BHWallet str) {
                LoadDataModel loadDataModel = new LoadDataModel(str);
                mutableLiveData.postValue(loadDataModel);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                LoadDataModel loadDataModel = new LoadDataModel(LoadingStatus.ERROR,"");
                mutableLiveData.postValue(loadDataModel);

            }
        };

        Observable.create((emitter)->{
            try{
                String pwdMd5 = MD5.md5(newPwd);
                //bhWallet.password = pwdMd5;
                //解密私钥
                byte[] originPK = CryptoUtil.decrypt(HexUtils.toBytes(bhWallet.privateKey),bhWallet.password);
                //加密私钥
                byte[] newEnPK = CryptoUtil.encrypt(originPK,pwdMd5);
                bhWallet.privateKey = HexUtils.toHex(newEnPK);
                //解密助记词
                if(!TextUtils.isEmpty(bhWallet.mnemonic)){
                    //解密助记词
                    byte [] originMnemonic = CryptoUtil.decrypt(HexUtils.toBytes(bhWallet.mnemonic),bhWallet.password);
                    //加密助记词
                    byte [] newEnMnemonic = CryptoUtil.encrypt(originMnemonic,pwdMd5);
                    bhWallet.mnemonic = HexUtils.toHex(newEnMnemonic);
                }

                bhWallet.password = pwdMd5;

                //int res = bhWalletDao.updatePassword(bhWallet.id,pwdMd5);
                int res = bhWalletDao.update(bhWallet);
                if(res>0){
                    emitter.onNext(bhWallet);
                }
                emitter.onComplete();
            }catch (Exception e){
                e.printStackTrace();
                emitter.tryOnError(e);
            }

        }).compose(RxSchedulersHelper.io_main())
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(activity)))
                .subscribe(pbo);
    }

}
