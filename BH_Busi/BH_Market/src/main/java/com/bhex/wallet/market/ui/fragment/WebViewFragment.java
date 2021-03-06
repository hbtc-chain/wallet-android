package com.bhex.wallet.market.ui.fragment;

import android.animation.ObjectAnimator;
import android.view.View;
import android.webkit.WebView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.utils.LogUtils;
import com.bhex.tools.utils.ToolUtils;
import com.bhex.wallet.balance.R2;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.market.R;
import com.just.agentweb.WebChromeClient;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author gongdongyang
 * 2020年12月12日15:51:37
 */
@Route(path = ARouterConfig.Market.market_fragment_webview,name = "market-dex")
public class WebViewFragment extends JsBowserFragment {

    @BindView(R2.id.iv_back)
    AppCompatImageView iv_back;
    @BindView(R2.id.iv_close)
    AppCompatImageView iv_close;
    @BindView(R2.id.tv_center_title)
    AppCompatTextView tv_center_title;

    @Autowired(name = "url")
    public String url;


    @Override
    protected void initView() {
        ARouter.getInstance().inject(this);
        super.initView();
        iv_close.setVisibility(View.VISIBLE);
        iv_back.setVisibility(View.VISIBLE);
    }

    @Override
    public View getWebRootView() {
        return mRootView;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public View getBackView() {
        return iv_back;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_web_view;
    }

    @Override
    protected void addEvent() {

    }

    @Override
    protected WebChromeClient getWebChromeClient() {
        //return super.getWebChromeClient();
        return new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                callbackProgress(view,newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                //设置标题
                tv_center_title.setText(title);
                String url = view.getUrl();
            }
        };
    }

    @OnClick({R2.id.iv_refresh, R2.id.iv_back,R2.id.iv_close})
    public void onClickView(View view) {
        if (R.id.iv_refresh == view.getId()) {
            startRefreshAction(view);
            mAgentWeb.getUrlLoader().loadUrl(getUrl());
        } else if(R.id.iv_back == view.getId()){
            if(!mAgentWeb.back()){
                getActivity().finish();
            }
        } else if(R.id.iv_close == view.getId()){
            getActivity().finish();
        }
    }

    @Override
    protected void callbackProgress(WebView view, int newProgress) {
        if(newProgress==100 && objectAnimator!=null){
            objectAnimator.cancel();
            //mRootView.findViewById(R.id.iv_refresh).animate().rotation(0).setDuration(60).start();
        }
    }

    ObjectAnimator objectAnimator = null;
    private void startRefreshAction(View view) {
        //AppCompatImageView refreshView = mRootView.findViewById(R.id.iv_refresh);
        objectAnimator = ObjectAnimator.ofFloat(view,"rotation",0,360f)
                .setDuration(800);
        objectAnimator.setRepeatCount(-1);
        view.setPivotX(view.getWidth() / 2);
        view.setPivotY(view.getHeight()/ 2);
        objectAnimator.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ToolUtils.hintKeyBoard(getYActivity());
    }
}