package com.bhex.wallet.balance.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.core.LogisticsCenter;
import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bhex.lib.uikit.widget.RecycleViewExtDivider;
import com.bhex.lib.uikit.widget.text.marqueen.MarqueeFactory;
import com.bhex.lib.uikit.widget.text.marqueen.MarqueeView;
import com.bhex.lib_qr.XQRCode;
import com.bhex.lib_qr.util.QRCodeAnalyzeUtils;
import com.bhex.network.base.LoadDataModel;
import com.bhex.network.base.LoadingStatus;
import com.bhex.wallet.common.base.BaseFragment;
import com.bhex.network.utils.ToastUtils;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.utils.ColorUtil;
import com.bhex.tools.utils.LogUtils;
import com.bhex.tools.utils.PathUtils;
import com.bhex.tools.utils.PixelUtils;
import com.bhex.tools.utils.ToolUtils;
import com.bhex.wallet.balance.R;
import com.bhex.wallet.balance.R2;
import com.bhex.wallet.balance.adapter.AnnouncementMF;
import com.bhex.wallet.balance.adapter.ChainAdapter;
import com.bhex.wallet.balance.model.AnnouncementItem;
import com.bhex.wallet.balance.presenter.BalancePresenter;
import com.bhex.wallet.balance.ui.viewhodler.TipsViewHolder;
import com.bhex.wallet.balance.viewmodel.AnnouncementViewModel;
import com.bhex.wallet.common.cache.CacheCenter;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.db.entity.BHWallet;
import com.bhex.wallet.common.enums.BH_BUSI_TYPE;
import com.bhex.wallet.common.event.AccountEvent;
import com.bhex.wallet.common.event.CurrencyEvent;
import com.bhex.wallet.common.helper.AssetHelper;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.common.manager.CurrencyManager;
import com.bhex.wallet.common.manager.MainActivityManager;
import com.bhex.wallet.common.model.BHChain;
import com.bhex.wallet.common.ui.activity.BHQrScanActivity;
import com.bhex.wallet.common.utils.LiveDataBus;
import com.bhex.wallet.common.viewmodel.BalanceViewModel;
import com.google.android.material.textview.MaterialTextView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 资产首页
 *
 * @author gdy
 * 2020-3-12
 */
public class BalanceFragment extends BaseFragment<BalancePresenter> {

    @BindView(R2.id.tv_wallet_name)
    AppCompatTextView tv_wallet_name;
    @BindView(R2.id.tv_balance_txt2)
    MaterialTextView tv_balance_txt2;
    @BindView(R2.id.recycler_balance)
    RecyclerView recycler_balance;
    @BindView(R2.id.tv_address)
    AppCompatTextView tv_address;

    @BindView(R2.id.iv_eye)
    AppCompatImageView iv_eye;
    @BindView(R2.id.tv_asset)
    AppCompatTextView tv_asset;

    @BindView(R2.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R2.id.empty_layout)
    RelativeLayout empty_layout;

    @BindView(R2.id.layout_balance_tip)
    MarqueeView layout_balance_tip;


    private ChainAdapter mChainAdapter;
    private List<BHChain> mChainList;
    private BHWallet bhWallet;
    private BalanceViewModel balanceViewModel;
    private AnnouncementViewModel announcementViewModel;
    //总资产
    private double allTokenAssets;

    public BalanceFragment() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_balance;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        bhWallet = BHUserManager.getInstance().getCurrentBhWallet();
        //LogUtils.d("BalanceFragment===>:","=bhWallet="+bhWallet);
        String all_asset_label = getYActivity().getResources().getString(R.string.all_asset)+"("+CurrencyManager.getInstance().loadCurrency(getYActivity())+")";
        tv_balance_txt2.setText(all_asset_label);

        tv_wallet_name.setText("hello，"+bhWallet.name);

        mChainList = CacheCenter.getInstance().getTokenMapCache().loadChains();

        recycler_balance.getLayoutManager().setAutoMeasureEnabled(true);
        recycler_balance.setNestedScrollingEnabled(false);

        RecycleViewExtDivider ItemDecoration = new RecycleViewExtDivider(
                getContext(),LinearLayoutManager.VERTICAL,
                PixelUtils.dp2px(getYActivity(),68),0,
                ColorUtil.getColor(getContext(),R.color.global_divider_color));

        recycler_balance.addItemDecoration(ItemDecoration);
        recycler_balance.setAdapter(mChainAdapter = new ChainAdapter(mChainList));

        tv_address.setTag(bhWallet.getAddress());
        AssetHelper.proccessAddress(tv_address,bhWallet.getAddress());
        refreshLayout.setEnableLoadMore(false);

    }

    @Override
    protected void initPresenter() {
        mPresenter = new BalancePresenter(getYActivity());
    }

    @Override
    protected void addEvent() {
        //
        getYActivity().findViewById(R.id.iv_create_wallet).setOnClickListener(v->{
            ARouter.getInstance().build(ARouterConfig.Trusteeship.Trusteeship_Add_Index).withInt("flag",1).navigation();
        });

        //资产列表点击事件
        mChainAdapter.setOnItemClickListener((adapter, view, position) -> {
            BHChain bhChain =  mChainAdapter.getData().get(position);
            ARouter.getInstance().build(ARouterConfig.Balance.Balance_chain_tokens)
                    .withObject("bhChain",bhChain)
                    .withString("title","hbc")
                    .navigation();
        });

        balanceViewModel = ViewModelProviders.of(MainActivityManager._instance.mainActivity).get(BalanceViewModel.class).build(getYActivity());
        //资产订阅
        LiveDataBus.getInstance().with(BHConstants.Label_Account, LoadDataModel.class).observe(this, ldm->{
            refreshLayout.finishRefresh();
            if(ldm.loadingStatus==LoadingStatus.SUCCESS){
                updateAssets();
            }
        });

        //公告
        announcementViewModel = ViewModelProviders.of(this).get(AnnouncementViewModel.class);
        announcementViewModel.mutableLiveData.observe(this,ldm->{
            updateAnnouncement(ldm);
        });

        refreshLayout.setOnRefreshListener(refreshLayout1 -> {
            balanceViewModel.getAccountInfo(getYActivity(),null);
            announcementViewModel.loadAnnouncement(getYActivity());
        });
        refreshLayout.autoRefresh();
        getYActivity().findViewById(R.id.btn_transfer_in).setOnClickListener(this::onTransferAction);
        getYActivity().findViewById(R.id.btn_transfer_out).setOnClickListener(this::onTransferAction);
        getYActivity().findViewById(R.id.btn_entrust).setOnClickListener(this::onTransferAction);
        getYActivity().findViewById(R.id.iv_scan).setOnClickListener(v -> {
            Postcard postcard = ARouter.getInstance()
                    .build(ARouterConfig.Common.commom_scan_qr);
            LogisticsCenter.completion(postcard);
            Intent intent = new Intent(getActivity(), postcard.getDestination());
            intent.putExtras(postcard.getExtras());
            startActivityForResult(intent, BHQrScanActivity.REQUEST_CODE);
            //ARouter.getInstance().build(ARouterConfig.Common.commom_scan_qr).navigation(getYActivity(), BHQrScanActivity.REQUEST_CODE);
        });

        if(tv_address.getTag()!=null){
            getYActivity().findViewById(R.id.iv_qr_code).setOnClickListener(v->{
                AddressQRFragment.showFragment(getChildFragmentManager(),
                        AddressQRFragment.class.getSimpleName(),
                        BHConstants.BHT_TOKEN,
                        tv_address.getTag().toString());
            });
        }


    }

    //更新公告
    private void updateAnnouncement(LoadDataModel ldm) {
        if(ldm.loadingStatus != LoadingStatus.SUCCESS){
            return;
        }
        List<AnnouncementItem> list = (List<AnnouncementItem>)ldm.getData();
        MarqueeFactory<RelativeLayout, AnnouncementItem> marqueeFactory = new AnnouncementMF(getContext(),this::onCloseAction);
        marqueeFactory.setData(list);
        layout_balance_tip.setVisibility(View.VISIBLE);
        layout_balance_tip.setMarqueeFactory(marqueeFactory);
        layout_balance_tip.startFlipping();

        marqueeFactory.setOnItemClickListener((view, holder) -> {
            //holder.getData().
            ARouter.getInstance().build(ARouterConfig.Market.market_webview).withString("url",holder.getData().jump_url).navigation();
        });

    }

    private void onCloseAction() {
        layout_balance_tip.stopFlipping();
        layout_balance_tip.setVisibility(View.GONE);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //TipsViewHolder viewHolder = new TipsViewHolder();
        //viewHolder.initView(getYActivity(),getYActivity().findViewById(R.id.layout_balance_tip));
    }

    /**
     * 更新用户资产
     * @param
     */

    private void updateAssets() {
        String all_asset_label = getYActivity().getResources().getString(R.string.all_asset)+"("+CurrencyManager.getInstance().loadCurrency(getYActivity())+")";
        tv_balance_txt2.setText(all_asset_label);

        //计算每一个币种的资产价值 和 总资产
        updateTopTokenAssets();
        mChainAdapter.notifyDataSetChanged();
    }

    //更新头部资产
    public void updateTopTokenAssets(){
        if(BHUserManager.getInstance().getAccountInfo()==null){
            return;
        }
        allTokenAssets = mPresenter.calculateAllTokenPrice(getYActivity(),BHUserManager.getInstance().getAccountInfo(),mChainList);
        String allTokenAssetsText = CurrencyManager.getInstance().getCurrencyDecription(getYActivity(),allTokenAssets);
        //设置第一字符15sp
        String tag = iv_eye.getTag().toString();
        if(iv_eye.getTag().equals("0")){
            mPresenter.setTextFristSamll(tv_asset,allTokenAssetsText);
        }else{
            tv_asset.setText("***");
        }
    }

    //交易--转账、收款、委托
    private void onTransferAction(View view) {
        if(view.getId()==R.id.btn_transfer_in){
            ARouter.getInstance().build(ARouterConfig.Balance.Balance_transfer_in)
                    .withString("symbol", BHConstants.BHT_TOKEN)
                    .withInt("way", BH_BUSI_TYPE.链内转账.getIntValue())
                    .navigation();
        }else if(view.getId()==R.id.btn_transfer_out){
            ARouter.getInstance().build(ARouterConfig.Balance.Balance_transfer_out)
                    .withString("symbol", BHConstants.BHT_TOKEN)
                    .withInt("way", BH_BUSI_TYPE.链内转账.getIntValue())
                    .navigation();
        }else if(view.getId()==R.id.btn_entrust){
            ARouter.getInstance().build(ARouterConfig.Validator.Validator_Index)
                    .navigation();
        }
    }

    @OnClick({R2.id.iv_eye,R2.id.tv_address})
    public void onViewClicked(View view) {
        if(view.getId()==R.id.iv_eye){
            //隐藏资产
            mPresenter.hiddenAssetExt(getYActivity(),tv_asset,iv_eye);
            mChainAdapter.setIsHidden(iv_eye.getTag().toString());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeAccount(AccountEvent walletEvent){
        //当前钱包用户
        bhWallet = BHUserManager.getInstance().getCurrentBhWallet();
        AssetHelper.proccessAddress(tv_address,bhWallet.getAddress());
        //清空原始用户资产
        tv_wallet_name.setText("hello，"+bhWallet.name);
        mChainAdapter.notifyDataSetChanged();
        //更新资产
        balanceViewModel.getAccountInfo(getYActivity(),null);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            bhWallet = BHUserManager.getInstance().getCurrentBhWallet();
            tv_wallet_name.setText("hello，"+bhWallet.name);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeCurrency(CurrencyEvent currencyEvent){
        updateAssets();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //处理二维码扫描结果
        if (requestCode == BHQrScanActivity.REQUEST_CODE ) {
            if(resultCode == Activity.RESULT_OK){
                //处理扫描结果（在界面上显示）
                String qrCode  = data.getExtras().getString(XQRCode.RESULT_DATA);
                //mTransferOutViewHolder.input_to_address.setInputString(qrCode);
            }else if(resultCode == BHQrScanActivity.REQUEST_IMAGE){
                getAnalyzeQRCodeResult(data.getData());
            }
        }
    }


    private void getAnalyzeQRCodeResult(Uri uri) {
        XQRCode.analyzeQRCode(PathUtils.getFilePathByUri(getYActivity(), uri), new QRCodeAnalyzeUtils.AnalyzeCallback() {
            @Override
            public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                //mTransferOutViewHolder.input_to_address.setInputString(result);
            }

            @Override
            public void onAnalyzeFailed() {
                ToastUtils.showToast(getResources().getString(R.string.encode_qr_fail));
            }
        });
    }
}
