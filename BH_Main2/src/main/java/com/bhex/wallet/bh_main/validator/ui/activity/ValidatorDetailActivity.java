package com.bhex.wallet.bh_main.validator.ui.activity;

import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bhex.lib.uikit.widget.CustomTextView;
import com.bhex.lib.uikit.widget.EmptyLayout;
import com.bhex.lib.uikit.widget.balance.CoinBottomBtn;
import com.bhex.network.base.LoadingStatus;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.utils.DateUtil;
import com.bhex.wallet.bh_main.R;
import com.bhex.wallet.bh_main.R2;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.common.model.AccountInfo;
import com.bhex.wallet.common.model.BHBalance;
import com.bhex.wallet.common.model.ValidatorInfo;
import com.bhex.wallet.common.tx.TransactionOrder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author zc
 * 2020-4-16
 * 验证节点
 */
@Route(path = ARouterConfig.Validator_Detail)
public class ValidatorDetailActivity extends BaseActivity {

    @Autowired(name = "validatorInfo")
    ValidatorInfo mValidatorInfo;
    @Autowired(name = "valid")
    int mValid;
    @BindView(R2.id.tv_validator_name)
    AppCompatTextView tv_validator_name;
    @BindView(R2.id.tv_voting_power_proportion)
    CustomTextView tv_voting_power_proportion;
    @BindView(R2.id.tv_self_delegate_proportion)
    CustomTextView tv_self_delegate_proportion;
    @BindView(R2.id.tv_up_time)
    CustomTextView tv_up_time;
    @BindView(R2.id.tv_other_delegate_proportion)
    CustomTextView tv_other_delegate_proportion;
    @BindView(R2.id.tv_update_time)
    CustomTextView tv_update_time;
    @BindView(R2.id.tv_max_rate)
    CustomTextView tv_max_rate;
    @BindView(R2.id.tv_max_change_rate)
    CustomTextView tv_max_change_rate;
    @BindView(R2.id.tv_address_value)
    CustomTextView tv_address_value;
    @BindView(R2.id.tv_website_value)
    AppCompatTextView tv_website_value;
    @BindView(R2.id.tv_detail_value)
    AppCompatTextView tv_detail_value;
    @BindView(R2.id.iv_status)
    AppCompatImageView iv_status;
    @BindView(R2.id.tv_status)
    AppCompatTextView tv_status;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_validator_detail;
    }

    @Override
    protected void initView() {

        ARouter.getInstance().inject(this);
        initValidatorView();
    }


    private void initValidatorView() {
        if(mValidatorInfo==null)
            return;
        if (mValidatorInfo.getDescription()!=null) {
            tv_validator_name.setText(mValidatorInfo.getDescription().getMoniker());
            tv_website_value.setText(mValidatorInfo.getDescription().getWebsite());
        }
        if (mValid== BHConstants.VALIDATOR_VALID) {
            iv_status.setImageResource(R.mipmap.icon_validator_valid);
            tv_status.setText(R.string.tab_valid);
            tv_status.setTextColor(getResources().getColor(R.color.color_green));
        } else {
            iv_status.setImageResource(R.mipmap.icon_validator_invalid);
            tv_status.setText(R.string.tab_invalid);
            tv_status.setTextColor(getResources().getColor(R.color.dark_black));
        }

        tv_voting_power_proportion.setText(TextUtils.isEmpty(mValidatorInfo.getVoting_power_proportion())? "":mValidatorInfo.getVoting_power_proportion() + "%");
        tv_self_delegate_proportion.setText(TextUtils.isEmpty(mValidatorInfo.getSelf_delegate_proportion())? "":mValidatorInfo.getSelf_delegate_proportion() + "%");
        tv_other_delegate_proportion.setText(TextUtils.isEmpty(mValidatorInfo.getOther_delegate_proportion())? "":mValidatorInfo.getVoting_power_proportion() + "%");
        tv_up_time.setText(TextUtils.isEmpty(mValidatorInfo.getUp_time())? "":mValidatorInfo.getUp_time() + "%");

        String tv_time = DateUtil.transTimeWithPattern(mValidatorInfo.getLast_voted_time()*1000,"yyyy/MM/dd hh:mm:ss aa z");
        tv_update_time.setText(tv_time);
        if (mValidatorInfo.getCommission()!=null) {
            tv_max_rate.setText(TextUtils.isEmpty(mValidatorInfo.getCommission().getMax_rate()) ? "" : mValidatorInfo.getCommission().getMax_rate() + "%");
            tv_max_change_rate.setText(TextUtils.isEmpty(mValidatorInfo.getCommission().getMax_change_rate()) ? "" : mValidatorInfo.getCommission().getMax_change_rate() + "%");
        }
        tv_address_value.setText(mValidatorInfo.getOperator_address());
    }

    @Override
    protected void addEvent() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R2.id.iv_copy, R2.id.btn_transfer_entrust, R2.id.btn_relieve_entrust, R2.id.btn_do_entrust})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.iv_copy) {

        } else if (view.getId() == R.id.btn_transfer_entrust) {

        } else if (view.getId() == R.id.btn_relieve_entrust) {

        } else if (view.getId() == R.id.btn_do_entrust) {

        }
    }

}
