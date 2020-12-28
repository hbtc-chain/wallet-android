package com.bhex.wallet.balance.presenter;

import android.content.Context;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.bhex.wallet.common.base.BaseActivity;
import com.bhex.wallet.common.base.BasePresenter;
import com.bhex.wallet.common.tx.TransactionOrder;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/4/14
 * Time: 17:09
 */
public class TranscationDetailPresenter extends BasePresenter {

    public TranscationDetailPresenter(BaseActivity activity) {
        super(activity);
    }

    public void setTitle(Context context, TransactionOrder txo,AppCompatTextView tv_center_title){
        TransactionOrder.ActivitiesBean activitiesBean = txo.activities.get(0);

    }
}
