package com.bhex.wallet.bh_main.validator.ui.activity;

import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bhex.lib.uikit.widget.CustomTextView;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.utils.DateUtil;
import com.bhex.wallet.bh_main.R;
import com.bhex.wallet.bh_main.R2;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.model.ValidatorInfo;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author zc
 * 2020-4-16
 * 委托
 */
@Route(path = ARouterConfig.Do_Entrust)
public class DoEntrustActivity extends BaseActivity {

    @Autowired(name = "validatorInfo")
    ValidatorInfo mValidatorInfo;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_do_entrust;
    }

    @Override
    protected void initView() {

        ARouter.getInstance().inject(this);
        initValidatorView();
    }


    private void initValidatorView() {
        if(mValidatorInfo==null)
            return;
    }

    @Override
    protected void addEvent() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public void onViewClicked(View view) {

    }

}
