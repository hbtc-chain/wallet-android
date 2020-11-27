package com.bhex.wallet.bh_main.my.ui.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bhex.lib.uikit.widget.EmptyLayout;
import com.bhex.lib.uikit.widget.GradientTabLayout;
import com.bhex.network.base.LoadDataModel;
import com.bhex.network.base.LoadingStatus;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.tools.utils.LogUtils;
import com.bhex.wallet.bh_main.R;
import com.bhex.wallet.bh_main.R2;
import com.bhex.wallet.bh_main.my.adapter.MessageAdapter;
import com.bhex.wallet.bh_main.my.model.BHMessage;
import com.bhex.wallet.bh_main.my.ui.fragment.MessageListFragment;
import com.bhex.wallet.bh_main.my.viewmodel.MessageViewModel;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.enums.BH_BUSI_TYPE;
import com.bhex.wallet.common.model.BHPage;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author gongdongyang
 * 2020-5-16 16:14:15
 * 消息中心
 */
@Route(path = ARouterConfig.My.My_Message, name = "消息中心")
public class MessageActivity extends BaseActivity {

    @BindView(R2.id.tv_center_title)
    AppCompatTextView tv_center_title;
    @BindView(R2.id.tab)
    GradientTabLayout tab;

    @BindView(R2.id.viewPager)
    ViewPager viewPager;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_message;
    }

    @Override
    protected void initView() {
        tv_center_title.setText(getResources().getString(R.string.message_center));

        List<Pair<String, Fragment>> items = new ArrayList<>();
        //文本导出
        MessageListFragment txMessageFragment = new MessageListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(MessageListFragment.Message_Type,BH_BUSI_TYPE.转账通知.value);
        txMessageFragment.setArguments(bundle);
        items.add(new Pair<>(getResources().getString(R.string.transfer_notification),txMessageFragment));

        //二维码导出
        MessageListFragment notificationFragment = new MessageListFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putString(MessageListFragment.Message_Type,BH_BUSI_TYPE.系统通知.value);
        notificationFragment.setArguments(bundle2);
        items.add(new Pair<>(getResources().getString(R.string.system_message),notificationFragment));

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return items.get(position).second;
            }

            @Override
            public int getCount() {
                return items.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {//添加标题Tab
                return items.get(position).first;
            }
        });

        tab.setViewPager(viewPager);


    }

    @Override
    protected void addEvent() {

    }

    public GradientTabLayout getTab() {
        return tab;
    }
}
