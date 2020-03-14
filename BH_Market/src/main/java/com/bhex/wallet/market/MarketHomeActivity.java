package com.bhex.wallet.market;

import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bhex.network.base.LoadDataModel;
import com.bhex.network.base.LoadingStatus;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.network.utils.ToastUtils;
import com.bhex.tools.utils.LogUtils;
import com.bhex.wallet.market.model.MarketAllEntity;
import com.bhex.wallet.market.viewmodel.MarketListViewModel;

import java.util.List;

import butterknife.BindView;

/**
 * Market主页
 * 2020-2-24 21:45:15
 */
public class MarketHomeActivity extends BaseActivity {

    MarketListViewModel marketListViewModel;

    @BindView(R2.id.btn_market_data)
    AppCompatButton btnMarketData;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_market_home;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void addEvent() {
        btnMarketData.setOnClickListener(v -> {
            marketListViewModel = ViewModelProviders.of(this).get(MarketListViewModel.class);

            marketListViewModel.marketLiveDatas.observe(this, new Observer<LoadDataModel>() {
                @Override
                public void onChanged(LoadDataModel loadDataModel) {
                    if(loadDataModel.loadingStatus==LoadingStatus.SUCCESS){
                        ToastUtils.showToast("成功!");
                    }else{
                        ToastUtils.showToast("失败");
                    }
                }
            });
            marketListViewModel.loadMarketAllData();
        });
    }

}
