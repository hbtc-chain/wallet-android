package com.bhex.wallet.market.ui.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;

import com.bhex.network.mvx.base.BaseFragment;
import com.bhex.network.utils.JsonUtils;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.utils.LogUtils;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.market.R;
import com.bhex.wallet.market.model.H5Sign;
import com.bhex.wallet.market.wv.WVJBWebViewClient;
import com.just.agentweb.AbsAgentWebSettings;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.DefaultWebClient;
import com.just.agentweb.IAgentWebSettings;
import com.just.agentweb.WebChromeClient;

import java.util.HashMap;
import java.util.Map;

/**
 * @author gongdongyang
 * 2020-8-31 12:47:12
 */
public abstract class BaseBowserFragment extends BaseFragment {

    protected AgentWeb mAgentWeb;

    //private MiddlewareWebClientBase mMiddleWareWebClient;
    //private MiddlewareWebChromeBase mMiddleWareWebChrome;

    public abstract  View getWebRootView();

    @Override
    protected void initView() {

        mAgentWeb = AgentWeb.with(this)//
                .setAgentWebParent((LinearLayout) getWebRootView(), -1, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))//传入AgentWeb的父控件。
                .useDefaultIndicator(ContextCompat.getColor(getContext(),R.color.blue_bg), 3)//设置进度条颜色与高度，-1为默认值，高度为2，单位为dp。
                .setAgentWebWebSettings(getSettings())//设置 IAgentWebSettings。
                .setWebChromeClient(mWebChromeClient) //WebChromeClient
                .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK) //严格模式 Android 4.2.2 以下会放弃注入对象 ，使用AgentWebView没影响。
                .setMainFrameErrorView(R.layout.agentweb_error_page, -1) //参数1是错误显示的布局，参数2点击刷新控件ID -1表示点击整个布局都刷新， AgentWeb 3.0.0 加入。
                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)//打开其他页面时，弹窗质询用户前往其他应用 AgentWeb 3.0.0 加入。
                .createAgentWeb()//创建AgentWeb。
                .ready()//设置 WebSettings。
                .go(getUrl());
                //.get();

        mAgentWeb.getWebCreator().getWebView().setWebContentsDebuggingEnabled(true);
        WebSettings webSettings = mAgentWeb.getAgentWebSettings().getWebSettings();
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        mAgentWeb.getWebCreator().getWebView().setWebViewClient(getWebViewClient(mAgentWeb.getWebCreator().getWebView()));
        String ua = webSettings.getUserAgentString();
        webSettings.setUserAgentString(ua+";hbtcchainwallet");
    }

    //abstract public String getUrl();
    protected com.just.agentweb.WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }
    };


    public abstract String getUrl();


    public IAgentWebSettings getSettings() {
        AbsAgentWebSettings webSettings =  new AbsAgentWebSettings() {
            private AgentWeb mAgentWeb;
            @Override
            protected void bindAgentWebSupport(AgentWeb agentWeb) {
                this.mAgentWeb = agentWeb;
            }

            @Override
            public IAgentWebSettings toSetting(WebView webView) {
                IAgentWebSettings iAgentWebSettings = super.toSetting(webView);
                iAgentWebSettings.getWebSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
                return iAgentWebSettings;
            }
        };
        return  webSettings;
    }

    private MyWebViewClient getWebViewClient(WebView webView) {

        return new MyWebViewClient(webView);
    }

    class MyWebViewClient extends WVJBWebViewClient {
        public MyWebViewClient(WebView webView) {
            super(webView,((data, callback) -> {
                callback.callback("Response for message from ObjC!");
            }));

            registerHandler("get_account",(data,callback) -> {
                Map<String,String> map = new HashMap<>();
                map.put("address", BHUserManager.getInstance().getCurrentBhWallet().address);
                callback.callback(JsonUtils.toJson(map));
            });

            registerHandler("sign",(data, callback) -> {
                if(data!=null){
                    data = data.toString().replace("amount_int","amount_in")
                            .replace(":1",": \"1")
                            .replace("0,","0\",")
                            .replace("88425683849.54263","88425683849");
                    LogUtils.d("BaseBowserFragment==","=sign=data=="+data);
                    H5Sign h5Sign = JsonUtils.fromJson(data.toString(), H5Sign.class);
                    PayDetailFragment.newInstance().showDialog(getChildFragmentManager(),PayDetailFragment.class.getSimpleName(),h5Sign);
                }
            });
        }
    }

}