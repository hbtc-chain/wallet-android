package com.bhex.wallet.balance.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bhex.lib.uikit.util.ColorUtil;
import com.bhex.lib.uikit.util.PixelUtils;
import com.bhex.lib.uikit.widget.RecycleViewDivider;
import com.bhex.lib.uikit.widget.RecycleViewExtDivider;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.utils.LogUtils;
import com.bhex.tools.utils.ShapeUtils;
import com.bhex.wallet.balance.R;
import com.bhex.wallet.balance.R2;
import com.bhex.wallet.balance.adapter.BalanceAdapter;
import com.bhex.wallet.balance.helper.BHBalanceHelper;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.model.BHBalance;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author gongdongyang
 * 2020-8-31 10:57:14
 */
@Route(path = ARouterConfig.Balance_chain_tokens, name = "链下Token")
public class ChainTokenActivity extends BaseActivity {

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
    BalanceAdapter mBalanceAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chain_token;
    }

    @Override
    protected void initView() {
        ARouter.getInstance().inject(this);
        tv_center_title.setText(mBalance.symbol.toUpperCase());
        layout_index_1.setBackground(ShapeUtils.getRoundRectDrawable(
                10, ColorUtil.getColor(this, R.color.blue_bg),
                true, 0));
        tv_token_name.setText(mBalance.symbol.toUpperCase());

        if (BHConstants.BHT_TOKEN.equalsIgnoreCase(mBalance.chain)) {
            tv_token_address.setText(mBalance.address);
        } else  {
            tv_token_address.setText(mBalance.external_address);
        }
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
    }

    @OnClick({R2.id.tv_token_address, R2.id.iv_token_qr})
    public void onViewClicked(View view) {

    }

}