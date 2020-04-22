package com.bhex.wallet.balance.ui.activity;

import android.text.Editable;
import android.text.TextUtils;
import android.widget.CheckedTextView;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bhex.lib.uikit.util.ColorUtil;
import com.bhex.lib.uikit.util.PixelUtils;
import com.bhex.lib.uikit.widget.RecycleViewDivider;
import com.bhex.lib.uikit.widget.RecycleViewExtDivider;
import com.bhex.lib.uikit.widget.editor.SimpleTextWatcher;
import com.bhex.network.base.LoadingStatus;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.tools.constants.BHConstants;
import com.bhex.wallet.balance.R;
import com.bhex.wallet.balance.R2;
import com.bhex.wallet.balance.adapter.CoinSearchAdapter;
import com.bhex.wallet.balance.event.BHCoinEvent;
import com.bhex.wallet.balance.model.BHTokenItem;
import com.bhex.wallet.balance.viewmodel.CoinViewModel;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.model.BHBalance;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * @author gongdongyang
 * 2020-4-3 15:27:32
 * 币种搜索
 */
@Route(path = ARouterConfig.Balance_Search)
public class CoinSearchActivity extends BaseActivity {

    @BindView(R2.id.tv_center_title)
    AppCompatTextView tv_center_title;
    @BindView(R2.id.ed_search_content)
    AppCompatEditText ed_search_content;
    @BindView(R2.id.recycler_coin)
    RecyclerView recycler_coin;

    @Autowired
    public List<BHBalance> balanceList;

    CoinSearchAdapter mCoinSearchAdapter;

    private List<BHTokenItem> coinList;

    private CoinViewModel mCoinViewModel;

    private Map<String, BHBalance> balanceMap = new HashMap<>();

    private List<BHTokenItem> originList;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_coin_search;
    }

    @Override
    protected void initView() {
        tv_center_title.setText(getResources().getString(R.string.add_coin));
        ARouter.getInstance().inject(this);
        for(BHBalance balance:balanceList){
            balanceMap.put(balance.symbol,balance);
        }
    }

    @Override
    protected void addEvent() {
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.VERTICAL);

        mCoinSearchAdapter = new CoinSearchAdapter(R.layout.item_seach_coin,coinList);
        recycler_coin.setLayoutManager(lm);
        recycler_coin.setAdapter(mCoinSearchAdapter);

        RecycleViewExtDivider ItemDecoration = new RecycleViewExtDivider(
                this,LinearLayoutManager.VERTICAL,
                PixelUtils.dp2px(this,68),
                0,
                ColorUtil.getColor(this,R.color.divider_line_color));
        recycler_coin.addItemDecoration(ItemDecoration);

        mCoinViewModel = ViewModelProviders.of(this).get(CoinViewModel.class);

        mCoinViewModel.coinLiveData.observe(this,listLoadDataModel -> {
            if(listLoadDataModel.loadingStatus != LoadingStatus.SUCCESS){
                return;
            }
            originList = listLoadDataModel.getData();
            //更新列表
            updateCoinList(listLoadDataModel.getData());

        });
        mCoinViewModel.loadCoin(this);

        mCoinSearchAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if(view.getId()==R.id.ck_select){
                CheckedTextView ck = (CheckedTextView) view;
                ck.setChecked(!ck.isChecked());
                BHTokenItem bhCoinItem = coinList.get(position);
                EventBus.getDefault().post(new BHCoinEvent(bhCoinItem,ck.isChecked()));

            }
        });
        ed_search_content.addTextChangedListener(searchTextWatcher);
    }

    /**
     * 获取数据源
     * @param data
     */
    private void updateCoinList(List<BHTokenItem> data) {
        if(data==null || data.size()==0){
            return;
        }

        for (int i = 0; i < data.size(); i++) {
           BHTokenItem coinItem = data.get(i);
           if(coinItem.symbol.equalsIgnoreCase("btc")){
               coinItem.resId = R.mipmap.ic_btc;
           }else if(coinItem.symbol.equalsIgnoreCase("eth")){
               coinItem.resId = R.mipmap.ic_eth;
           }else if(coinItem.symbol.equalsIgnoreCase("eos")){
               coinItem.resId = R.mipmap.ic_eos;
           }else if(coinItem.symbol.equalsIgnoreCase("tusdt")){
               coinItem.resId = R.mipmap.ic_usdt;
           }else if(coinItem.symbol.equalsIgnoreCase(BHConstants.BHT_TOKEN)){
               coinItem.resId = R.mipmap.ic_bht;
           }else{
               coinItem.resId = R.mipmap.ic_default_coin;
           }
           if(balanceMap.get(coinItem.symbol.toLowerCase())!=null){
               coinItem.isSelected = true;
           }else{
               coinItem.isSelected = false;
           }
        }
        mCoinSearchAdapter.getData().clear();
        coinList = data;
        mCoinSearchAdapter.addData(coinList);
    }

    /**
     * 搜索
     */
    private SimpleTextWatcher searchTextWatcher = new SimpleTextWatcher(){
        @Override
        public void afterTextChanged(Editable s) {
            super.afterTextChanged(s);
            String searhContent = ed_search_content.getText().toString().trim();

            if(originList==null || originList.size()==0 || TextUtils.isEmpty(searhContent)){
                return ;
            }
            List<BHTokenItem> resultList = new ArrayList<>();
            //coinList.clear();
            for(int i=0;i<originList.size();i++){
                BHTokenItem bhCoinItem = originList.get(i);
                if(bhCoinItem.symbol.toLowerCase().contains(searhContent.toLowerCase())){
                    resultList.add(bhCoinItem);
                }
            }

            updateCoinList(resultList);
        }
    };


}
