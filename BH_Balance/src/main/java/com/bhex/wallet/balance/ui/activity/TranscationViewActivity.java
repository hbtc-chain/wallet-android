package com.bhex.wallet.balance.ui.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSON;
import com.bhex.wallet.balance.R;
import com.bhex.wallet.balance.adapter.TranscationAdapter;
import com.bhex.wallet.balance.helper.TransactionHelper;
import com.bhex.wallet.balance.model.TxOrderItem;
import com.bhex.wallet.common.config.ARouterConfig;

import java.util.List;

import butterknife.BindView;

/**
 *
 * 提取收益详情
 * @author gongdongyang
 * 2020-4-27 17:44:23
 * */

@Route(path = ARouterConfig.Balance_transcation_view)
public class TranscationViewActivity extends TxBaseActivity {

    @Autowired(name = "txo")
    public TxOrderItem txo;

    List<TxOrderItem.ActivitiesBean> mList;
    TranscationAdapter mdvAdapter;

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

        TransactionHelper.displayTranscationAmount(this, tv_tranction_amount,
                txo.activities.get(0).type,
                txo.value,
                JSON.toJSONString(txo.activities));


        mList = txo.activities;
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_reward.setLayoutManager(lm);

        recycler_reward.setNestedScrollingEnabled(false);
        mdvAdapter = new TranscationAdapter(R.layout.layout_reward,mList);
        recycler_reward.setAdapter(mdvAdapter);
    }

    @Override
    protected void addEvent() {
        super.addEvent();
    }
}
