package com.bhex.wallet.balance.ui.activity;

import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bhex.wallet.common.base.BaseActivity;
import com.bhex.network.mvx.base.IPresenter;
import com.bhex.tools.utils.DateUtil;
import com.bhex.tools.utils.LogUtils;
import com.bhex.tools.utils.ToolUtils;
import com.bhex.wallet.balance.R;
import com.bhex.wallet.balance.R2;
import com.bhex.wallet.balance.helper.TransactionHelper;
import com.bhex.wallet.balance.model.TxOrderItem;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.enums.TRANSCATION_BUSI_TYPE;
import com.bhex.wallet.common.tx.TransactionOrder;
import com.hjq.toast.ToastUtils;

import butterknife.BindView;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/4/27
 * Time: 17:46
 */
public abstract class TxBaseActivity<T extends IPresenter> extends BaseActivity<T> {

    //public TransactionOrder mtxo;
    public TransactionOrder mtxo;

    @BindView(R2.id.tv_center_title)
    public AppCompatTextView tv_center_title;
    @BindView(R2.id.tv_tranction_amount)
    public AppCompatTextView tv_tranction_amount;
    @BindView(R2.id.tv_tranction_hash)
    public AppCompatTextView tv_tranction_hash;
    @BindView(R2.id.tv_transcation_status)
    public AppCompatTextView tv_transcation_status;
    @BindView(R2.id.tv_transcation_time)
    public AppCompatTextView tv_transcation_time;
    @BindView(R2.id.iv_txid_paste)
    public AppCompatImageView iv_txid_paste;

    @BindView(R2.id.iv_ibc_txid_paste)
    AppCompatImageView iv_ibc_txid_paste;
    @BindView(R2.id.tv_ibc_txhash)
    AppCompatTextView tv_ibc_txhash;

    @BindView(R2.id.recycler_reward)
    RecyclerView recycler_reward;

    @Override
    protected void initView() {
        tv_center_title.setText("");
    }

    public void initBaseData(){
        String tx_type = TransactionHelper.getTranscationType(this, mtxo);
        tv_center_title.setText(tx_type);
        tv_tranction_hash.setText(mtxo.hash);
        tv_ibc_txhash.setText(mtxo.ibc_tx_hash);

        TransactionHelper.setTranscationStatusExt(this, mtxo, tv_transcation_status);
        String tv_time = DateUtil.transTimeWithPattern(mtxo.time * 1000, DateUtil.DATA_TIME_STYLE);
        tv_transcation_time.setText(tv_time);

        if(TextUtils.isEmpty(mtxo.ibc_tx_hash)){
            findViewById(R.id.layout_index_0).setVisibility(View.GONE);
        }


        /*if(TransactionHelper.transactionIsProccess(tx_type)){
            recycler_reward.setVisibility(View.VISIBLE);
        }else{
            recycler_reward.setVisibility(View.GONE);
        }*/
    }

    @Override
    protected void addEvent() {
        iv_txid_paste.setOnClickListener(v -> {
            String text = tv_tranction_hash.getText().toString();
            ToolUtils.copyText(text, this);
            ToastUtils.show(getResources().getString(R.string.copyed));
        });

        iv_ibc_txid_paste.setOnClickListener(v->{
            String text = tv_ibc_txhash.getText().toString();
            ToolUtils.copyText(text, this);
            ToastUtils.show(getResources().getString(R.string.copyed));
        });
    }
}
