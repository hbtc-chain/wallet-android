package com.bhex.wallet.market.ui.fragment;

import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.ViewModelProviders;

import com.alibaba.fastjson.JSONObject;
import com.bhex.network.base.LoadDataModel;
import com.bhex.network.base.LoadingStatus;
import com.bhex.network.exception.ExceptionEngin;
import com.bhex.network.utils.JsonUtils;
import com.bhex.network.utils.ToastUtils;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.language.LocalManageUtil;
import com.bhex.tools.utils.LogUtils;
import com.bhex.wallet.balance.viewmodel.TransactionViewModel;
import com.bhex.wallet.common.tx.BHSendTranscation;
import com.bhex.wallet.common.tx.BHTransactionManager;
import com.bhex.wallet.market.R;
import com.bhex.wallet.market.R2;
import com.bhex.wallet.market.event.H5SignEvent;
import com.bhex.wallet.market.model.H5Sign;
import com.bhex.wallet.market.wv.WVJBWebViewClient;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author gongdongyang
 * 2020-9-6 16:42:41
 */
public class MarketFragment extends BaseBowserFragment  {

    @BindView(R2.id.iv_refresh)
    AppCompatImageView ivRefresh;
    @BindView(R2.id.tv_center_title)
    AppCompatTextView tv_center_title;
    private H5Sign mH5Sign;

    private TransactionViewModel transactionViewModel;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_market;
    }

    @Override
    public View getWebRootView() {
        return mRootView;
    }

    @Override
    protected void initView() {
        super.initView();
        tv_center_title.setText(getString(R.string.tab_trade));

        transactionViewModel = ViewModelProviders.of(this).get(TransactionViewModel.class);
        transactionViewModel.mutableLiveData.observe(this,ldm -> {
            updateTransferStatus(ldm);
        });
    }

    @Override
    protected void addEvent() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public String getUrl() {
        StringBuffer url = new StringBuffer(BHConstants.MARKET_URL);
        Locale locale = LocalManageUtil.getSetLanguageLocale(getActivity());
        if(locale!=null){
            if(locale.getLanguage().contains("en")){
                url.append("?lang=en-us");
            }else{
                url.append("?lang=zh-cn");
            }
        }else{
            url.append("?lang=zh-cn");
        }
        return url.toString();
    }

    @OnClick({R2.id.iv_refresh})
    public void onClickView(View view) {
        if (R.id.iv_refresh == view.getId()) {
            mAgentWeb.getUrlLoader().loadUrl(getUrl());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void signMessage(H5SignEvent h5SignEvent){
        mH5Sign = h5SignEvent.h5Sign;
        /*BHTransactionManager.loadSuquece(suquece -> {
            BHSendTranscation bhSendTranscation = BHTransactionManager.create_dex_transcation(h5SignEvent.h5Sign.type,h5SignEvent.h5Sign.value,suquece,h5SignEvent.data);
            transactionViewModel.sendTransaction(getActivity(),bhSendTranscation);
            return 0;
        });*/
        transactionViewModel.create_dex_transcation(getYActivity(),h5SignEvent.h5Sign.type,h5SignEvent.h5Sign.value,h5SignEvent.data);
    }
    private void updateTransferStatus(LoadDataModel ldm) {

        if(mH5Sign==null || TextUtils.isEmpty(mH5Sign.type)) {
            return;
        }

        WVJBWebViewClient.WVJBResponseCallback callback = callbackMaps.get(mH5Sign.type);
        if(callback==null){
            return;
        }

        DexResponse<JSONObject> dexResponse = new DexResponse(ldm.code,ldm.msg);
        callback.callback(JsonUtils.toJson(dexResponse));
        callbackMaps.remove(mH5Sign.type);
    }
}
