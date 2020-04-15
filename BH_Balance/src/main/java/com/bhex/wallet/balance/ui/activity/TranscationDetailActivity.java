package com.bhex.wallet.balance.ui.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.tools.utils.DateUtil;
import com.bhex.tools.utils.ToolUtils;
import com.bhex.wallet.balance.R;
import com.bhex.wallet.balance.R2;
import com.bhex.wallet.balance.helper.TransactionHelper;
import com.bhex.wallet.balance.model.TxOrderItem;
import com.bhex.wallet.balance.presenter.TranscationDetailPresenter;
import com.bhex.wallet.common.config.ARouterConfig;
import com.hjq.toast.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author gongdongyang
 * 转委托界面
 * 2020-4-8 11:21:16
 */
@Route(path = ARouterConfig.Balance_transcation_detail)
public class TranscationDetailActivity extends BaseActivity<TranscationDetailPresenter> {

    @BindView(R2.id.tv_center_title)
    AppCompatTextView tv_center_title;
    @BindView(R2.id.tv_tranction_amount)
    AppCompatTextView tv_tranction_amount;
    @BindView(R2.id.tv_tranction_hash)
    AppCompatTextView tv_tranction_hash;
    @BindView(R2.id.tv_transcation_status)
    AppCompatTextView tv_transcation_status;
    @BindView(R2.id.tv_transcation_time)
    AppCompatTextView tv_transcation_time;
    @BindView(R2.id.iv_txid_paste)
    AppCompatImageView iv_txid_paste;
    @BindView(R2.id.tv_from)
    AppCompatTextView tv_from;
    @BindView(R2.id.tv_to)
    AppCompatTextView tv_to;

    @Autowired(name = "txo")
    TxOrderItem txo;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_transcation;
    }

    @Override
    protected void initView() {
        ARouter.getInstance().inject(this);

        String tx_type = TransactionHelper.getTranscationType(this, txo.getActivities().get(0).type);

        tv_center_title.setText(tx_type);

        tv_tranction_hash.setText(txo.hash);

        TransactionHelper.setTranscationStatusExt(this, txo.success, tv_transcation_status);
        String tv_time = DateUtil.transTimeWithPattern(txo.time * 1000, DateUtil.DATA_TIME_STYLE);
        tv_transcation_time.setText(tv_time);

        //tv_tranction_amount.setText();

        TransactionHelper.displayTranscationAmount(this, tv_tranction_amount,
                txo.activities.get(0).type,
                txo.value);

        TransactionHelper.displayTranscationFromTo(this, tv_from, tv_to,
                txo.activities.get(0).type,
                txo.value);
    }

    @Override
    protected void addEvent() {

    }


    @OnClick({R2.id.iv_txid_paste, R2.id.iv_from_paste, R2.id.iv_to_paste})
    public void onViewClicked(View view) {
        String text = "";
        if (view.getId() == R.id.iv_txid_paste) {
            text = tv_tranction_hash.getText().toString();
        }else if(view.getId() == R.id.iv_to_paste){
            text = tv_to.getText().toString();
        }else if(view.getId() == R.id.iv_from_paste){
            text = tv_from.getText().toString();
        }

        ToolUtils.copyText(text, this);
        ToastUtils.show(getResources().getString(R.string.copyed));
    }

}
