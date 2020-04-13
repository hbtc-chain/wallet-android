package com.bhex.network.mvx.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bhex.network.R;
import com.bhex.network.receiver.NetWorkStatusChangeReceiver;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.language.LocalManageUtil;
import com.bhex.tools.utils.LogUtils;
import com.bhex.tools.utils.StatusBarUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * created by gongdongyang
 * on 2020/2/24
 */
public abstract class BaseCoreActivity<T extends IPresenter> extends AppCompatActivity {

    protected final static String TAG = BaseCoreActivity.class.getSimpleName();

    public Toolbar mToolBar;

    private Unbinder mUnbinder;

    private NetWorkStatusChangeReceiver netWorkChangReceiver;

    private boolean isRegistered = false;

    protected T mPresenter;

    protected ProgressDialog mProgressDialog;

    private int dialogRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        //StatusBarUtil.setStatusColor(this,false,false,R.color.blue);
        setStatusColor();
        initPresenter();


        mUnbinder = ButterKnife.bind(this);

        initReceiver();
        initToolbar();
        initView();
        addEvent();
    }

    protected  void setStatusColor(){
        if(getStatusColorValue()== BHConstants.STATUS_COLOR_WHITE){
            StatusBarUtil.setStatusColor(this,false,true,R.color.white);
            //ImmersionBar.with(this).statusBarColor(R.color.white).statusBarDarkFont(true).reset().init();
        }else if(getStatusColorValue()== BHConstants.STATUS_COLOR_BLUE){
            StatusBarUtil.setStatusColor(this,false,false,R.color.blue);
            //ImmersionBar.with(this).statusBarColor(R.color.blue).statusBarDarkFont(false).reset().init();
        }else{
            StatusBarUtil.setStatusColor(this,false,true,R.color.white);
            //ImmersionBar.with(this).statusBarColor(R.color.white).statusBarDarkFont(true).reset().init();
        }
    }

    protected  int getStatusColorValue(){
        return BHConstants.STATUS_COLOR_WHITE;
    }

    protected  void initPresenter(){

    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract void addEvent();

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalManageUtil.attachBaseContext(newBase,""));
    }

    private void initReceiver() {
        netWorkChangReceiver = new NetWorkStatusChangeReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netWorkChangReceiver, filter);
        isRegistered = true;
    }

    private void initToolbar(){
        mToolBar = findViewById(R.id.toolbar);
        if(mToolBar!=null){
            mToolBar.setTitle("");
            setSupportActionBar(mToolBar);
            getSupportActionBar().setHomeButtonEnabled(isShowBacking());
            getSupportActionBar().setDisplayHomeAsUpEnabled(isShowBacking());
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mUnbinder!=null){
            mUnbinder.unbind();
        }

        //解绑
        if (isRegistered) {
            unregisterReceiver(netWorkChangReceiver);
        }
    }

    protected boolean isShowBacking() {
        return true;
    }

    /**
     * 显示对话框
     */
    public void showProgressbarLoading() {
        showProgressbarLoading("",getResources().getString(R.string.http_loading));
    }

    public void showProgressbarLoading(String title, String hint) {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            return;
        this.mProgressDialog = ProgressDialog.show(this, title, hint);
        this.mProgressDialog.setCancelable(true);
        this.mProgressDialog.setCanceledOnTouchOutside(false);
    }

    /**
     * 隐藏对话框
     */
    public void dismissProgressDialog() {
        if (mProgressDialog == null)
            return;

        if (mProgressDialog.isShowing()  && --dialogRef <= 0){
            this.mProgressDialog.dismiss();
        }

    }


    public T getPresenter() {
        return mPresenter;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
