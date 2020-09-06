package com.bhex.wallet.market.ui.fragment;

import android.view.View;

import androidx.fragment.app.DialogFragment;

import com.bhex.tools.utils.LogUtils;
import com.bhex.wallet.common.manager.MMKVManager;
import com.bhex.wallet.market.R;

/**
 * @author gongdongyang
 * 2020-9-6 16:42:41
 */
public class MarketFragment extends BaseBowserFragment implements DexAuthorizeFragment.DexAuthorizeListener {
    private boolean mFlag;



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
        mFlag = MMKVManager.getInstance().mmkv().decodeBool("A",false);
        if(mFlag){
            mAgentWeb.getUrlLoader().loadUrl("https://www.baidu.com");
        }
    }

    @Override
    protected void addEvent() {
        if(!mFlag){
            DexAuthorizeFragment.showDialog(getChildFragmentManager(),DexAuthorizeFragment.class.getSimpleName(),this);
        }
    }

    @Override
    public void clickItem(int position) {
        if(position==1){
            mAgentWeb.getUrlLoader().loadUrl("https://www.baidu.com");
        }
    }
}
