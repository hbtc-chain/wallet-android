package com.bhex.wallet.balance.ui.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bhex.lib.uikit.util.ColorUtil;
import com.bhex.lib.uikit.util.PixelUtils;
import com.bhex.lib.uikit.widget.RecycleViewExtDivider;
import com.bhex.network.base.LoadDataModel;
import com.bhex.network.base.LoadingStatus;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.network.utils.ToastUtils;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.utils.LogUtils;
import com.bhex.tools.utils.ShapeUtils;
import com.bhex.tools.utils.ToolUtils;
import com.bhex.wallet.balance.R;
import com.bhex.wallet.balance.R2;
import com.bhex.wallet.balance.adapter.BalanceAdapter;
import com.bhex.wallet.balance.helper.BHBalanceHelper;
import com.bhex.wallet.balance.presenter.BalancePresenter;
import com.bhex.wallet.balance.viewmodel.BalanceViewModel;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.db.entity.BHWallet;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.common.model.AccountInfo;
import com.bhex.wallet.common.model.BHBalance;
import com.bhex.wallet.common.utils.LiveDataBus;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author gongdongyang
 * 2020-8-31 10:57:14
 */
@Route(path = ARouterConfig.Balance_chain_tokens, name = "链下Token")
public class ChainTokenActivity extends BaseActivity<BalancePresenter> {

    @Autowired(name = "balance")
    BHBalance mBalance;

    @BindView(R2.id.tv_center_title)
    AppCompatTextView tv_center_title;
    @BindView(R2.id.layout_index_1)
    RelativeLayout layout_index_1;
    @BindView(R2.id.tv_token_name)
    AppCompatTextView tv_token_name;
    @BindView(R2.id.tv_token_address)
    AppCompatTextView tv_token_address;
    @BindView(R2.id.iv_token_qr)
    AppCompatImageView iv_token_qr;
    @BindView(R2.id.rcv_token_list)
    RecyclerView rcv_token_list;
    @BindView(R2.id.iv_paste)
    AppCompatImageView iv_paste;

    BalanceAdapter mBalanceAdapter;

    private BHWallet mCurrentWallet;
    private BalanceViewModel balanceViewModel;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chain_token;
    }

    @Override
    protected void initView() {
        ARouter.getInstance().inject(this);
        mCurrentWallet = BHUserManager.getInstance().getCurrentBhWallet();

        tv_center_title.setText(mBalance.symbol.toUpperCase());
        layout_index_1.setBackground(ShapeUtils.getRoundRectDrawable(
                10, ColorUtil.getColor(this, R.color.blue_bg),
                true, 0));
        tv_token_name.setText(mBalance.symbol.toUpperCase());

        if (BHConstants.BHT_TOKEN.equalsIgnoreCase(mBalance.chain)) {
            tv_token_address.setText(mBalance.address);
        } else  {
            if(!TextUtils.isEmpty(mBalance.external_address)){
                tv_token_address.setText(mBalance.external_address);
            }else{
                tv_token_address.setText(mCurrentWallet.address);
            }
        }

        iv_paste.setOnClickListener(v -> {
            ToolUtils.copyText(tv_token_address.getText().toString(),this);
            ToastUtils.showToast(getString(R.string.copyed));
        });
    }

    @Override
    protected void addEvent() {
        List<BHBalance> balanceList = BHBalanceHelper.loadBalanceByChain(mBalance.chain);
        //LogUtils.e("ChainTokenActivity===>:","size=="+balanceList.size());
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        RecycleViewExtDivider itemDecoration = new RecycleViewExtDivider(
                this,LinearLayoutManager.VERTICAL,
                PixelUtils.dp2px(this,68),0,
                ColorUtil.getColor(this,R.color.global_divider_color));
        rcv_token_list.addItemDecoration(itemDecoration);

        rcv_token_list.setLayoutManager(llm);
        mBalanceAdapter = new BalanceAdapter(balanceList);
        rcv_token_list.setAdapter(mBalanceAdapter);

        mBalanceAdapter.setOnItemClickListener((adapter, view, position) -> {
            BHBalance bhBalance = mBalanceAdapter.getData().get(position);
            ARouter.getInstance().build(ARouterConfig.Balance_Token_Detail)
                    .withObject("balance",bhBalance)
                    .navigation();
        });

        balanceViewModel = ViewModelProviders.of(this).get(BalanceViewModel.class).build(this);
        //资产订阅
        LiveDataBus.getInstance().with(BHConstants.Label_Account, LoadDataModel.class).observe(this, ldm->{
            //refreshLayout.finishRefresh();
            if(ldm.loadingStatus== LoadingStatus.SUCCESS){
                updateAssets((AccountInfo) ldm.getData());
            }
        });
        balanceViewModel.getAccountInfo(this);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new BalancePresenter(this);
    }

    @OnClick({R2.id.tv_token_address, R2.id.iv_token_qr})
    public void onViewClicked(View view) {

    }


    private void updateAssets(AccountInfo accountInfo) {
        BHUserManager.getInstance().setAccountInfo(accountInfo);
        mPresenter.calculateAllTokenPrice(accountInfo,mBalanceAdapter.getData());
        mBalanceAdapter.notifyDataSetChanged();
    }

}