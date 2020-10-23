package com.bhex.wallet.common.browse;

import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;

import com.just.agentweb.MiddlewareWebClientBase;

/**
 *
 */

public class MiddlewareWebViewClient extends MiddlewareWebClientBase {

    public MiddlewareWebViewClient() {

    }

    private static int count = 1;

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        return super.shouldOverrideUrlLoading(view, request);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return super.shouldOverrideUrlLoading(view, url);

    }
}
