package com.bhex.wallet.balance.ui.activity;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSON;
import com.bhex.network.base.LoadDataModel;
import com.bhex.network.base.LoadingStatus;
import com.bhex.network.utils.JsonUtils;
import com.bhex.wallet.balance.R;
import com.bhex.wallet.balance.R2;
import com.bhex.wallet.balance.adapter.TranscationAdapter;
import com.bhex.wallet.balance.helper.TransactionHelper;
import com.bhex.wallet.balance.model.TxOrderItem;
import com.bhex.wallet.balance.viewmodel.TransactionViewModel;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.tx.TransactionOrder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

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

    @Autowired(name = "transactionId")
    public String transactionId;

    @BindView(R2.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    List<TransactionOrder.ActivitiesBean> mList;
    TranscationAdapter mdvAdapter;

    TransactionViewModel transactionViewModel;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_with_reward;
    }

    @Override
    protected void initView() {
        super.initView();
        ARouter.getInstance().inject(this);
        //mtxo = txo;
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

        transactionViewModel = ViewModelProviders.of(this).get(TransactionViewModel.class);
        transactionViewModel.transLiveData.observe(this,ldm->{
            //更新交易详情
            updateTranscation(ldm);
        });


        refreshLayout.setOnRefreshListener(refreshLayout1 -> {
            transactionViewModel.queryTransactionDetail(this,transactionId);
        });
        refreshLayout.autoRefresh();
    }

    /**
     * 更新交易详情
     */
    private void updateTranscation(LoadDataModel ldm){
        refreshLayout.finishRefresh();
        if(ldm.loadingStatus == LoadingStatus.SUCCESS){
            mtxo = (TransactionOrder) ldm.getData();
            initBaseData();
            mList = mtxo.activities;
            mdvAdapter.getData().clear();
            mdvAdapter.addData(mtxo.activities);
            TransactionHelper.displayTranscationAmount(this, tv_tranction_amount,
                    mtxo.getActivities().get(0).getType(),
                    mtxo.getActivities().get(0).getValue().toString(),
                    JsonUtils.toJson(mtxo.getActivities()));
        }else if(ldm.loadingStatus == LoadingStatus.ERROR){

        }
    }
}
