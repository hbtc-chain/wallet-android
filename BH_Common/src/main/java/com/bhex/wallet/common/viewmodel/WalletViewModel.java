package com.bhex.wallet.common.viewmodel;

import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bhex.network.RxSchedulersHelper;
import com.bhex.network.app.BaseApplication;
import com.bhex.network.base.LoadDataModel;
import com.bhex.network.base.LoadingStatus;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.network.observer.BHBaseObserver;
import com.bhex.network.observer.BHProgressObserver;
import com.bhex.network.observer.SimpleObserver;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.crypto.CryptoUtil;
import com.bhex.tools.crypto.HexUtils;
import com.bhex.tools.utils.MD5;
import com.bhex.wallet.common.db.AppDataBase;
import com.bhex.wallet.common.db.dao.BHWalletDao;
import com.bhex.wallet.common.db.entity.BHWallet;
import com.bhex.wallet.common.enums.BACK_WALLET_TYPE;
import com.bhex.wallet.common.helper.BHWalletHelper;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.common.utils.BHWalletUtils;
import com.bhex.wallet.common.utils.LiveDataBus;
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

    public MutableLiveData<LoadDataModel<BHWallet>> deleteLiveData = new MutableLiveData<>();


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
            protected void onFailure(int code, String errorMsg) {
                super.onFailure(code, errorMsg);
                LoadDataModel loadDataModel = new LoadDataModel(code,"");
                mutableLiveData.postValue(loadDataModel);
            }
        };

        Observable.create((emitter)->{
            try{
                BHWallet bhWallet = BHWalletUtils.generateMnemonic(name,pwd);
                int maxId = bhWalletDao.loadMaxId();

                bhWallet.id = maxId+1;
                int id = bhWalletDao.insert(bhWallet).intValue();

                //设置默认钱包
                //把所有设置非默认
                int res = bhWalletDao.updateNoDefault(0);
                //把bh_id设置默认
                res = bhWalletDao.update(id,1);

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

        Observable.create((ObservableOnSubscribe<List<BHWallet>>)emitter -> {
            List<BHWallet> list = bhWalletDao.loadAll();
            if(list!=null && list.size()>0){
                BHUserManager.getInstance().setAllWallet(list);
                BHUserManager.getInstance().setCurrentBhWallet(list.get(0));
            }
            emitter.onNext(list);
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
            protected void onFailure(int code, String errorMsg) {
                super.onFailure(code, errorMsg);
                LoadDataModel loadDataModel = new LoadDataModel();
                mutableLiveData.postValue(loadDataModel);
            }
        };


        Observable.create((ObservableOnSubscribe<String>)emitter -> {
            //把所有设置非默认
            int res = bhWalletDao.updateNoDefault(0);
            //把bh_id设置默认
            res = bhWalletDao.update(bh_id,isDefault);

            emitter.onNext("");
            emitter.onComplete();
        }).flatMap((Function<String, ObservableSource<String>>)s -> {
            List<BHWallet> list = bhWalletDao.loadAll();
            return  Observable.just("apply");
        }).compose(RxSchedulersHelper.io_main())
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(activity)))
                .subscribe(pbo);
    }


    public void updateWalletName(Fragment fragment, BHWallet bhWallet){
        BHProgressObserver pbo = new BHProgressObserver<String>(fragment.getContext()) {
            @Override
            protected void onSuccess(String str) {
                LoadDataModel loadDataModel = new LoadDataModel("");
                bhWallet.isDefault = 1;
                BHUserManager.getInstance().setCurrentBhWallet(bhWallet);
                mutableLiveData.postValue(loadDataModel);
            }

            @Override
            protected void onFailure(int code, String errorMsg) {
                super.onFailure(code, errorMsg);
                LoadDataModel loadDataModel = new LoadDataModel();
                mutableLiveData.postValue(loadDataModel);
            }
        };


        Observable.create((ObservableOnSubscribe<String>)emitter -> {
            //把所有设置非默认
            //int res = bhWalletDao.updateNoDefault(0);
            //把bh_id设置默认
            int res = bhWalletDao.update(bhWallet);

            emitter.onNext("");
            emitter.onComplete();
        }).flatMap((Function<String, ObservableSource<String>>)s -> {
            List<BHWallet> list = bhWalletDao.loadAll();
            return  Observable.just("apply");
        }).compose(RxSchedulersHelper.io_main())
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(fragment)))
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
                deleteLiveData.postValue(loadDataModel);
            }

            @Override
            protected void onFailure(int code, String errorMsg) {
                super.onFailure(code, errorMsg);
                LoadDataModel loadDataModel = new LoadDataModel(code,errorMsg);
                deleteLiveData.postValue(loadDataModel);
            }
        };

        Observable.create((ObservableOnSubscribe<String>)emitter -> {
            //把所有设置非默认
            bhWalletDao.deleteWallet(bh_id);
            //更新所有用钱包
            //更新当前默认钱包
            List<BHWallet> allBhWallet = bhWalletDao.loadAll();
            if(allBhWallet!=null && allBhWallet.size()>0){
                BHUserManager.getInstance().setCurrentBhWallet(allBhWallet.get(0));
                BHUserManager.getInstance().setAllWallet(allBhWallet);
            }

            emitter.onNext("1");
            emitter.onComplete();
          }
        ).flatMap(new Function<String, ObservableSource<String>>() {
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
                if(bhWallet==null || TextUtils.isEmpty(bhWallet.address)){
                    LoadDataModel loadDataModel = new LoadDataModel(1,"");
                    mutableLiveData.postValue(loadDataModel);
                }else{
                    LoadDataModel loadDataModel = new LoadDataModel();
                    BHUserManager.getInstance().setCurrentBhWallet(bhWallet);
                    mutableLiveData.postValue(loadDataModel);
                }
            }

            @Override
            protected void onFailure(int code, String errorMsg) {
                super.onFailure(code, errorMsg);
                LoadDataModel loadDataModel = new LoadDataModel(code,"");
                mutableLiveData.postValue(loadDataModel);
            }
        };

        Observable.create((emitter)->{
            try{
                //验证托管单元地址是否存在
                String bh_address = BHWalletUtils.memonicToAddress(words);
                //判断助记词是否已经导入过
                boolean isWalletExist = BHWalletHelper.isExistBHWallet(bh_address);
                if(isWalletExist){
                    emitter.onNext(new BHWallet());
                    emitter.onComplete();
                }else{
                    BHWallet bhWallet = BHWalletUtils.importMnemonic(BHWalletUtils.BH_CUSTOM_TYPE,words,name,pwd);
                    int maxId = bhWalletDao.loadMaxId();
                    bhWallet.isBackup = BACK_WALLET_TYPE.已备份.value;
                    bhWallet.id = maxId+1;
                    int id = bhWalletDao.insert(bhWallet).intValue();

                    //设置默认钱包
                    //把所有设置非默认
                    int res = bhWalletDao.updateNoDefault(0);
                    //把bh_id设置默认
                    res = bhWalletDao.update(id,1);

                    //更新当前默认钱包
                    List<BHWallet> allBhWallet = bhWalletDao.loadAll();
                    if(allBhWallet!=null && allBhWallet.size()>0){
                        BHUserManager.getInstance().setCurrentBhWallet(allBhWallet.get(0));
                        BHUserManager.getInstance().setAllWallet(allBhWallet);
                    }
                    emitter.onNext(bhWallet);
                    emitter.onComplete();
                }
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
            public void onSuccess(BHWallet bhWallet) {
                if(bhWallet==null || TextUtils.isEmpty(bhWallet.address)){
                    LoadDataModel loadDataModel = new LoadDataModel(1,"");
                    mutableLiveData.postValue(loadDataModel);
                }else{
                    LoadDataModel loadDataModel = new LoadDataModel("");
                    mutableLiveData.postValue(loadDataModel);
                }

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
                //先判断地址是否存在
                String bh_address = BHWalletUtils.privatekeyToAddress(privateKey);
                boolean isWalletExist = BHWalletHelper.isExistBHWallet(bh_address);

                if(isWalletExist){
                    emitter.onNext(new BHWallet());
                    emitter.onComplete();
                }else{
                    BHWallet bhWallet = BHWalletUtils.importPrivateKey(privateKey,name,pwd);
                    int maxId = bhWalletDao.loadMaxId();
                    bhWallet.isBackup = BACK_WALLET_TYPE.已备份.value;
                    bhWallet.id = maxId+1;
                    int resId = bhWalletDao.insert(bhWallet).intValue();

                    //设置默认钱包
                    //把所有设置非默认
                    resId = bhWalletDao.updateNoDefault(0);
                    //把bh_id设置默认
                    resId = bhWalletDao.update(bhWallet.id,1);

                    //更新当前默认钱包
                    List<BHWallet> allBhWallet = bhWalletDao.loadAll();
                    if(allBhWallet!=null && allBhWallet.size()>0){
                        BHUserManager.getInstance().setCurrentBhWallet(allBhWallet.get(0));
                        BHUserManager.getInstance().setAllWallet(allBhWallet);
                    }

                    emitter.onNext(bhWallet);
                    emitter.onComplete();
                }

            }catch (Exception e){
                e.printStackTrace();
                emitter.onError(e);
            }

        }).compose(RxSchedulersHelper.io_main())
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(activity)))
                .subscribe(pbo);
    }

    /**
     * 导入KeyStore文件
     * @param activity
     * @param keyStore
     * @param name
     * @param pwd
     */
    public void importKeyStore(BaseActivity activity,String keyStore,String name, String pwd){
        BHBaseObserver observer = new BHBaseObserver<BHWallet>() {
            @Override
            protected void onSuccess(BHWallet bhWallet) {
                LoadDataModel ldm = new LoadDataModel(bhWallet);
                mutableLiveData.postValue(ldm);
            }

            @Override
            protected void onFailure(int code, String errorMsg) {
                super.onFailure(code, errorMsg);
                LoadDataModel ldm = new LoadDataModel(code,errorMsg);
                mutableLiveData.postValue(ldm);
            }


        };

        Observable.create(emitter -> {
            //判断地址是否存在
            String bh_address = BHWalletUtils.keyStoreToAddress(keyStore,pwd);
            if(BHWalletHelper.isExistBHWallet(bh_address)){
                emitter.onNext(new BHWallet());
            }else if(!TextUtils.isEmpty(bh_address)){
                BHWallet bhWallet = BHWalletUtils.importKeyStore(keyStore,name,pwd);
                //当前
                int maxId = bhWalletDao.loadMaxId();
                bhWallet.isBackup = BACK_WALLET_TYPE.已备份.value;
                bhWallet.id = maxId+1;
                int resId = bhWalletDao.insert(bhWallet).intValue();

                //设置默认钱包
                //把所有设置非默认
                int res = bhWalletDao.updateNoDefault(0);
                //把bh_id设置默认
                res = bhWalletDao.update(bhWallet.id,1);

                //更新当前默认钱包
                List<BHWallet> allBhWallet = bhWalletDao.loadAll();
                if(allBhWallet!=null && allBhWallet.size()>0){
                    BHUserManager.getInstance().setCurrentBhWallet(allBhWallet.get(0));
                    BHUserManager.getInstance().setAllWallet(allBhWallet);
                }
                emitter.onNext(bhWallet);
            }

        }).compose(RxSchedulersHelper.io_main())
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(activity)))
                .subscribe(observer);
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
                walletLiveData.postValue(loadDataModel);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                LoadDataModel loadDataModel = new LoadDataModel(LoadingStatus.ERROR,"");
                walletLiveData.postValue(loadDataModel);

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
                    //更新钱包列表
                    List<BHWallet> list = bhWalletDao.loadAll();
                    BHUserManager.getInstance().setAllWallet(list);
                    BHUserManager.getInstance().setCurrentBhWallet(list.get(0));
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

    /**
     * 备份助记词
     * @param activity
     * @param bhWallet
     */
    public void backupMnemonic(BaseActivity activity,BHWallet bhWallet) {
        BHProgressObserver pbo = new BHProgressObserver<String>(activity) {
            @Override
            protected void onSuccess(String str) {
                LoadDataModel ldm = new LoadDataModel("");
                bhWallet.isBackup = BACK_WALLET_TYPE.已备份.value;
                BHUserManager.getInstance().setCurrentBhWallet(bhWallet);
                //walletLiveData.postValue(loadDataModel);
                LiveDataBus.getInstance().with(BHConstants.Label_Mnemonic_Back,LoadDataModel.class).postValue(ldm);
            }

            @Override
            protected void onFailure(int code, String errorMsg) {
                super.onFailure(code, errorMsg);
                LoadDataModel ldm = new LoadDataModel();
                //walletLiveData.postValue(loadDataModel);
                LiveDataBus.getInstance().with(BHConstants.Label_Mnemonic_Back,LoadDataModel.class).postValue(ldm);
            }
        };


        Observable.create(emitter-> {
            //把bh_id设置默认
            int res = bhWalletDao.backupMnemonic(bhWallet.id);
            emitter.onNext("");
            emitter.onComplete();
        }).compose(RxSchedulersHelper.io_main())
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(activity)))
                .subscribe(pbo);
    }

}
