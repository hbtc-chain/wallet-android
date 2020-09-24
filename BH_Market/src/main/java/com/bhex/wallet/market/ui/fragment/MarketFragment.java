package com.bhex.wallet.market.ui.fragment;

import android.os.SystemClock;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.language.LocalManageUtil;
import com.bhex.wallet.market.R;
import com.bhex.wallet.market.R2;

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
        tv_center_title.setText(getString(R.string.tab_market));
        //mAgentWeb.getUrlLoader().loadUrl("http://juswap.io/trade");
    }

    @Override
    protected void addEvent() {
        /*if(!mFlag){
            //DexAuthorizeFragment.showDialog(getChildFragmentManager(),DexAuthorizeFragment.class.getSimpleName(),this);
        }*/
        //ChooseWalletFragment.showDialog(getChildFragmentManager(),ChooseWalletFragment.class.getSimpleName());
        /*H5Sign sign = new H5Sign();
        sign.type = TRANSCATION_BUSI_TYPE.添加流动性.getType();
        PayDetailFragment.newInstance().showDialog(getChildFragmentManager(),PayDetailFragment.class.getSimpleName(),sign);*/
    }

    @Override
    public String getUrl() {
        StringBuffer url = new StringBuffer(BHConstants.MARKET_URL);
        Locale locale = LocalManageUtil.getSetLanguageLocale(getActivity());
        long time = new Date().getTime();
        if(locale!=null){
            if(locale.getLanguage().contains("en")){
                url.append("?lang=en-us&t="+time);
            }else{
                url.append("?lang=zh-cn&t="+time);
            }
        }else{
            url.append("?lang=zh-cn&t="+time);
        }
        return url.toString();
    }

    @OnClick({R2.id.iv_refresh})
    public void onClickView(View view) {
        if (R.id.iv_refresh == view.getId()) {
            mAgentWeb.getUrlLoader().loadUrl(BHConstants.MARKET_URL);
        }
    }
}
