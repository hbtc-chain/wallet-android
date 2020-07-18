package com.bhex.wallet.bh_main.proposals.ui.activity;

import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.ViewModelProviders;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bhex.lib.uikit.widget.CustomTextView;
import com.bhex.lib.uikit.widget.editor.SimpleTextWatcher;
import com.bhex.lib.uikit.widget.editor.WithDrawInput;
import com.bhex.network.base.LoadDataModel;
import com.bhex.network.base.LoadingStatus;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.network.utils.ToastUtils;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.crypto.CryptoUtil;
import com.bhex.tools.utils.NumberUtil;
import com.bhex.tools.utils.ToolUtils;
import com.bhex.wallet.bh_main.R;
import com.bhex.wallet.bh_main.R2;
import com.bhex.wallet.bh_main.proposals.presenter.CreateProposalPresenter;
import com.bhex.wallet.bh_main.proposals.viewmodel.ProposalViewModel;
import com.bhex.wallet.bh_main.validator.enums.ENTRUST_BUSI_TYPE;
import com.bhex.wallet.bh_main.validator.presenter.DoEntrustPresenter;
import com.bhex.wallet.bh_main.validator.viewmodel.EnstrustViewModel;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.common.model.AccountInfo;
import com.bhex.wallet.common.model.ValidatorDelegationInfo;
import com.bhex.wallet.common.model.ValidatorInfo;
import com.bhex.wallet.common.tx.BHSendTranscation;
import com.bhex.wallet.common.tx.BHTransactionManager;
import com.bhex.wallet.common.ui.fragment.PasswordFragment;
import com.bhex.wallet.common.utils.LiveDataBus;
import com.bhex.wallet.common.viewmodel.BalanceViewModel;
import com.google.android.material.button.MaterialButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.math.BigInteger;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author zc
 * 2020-4-22
 * 发起提案
 */
@Route(path = ARouterConfig.Create_Proposal)
public class CreateProposalActivity extends BaseActivity<CreateProposalPresenter>  implements PasswordFragment.PasswordClickListener {

    @BindView(R2.id.ed_proposal_title)
    AppCompatEditText ed_proposal_title;

    @BindView(R2.id.ll_proposal_type)
    LinearLayout ll_proposal_type;

    @BindView(R2.id.ed_proposal_type)
    AppCompatEditText ed_proposal_type;


    @BindView(R2.id.ed_description)
    AppCompatEditText ed_description;

    @BindView(R2.id.tv_description_length)
    AppCompatTextView tv_description_length;

    @BindView(R2.id.tv_available_amount)
    CustomTextView tv_available_amount;

    @BindView(R2.id.ed_pledge_amount)
    WithDrawInput ed_pledge_amount;

    @BindView(R2.id.ed_fee)
    WithDrawInput ed_fee;


    @BindView(R2.id.btn_submit)
    MaterialButton btn_submit;

    @BindView(R2.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private String token = BHConstants.BHT_TOKEN;

    ProposalViewModel mProposalViewModel;
    private String available_amount;

    @Override
    protected void initPresenter() {
        mPresenter = new CreateProposalPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_create_proposal;
    }

    @Override
    protected void initView() {
        ARouter.getInstance().inject(this);
        initUI();
    }

    private void initUI() {
        refreshLayout.setEnableLoadMore(false);
        ed_fee.getEditText().setText(BHConstants.BHT_DEFAULT_FEE);
        tv_description_length.setText(getString(R.string.description_length_format,0));
        ed_pledge_amount.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        ed_fee.btn_right_text.setText(token.toUpperCase());
        ed_fee.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        tv_available_amount.setText(getString(R.string.available_format,getString(R.string.string_placeholder) + token.toUpperCase()));
    }

    @Override
    protected void addEvent() {
        mProposalViewModel = ViewModelProviders.of(this).get(ProposalViewModel.class);
        mProposalViewModel.createProposalLiveData.observe(this, ldm -> {
            updateCreateStatus(ldm);
        });
        ed_pledge_amount.btn_right_text.setOnClickListener(allListener);
        ed_description.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                tv_description_length.setText(getString(R.string.description_length_format,s.toString().length()));
            }
        });

        LiveDataBus.getInstance().with(BHConstants.Label_Account, LoadDataModel.class).observe(this, ldm->{
            refreshLayout.finishRefresh();
            if(ldm.loadingStatus==LoadingStatus.SUCCESS){
                updateAssets((AccountInfo) ldm.getData());
            }
        });

        queryAssetInfo(true);

        refreshLayout.setOnRefreshListener(refreshLayout1 -> {
            queryAssetInfo(false);
        });

        //返回隐藏键盘
        mToolBar.setNavigationOnClickListener(v -> {
            finish();
            ToolUtils.hintKeyBoard(this);
        });
    }

    private void queryAssetInfo(boolean isShowProgressDialog) {
        mProposalViewModel.getAccountInfo(this, isShowProgressDialog);
    }


    private void updateCreateStatus(LoadDataModel ldm) {
        if (ldm.loadingStatus == LoadingStatus.SUCCESS) {
            ToastUtils.showToast(getString(R.string.do_create_proposal_succeed));
            finish();
        } else {
            ToastUtils.showToast(getString(R.string.do_create_proposal_failed));
        }
    }

    @OnClick({R2.id.btn_submit})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.btn_submit) {
            sendSubmit();
        }
    }


    /**
     * 发送交易
     */
    private void sendSubmit() {
        boolean flag = mPresenter.checkCreateProposal(ed_proposal_title.getText().toString().trim(), ed_description.getText().toString().trim(),
                String.valueOf(available_amount),ed_pledge_amount.getInputString(),
                ed_fee.getInputString()
        );
        if (!flag) {
            return;
        }

        PasswordFragment.showPasswordDialog(getSupportFragmentManager(),
                PasswordFragment.class.getName(),
                this,0);
    }


    public View.OnClickListener allListener = v -> {
        if (available_amount == null || TextUtils.isEmpty(available_amount)) {
            ed_pledge_amount.getEditText().setText("");
            return;
        }
        String fee = ed_fee.getInputString();
        String all_count = NumberUtil.sub(String.valueOf(available_amount), fee);
        ed_pledge_amount.getEditText().setText(all_count);
    };

    private void updateAssets(AccountInfo data) {
        if (data == null) {
            return;
        }
        List<AccountInfo.AssetsBean> list = data.getAssets();
        if (list == null || list.size() == 0) {
            return;
        }
        for (AccountInfo.AssetsBean item : list) {
            if (item.getSymbol().equalsIgnoreCase(token)) {
                available_amount = mPresenter.getAmountForUser(item.getAmount(), item.getFrozen_amount(), token);
                tv_available_amount.setText(getString(R.string.available_format,available_amount + token.toUpperCase()));
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void confirmAction(String password, int position) {
        //String delegator_address = BHUserManager.getInstance().getCurrentBhWallet().getAddress();
        BigInteger gasPrice = BigInteger.valueOf((long) (BHConstants.BHT_GAS_PRICE));
        String amount = ed_pledge_amount.getInputString();
        String title = ed_proposal_title.getText().toString().trim();
        String desc = ed_description.getText().toString().trim();
        String feeAmount = ed_fee.getInputString();


        BHTransactionManager.loadSuquece(suquece -> {
            BHSendTranscation bhSendTranscation = BHTransactionManager.createProposal(BHConstants.TextProposalType,title,desc, amount, feeAmount,
                    gasPrice,password, suquece, token);
            mProposalViewModel.sendCreatePorposal(this, bhSendTranscation);
            return 0;
        });
    }
}
