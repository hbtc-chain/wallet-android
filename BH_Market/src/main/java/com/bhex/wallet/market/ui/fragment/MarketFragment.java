package com.bhex.wallet.market.ui.fragment;

import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.bhex.wallet.market.R;
import com.bhex.wallet.market.R2;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author gongdongyang
 * 2020-9-6 16:42:41
 */
public class MarketFragment extends BaseBowserFragment implements DexAuthorizeFragment.DexAuthorizeListener {

    @BindView(R2.id.iv_refresh)
    AppCompatImageView ivRefresh;
    @BindView(R2.id.tv_center_title)
    AppCompatTextView tv_center_title;
    //private boolean mFlag;

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
        //mFlag = MMKVManager.getInstance().mmkv().decodeBool("A",false);
        mAgentWeb.getUrlLoader().loadUrl("http://juswap.io/trade");
    }

    @Override
    protected void addEvent() {
        /*if(!mFlag){
            //DexAuthorizeFragment.showDialog(getChildFragmentManager(),DexAuthorizeFragment.class.getSimpleName(),this);
        }*/
        //ChooseWalletFragment.showDialog(getChildFragmentManager(),ChooseWalletFragment.class.getSimpleName());
        //PayDetailFragment.showDialog(getChildFragmentManager(),PayDetailFragment.class.getSimpleName());
    }

    @Override
    public void clickItem(int position) {
        if (position == 1) {
            //mAgentWeb.getUrlLoader().loadUrl("http://juswap.io");
        }
    }

    @OnClick({R2.id.iv_refresh})
    public void onClickView(View view) {
        if (R.id.iv_refresh == view.getId()) {
            mAgentWeb.getUrlLoader().loadUrl("http://juswap.io/trade");
        }
    }
}
