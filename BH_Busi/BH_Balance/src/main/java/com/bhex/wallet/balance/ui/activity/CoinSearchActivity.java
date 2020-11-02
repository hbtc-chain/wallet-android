package com.bhex.wallet.balance.ui.activity;

import android.text.Editable;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckedTextView;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
import com.bhex.lib.uikit.widget.EmptyLayout;
import com.bhex.lib.uikit.widget.RecycleViewExtDivider;
import com.bhex.lib.uikit.widget.editor.SimpleTextWatcher;
import com.bhex.network.base.LoadingStatus;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.tools.utils.ToolUtils;
import com.bhex.wallet.balance.R;
import com.bhex.wallet.balance.R2;
import com.bhex.wallet.balance.adapter.CoinSearchAdapter;
import com.bhex.wallet.balance.helper.CoinSearchHelper;
import com.bhex.wallet.balance.viewmodel.TokenViewModel;
import com.bhex.wallet.common.cache.SymbolCache;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.model.BHToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author gongdongyang
 * 2020-4-3 15:27:32
 * 币种搜索
 */
@Route(path = ARouterConfig.Balance_Search)
public class CoinSearchActivity extends BaseActivity implements OnRefreshListener {

    @BindView(R2.id.tv_center_title)
    AppCompatTextView tv_center_title;
    @BindView(R2.id.ed_search_content)
    AppCompatEditText ed_search_content;
    @BindView(R2.id.recycler_coin)
    RecyclerView recycler_coin;
    @BindView(R2.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R2.id.empty_layout)
    EmptyLayout empty_layout;

    @Autowired(name="chain")
    public String mChain;

    CoinSearchAdapter mCoinSearchAdapter;

    private List<BHToken> mTokenList;

    private TokenViewModel mTokenViewModel;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_coin_search;
    }

    @Override
    protected void initView() {
        tv_center_title.setText(getResources().getString(R.string.add_coin));
        ARouter.getInstance().inject(this);

        mTokenList = CoinSearchHelper.loadVerifiedToken(mChain);

        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.VERTICAL);

        mCoinSearchAdapter = new CoinSearchAdapter(mTokenList);
        recycler_coin.setLayoutManager(lm);
        recycler_coin.setAdapter(mCoinSearchAdapter);

        RecycleViewExtDivider ItemDecoration = new RecycleViewExtDivider(
                this,LinearLayoutManager.VERTICAL,
                PixelUtils.dp2px(this,68),
                0,
                ColorUtil.getColor(this,R.color.global_divider_color));
        recycler_coin.addItemDecoration(ItemDecoration);

        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setEnableLoadMore(false);
    }

    @Override
    protected void addEvent() {
        mTokenViewModel = ViewModelProviders.of(this).get(TokenViewModel.class);

        mTokenViewModel.searchLiveData.observe(this,ldm -> {
            if(ldm.loadingStatus == LoadingStatus.SUCCESS){
                updateTokenList((List<BHToken>)ldm.getData());
                return;
            }

            if(ldm.loadingStatus == LoadingStatus.ERROR){
                updateTokenList(null);
            }
        });

        mTokenViewModel.queryLiveData.observe(this,ldm->{
            if(ldm.loadingStatus == LoadingStatus.SUCCESS){
                ArrayMap<String,BHToken> arrayMap = SymbolCache.getInstance().getVerifiedToken();
                updateTokenList(new ArrayList<>(arrayMap.values()));
            }

            if(ldm.loadingStatus == LoadingStatus.ERROR){
                updateTokenList(null);
            }
            refreshLayout.finishRefresh();
        });

        mCoinSearchAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if(view.getId()==R.id.ck_select){
                BHToken bhToken = mTokenList.get(position);

                CheckedTextView ck = (CheckedTextView) view;
                ck.toggle();
                //添加取消币种
                mTokenViewModel.addOrCancelToken(ck.isChecked(),bhToken);
            }
        });
        ed_search_content.addTextChangedListener(searchTextWatcher);
        //refreshLayout.autoRefresh();
        ed_search_content.setOnEditorActionListener(onEditorActionListener);

    }

    private void updateTokenList(List<BHToken> data) {
        mCoinSearchAdapter.getData().clear();
        if(ToolUtils.checkListIsEmpty(data)){
            empty_layout.showNoData();
            mCoinSearchAdapter.notifyDataSetChanged();
            return;
        }
        empty_layout.loadSuccess();
        mCoinSearchAdapter.addData(data);
        mCoinSearchAdapter.notifyDataSetChanged();
    }

    /**
     * 搜索
     */
    private SimpleTextWatcher searchTextWatcher = new SimpleTextWatcher(){
        @Override
        public void afterTextChanged(Editable s) {
            super.afterTextChanged(s);
            String search_key = ed_search_content.getText().toString().trim();
            if(!TextUtils.isEmpty(search_key)){
                return;

            }
            mTokenList = CoinSearchHelper.loadVerifiedToken(mChain);
            if(ToolUtils.checkListIsEmpty(mTokenList)){
                empty_layout.showNoData();
            }else {
                empty_layout.loadSuccess();
            }
            mCoinSearchAdapter.getData().clear();
            mCoinSearchAdapter.addData(mTokenList);
            mCoinSearchAdapter.notifyDataSetChanged();
        }
    };


    TextView.OnEditorActionListener onEditorActionListener = (v, actionId, event) -> {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            searchTokenByName();
            ToolUtils.hintKeyBoard(CoinSearchActivity.this);
            return true;
        }
        return false;
    };

    //搜索节点
    private void searchTokenByName() {
        String search_key = ed_search_content.getText().toString().trim();
        if(!TextUtils.isEmpty(search_key)){
            empty_layout.showProgess();
            mTokenViewModel.search_token(this,search_key.toLowerCase(),mChain);
        }
    }


    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mTokenViewModel.loadVerifiedToken(this,mChain);
    }
}
