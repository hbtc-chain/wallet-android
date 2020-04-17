package com.bhex.wallet.bh_main.validator.adapter;

import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.utils.NumberUtil;
import com.bhex.wallet.bh_main.R;
import com.bhex.wallet.common.cache.CacheCenter;
import com.bhex.wallet.common.cache.SymbolCache;
import com.bhex.wallet.common.manager.CurrencyManager;
import com.bhex.wallet.common.model.BHBalance;
import com.bhex.wallet.common.model.BHToken;
import com.bhex.wallet.common.model.ValidatorInfo;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by BHEX.
 * User: zhouchang
 * Date: 2020/4/14
 */
public class ValidatorAdapter extends BaseQuickAdapter<ValidatorInfo, BaseViewHolder> {

    private int mValid;
    public ValidatorAdapter(int isValid,int layoutResId, @Nullable List<ValidatorInfo> data) {
        super(layoutResId, data);
        mValid = isValid;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder viewHolder, @Nullable ValidatorInfo validatorInfo) {
        viewHolder.setText(R.id.tv_validator_name,validatorInfo.getDescription().getMoniker());
        if (mValid == BHConstants.VALIDATOR_VALID) {
            viewHolder.setTextColor(R.id.tv_validator_name, getContext().getResources().getColor(R.color.main_text_black));
            viewHolder.setImageResource(R.id.iv_status, R.mipmap.icon_validator_valid);
        } else {
            viewHolder.setTextColor(R.id.tv_validator_name, getContext().getResources().getColor(R.color.dark_blue));
            viewHolder.setImageResource(R.id.iv_status, R.mipmap.icon_validator_invalid);
        }

        viewHolder.setText(R.id.tv_voting_power_proportion, validatorInfo.getVoting_power_proportion() + "%");
        viewHolder.setText(R.id.tv_self_delegate_proportion,validatorInfo.getSelf_delegate_proportion() + "%");
        viewHolder.setText(R.id.tv_other_delegate_proportion,TextUtils.isEmpty(validatorInfo.getOther_delegate_proportion())? "":validatorInfo.getOther_delegate_proportion() + "%");

    }

}
