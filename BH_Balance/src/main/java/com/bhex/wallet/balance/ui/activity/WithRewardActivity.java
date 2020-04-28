package com.bhex.wallet.balance.ui.activity;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.wallet.balance.R;
import com.bhex.wallet.balance.R2;
import com.bhex.wallet.balance.adapter.DelegateValidatorAdapter;
import com.bhex.wallet.balance.model.DelegateValidator;
import com.bhex.wallet.balance.model.TxOrderItem;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.tx.TransactionOrder;

import java.util.List;

import butterknife.BindView;

/**
 *
 * 提取收益详情
 * @author gongdongyang
 * 2020-4-27 17:44:23
 * */

@Route(path = ARouterConfig.Balance_transcation_reward)
public class WithRewardActivity extends TxBaseActivity {

    @Autowired(name = "txo")
    public TxOrderItem txo;

    @BindView(R2.id.recycler_reward)
    RecyclerView recycler_reward;

    List<TxOrderItem.ActivitiesBean> mList;
    DelegateValidatorAdapter mdvAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_with_reward;
    }

    @Override
    protected void initView() {
        super.initView();
        ARouter.getInstance().inject(this);
        mtxo = txo;

        initBaseData();

        mList = txo.activities;
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_reward.setLayoutManager(lm);

        recycler_reward.setNestedScrollingEnabled(false);
        mdvAdapter = new DelegateValidatorAdapter(R.layout.layout_reward,mList);
        recycler_reward.setAdapter(mdvAdapter);
    }

    @Override
    protected void addEvent() {
        super.addEvent();
    }
}
