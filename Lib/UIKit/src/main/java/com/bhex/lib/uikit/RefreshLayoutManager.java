package com.bhex.lib.uikit;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/4/16
 * Time: 1:22
 */
public class RefreshLayoutManager {

    public static void init(){
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
                //开始设置全局的基本参数（这里设置的属性只跟下面的MaterialHeader绑定，其他Header不会生效，能覆盖DefaultRefreshInitializer的属性和Xml设置的属性）
                layout.setEnableHeaderTranslationContent(false);
                return new MaterialHeader(context).setColorSchemeResources(R.color.refresh_cricle_color,
                        R.color.refresh_cricle_color,
                        R.color.refresh_cricle_color);

            }


        });

        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) -> {
            //layout.setEnableLoadMore(false);
            return new BallPulseFooter(context)
                    .setNormalColor(ContextCompat.getColor(context,R.color.blue_bg))
                    .setAnimatingColor(ContextCompat.getColor(context,R.color.blue_bg));
        });

    }
}
