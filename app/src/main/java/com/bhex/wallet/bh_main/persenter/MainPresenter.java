package com.bhex.wallet.bh_main.persenter;

import android.Manifest;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;

import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.network.mvx.base.BasePresenter;
import com.bhex.tools.constants.Constants;
import com.bhex.tools.utils.LogUtils;
import com.bhex.tools.utils.NavitateUtil;
import com.bhex.wallet.R;
import com.bhex.wallet.balance.ui.fragment.BalanceFragment;
import com.bhex.wallet.bh_main.exchange.ui.fragment.ExchangeFragment;
import com.bhex.wallet.bh_main.my.ui.fragment.MyFragment;
import com.bhex.wallet.bh_main.order.ui.fragment.OrderFragment;
import com.bhex.wallet.mnemonic.ui.activity.BackupMnemonicActivity;
import com.bhex.wallet.mnemonic.ui.fragment.SecureTipsFragment;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * created by gongdongyang
 * on 2020/2/26
 */
public class MainPresenter extends BasePresenter {

    public MainPresenter(BaseActivity activity) {
        super(activity);
    }


    @Override
    public void onCreate(@NotNull LifecycleOwner owner) {
        super.onCreate(owner);
        //requestPermissions();
        String isBackUp = getActivity().getIntent().getStringExtra(Constants.BACKUP_TEXT);
        //LogUtils.d("MainPresenter==>:","isBackUp:"+isBackUp);
        if(Constants.LATER_BACKUP.equals(isBackUp)){
            SecureTipsFragment.showDialog(getActivity().getSupportFragmentManager(),"");
        }
    }

    @Override
    public void onResume(@NotNull LifecycleOwner owner) {
        super.onResume(owner);


    }

    /**
     * 动态权限申请
     */
    public void requestPermissions() {
        RxPermissions rxPermission = new RxPermissions(getActivity());
        rxPermission
                .requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            // 用户已经同意该权限
                            LogUtils.d(permission.name + " is granted.");
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                            LogUtils.d(permission.name + " is denied. More info should be provided.");
                        } else {
                            // 用户拒绝了该权限，并且选中『不再询问』
                            LogUtils.d(permission.name + " is denied.");
                        }
                    }
                });
    }


    public void goBalanceFragment(){
        setCurrentFragment(BalanceFragment.class, null);
    }


    public void goExchangeFragment() {
        setCurrentFragment(ExchangeFragment.class, null);
    }

    public void goOrderFragment() {
        setCurrentFragment(OrderFragment.class,null);
    }


    public void goMyFragment() {
        setCurrentFragment(MyFragment.class, null);
    }


    private void setCurrentFragment(Class fragmentClass, Bundle bundle) {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        Fragment dstFragment = manager.findFragmentByTag(fragmentClass.getSimpleName());

        List<Fragment> fragments = manager.getFragments();

        FragmentTransaction transaction = manager.beginTransaction();

        if (fragments != null)
            for (Fragment frg : fragments)
                transaction.hide(frg);

        if (dstFragment == null) {
            dstFragment = Fragment.instantiate(getActivity(), fragmentClass.getName(), bundle);
            transaction.add(R.id.fl_content, dstFragment, fragmentClass.getSimpleName()).commitAllowingStateLoss();
        } else {
            transaction.show(dstFragment).commitAllowingStateLoss();
        }

    }
}
