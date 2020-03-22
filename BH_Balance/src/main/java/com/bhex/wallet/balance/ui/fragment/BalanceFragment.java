package com.bhex.wallet.balance.ui.fragment;


import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bhex.lib.uikit.widget.RecycleViewDivider;
import com.bhex.lib.uikit.widget.util.ColorUtil;
import com.bhex.lib.uikit.widget.util.PixelUtils;
import com.bhex.network.mvx.base.BaseFragment;
import com.bhex.network.utils.ToastUtils;
import com.bhex.tools.utils.ToolUtils;
import com.bhex.wallet.balance.R;
import com.bhex.wallet.balance.R2;
import com.bhex.wallet.balance.adapter.BalanceAdapter;
import com.bhex.wallet.balance.model.Balance;
import com.bhex.wallet.balance.presenter.BalancePresenter;
import com.bhex.wallet.common.db.entity.BHWallet;
import com.bhex.wallet.common.helper.AssetHelper;
import com.bhex.wallet.common.manager.BHUserManager;
import com.google.android.material.textview.MaterialTextView;
import com.yanzhenjie.recyclerview.SwipeMenu;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

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

    @BindView(R2.id.recycler_balance)
    SwipeRecyclerView recycler_balance;

    @BindView(R2.id.tv_address)
    AppCompatTextView tv_address;

    @BindView(R2.id.iv_eye)
    AppCompatImageView iv_eye;

    @BindView(R2.id.tv_asset)
    MaterialTextView tv_asset;


    private BalanceAdapter mBalanceAdapter;

    private List<Balance> mBalanceList;

    private BHWallet bhWallet;

    public BalanceFragment() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_balance;
    }

    @Override
    protected void initView() {
        bhWallet = BHUserManager.getInstance().getCurrentBhWallet();
        mBalanceList = mPresenter.makeBalanceList();


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_balance.setLayoutManager(layoutManager);

        recycler_balance.setSwipeMenuCreator(swipeMenuCreator);

        RecycleViewDivider ItemDecoration = new RecycleViewDivider(
                getContext(),LinearLayoutManager.VERTICAL,
                1,
                ColorUtil.getColor(getContext(),R.color.gray_E7ECF4));

        recycler_balance.addItemDecoration(ItemDecoration);
        //gray_f9f9fb
        //recycler_balance.addItemDecoration(divider);

        mBalanceAdapter = new BalanceAdapter(R.layout.item_balance, mBalanceList);
        recycler_balance.setAdapter(mBalanceAdapter);

        AssetHelper.proccessAddress(tv_address,bhWallet.getAddress());

    }


    @Override
    protected void initPresenter() {
        mPresenter = new BalancePresenter(getYActivity());
    }

    @Override
    protected void addEvent() {
        mBalanceAdapter.setOnItemClickListener((adapter, view, position) -> {

        });
    }


    @OnClick({R2.id.iv_eye,R2.id.tv_address})
    public void onViewClicked(View view) {
        if(view.getId()==R.id.iv_eye){
            String tag = (String) view.getTag();
            AppCompatImageView iv = (AppCompatImageView)view;
            if(tag.equals("0")){
                tv_asset.setText("*******");
                view.setTag("1");
                iv.setImageDrawable(getResources().getDrawable(R.mipmap.ic_eye_close));
            }else{
                tv_asset.setText("123.45678901");
                view.setTag("0");
                iv.setImageDrawable(getResources().getDrawable(R.mipmap.ic_eye));
            }
        }else if(view.getId()==R.id.tv_address){
            ToolUtils.copyText(bhWallet.getAddress(),getYActivity());
            ToastUtils.showToast(getResources().getString(R.string.copyed));
        }
    }


    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu leftMenu, SwipeMenu rightMenu, int position) {
            int width = PixelUtils.dp2px(getContext(),80);
            int height = ViewGroup.LayoutParams.MATCH_PARENT;

            SwipeMenuItem transferItem = new SwipeMenuItem(getContext())
                    .setBackground(R.drawable.btn_0_blue)
                    .setText(getResources().getString(R.string.transfer))
                    .setTextColor(Color.WHITE)
                    .setWidth(width)
                    .setHeight(height);
            rightMenu.addMenuItem(transferItem);


            SwipeMenuItem makeCollectItem = new SwipeMenuItem(getContext())
                    .setBackground(R.drawable.btn_0_blue)
                    .setText(getResources().getString(R.string.make_collection))
                    .setTextColor(Color.WHITE)
                    .setWidth(width)
                    .setHeight(height);
            leftMenu.addMenuItem(makeCollectItem);

        }
    };
}
