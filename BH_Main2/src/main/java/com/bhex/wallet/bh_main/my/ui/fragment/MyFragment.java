package com.bhex.wallet.bh_main.my.ui.fragment;


import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bhex.lib.uikit.widget.RecycleViewDivider;
import com.bhex.lib.uikit.widget.util.ColorUtil;
import com.bhex.lib.uikit.widget.util.PixelUtils;
import com.bhex.network.mvx.base.BaseFragment;
import com.bhex.tools.constants.Constants;
import com.bhex.tools.utils.NavitateUtil;
import com.bhex.wallet.bh_main.R;
import com.bhex.wallet.bh_main.R2;
import com.bhex.wallet.bh_main.my.adapter.MyAdapter;
import com.bhex.wallet.bh_main.my.helper.MyHelper;
import com.bhex.wallet.bh_main.my.ui.MyRecyclerViewDivider;
import com.bhex.wallet.bh_main.my.ui.activity.SettingActivity;
import com.bhex.wallet.bh_main.my.ui.item.MyItem;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.db.entity.BHWallet;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.common.utils.ARouterUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author gdy
 * 2020-3-12
 * 我的
 */
public class MyFragment extends BaseFragment {


    @BindView(R2.id.recycler_my)
    RecyclerView recycler_my;

    @BindView(R2.id.tv_setting)
    AppCompatTextView tv_setting;


    @BindView(R2.id.tv_username)
    AppCompatTextView tv_username;


    @BindView(R2.id.tv_address)
    AppCompatTextView tv_address;

    private List<MyItem> mItems;

    private MyAdapter mMyAdapter;

    private BHWallet mBhWallet;

    public MyFragment() {
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_my;
    }

    @Override
    protected void initView() {
        mItems = MyHelper.getAllItems(getYActivity());
        mMyAdapter = new MyAdapter(R.layout.item_my, mItems);

        mBhWallet = BHUserManager.getInstance().getBhWallet();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getYActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_my.setLayoutManager(layoutManager);
        mMyAdapter.setHasStableIds(true);


        MyRecyclerViewDivider myRecyclerDivider = new MyRecyclerViewDivider(
                getYActivity(), DividerItemDecoration.VERTICAL,
                PixelUtils.dp2px(getYActivity(), 8), ColorUtil.getColor(getYActivity(), R.color.gray_f9f9fb)
        );

        /*RecycleViewDivider divider = new RecycleViewDivider(
                getYActivity(), 1, PixelUtils.dp2px(getYActivity(),20),
                getResources().getColor(R.color.line_color));*/

        recycler_my.addItemDecoration(myRecyclerDivider);

        recycler_my.setAdapter(mMyAdapter);

        tv_username.setText(mBhWallet.getName());
        tv_address.setText(mBhWallet.getAddress());

        MyHelper.proccessAddress(tv_address,mBhWallet.getAddress());
    }


    @Override
    protected void addEvent() {
        mMyAdapter.setOnItemClickListener((adapter, view, position) -> {
            switch (position){
                case 4:
                    //设置
                    NavitateUtil.startActivity(getYActivity(),SettingActivity.class);
                    break;
                case 0:
                    //
                    ARouterUtil.startActivity(ARouterConfig.MNEMONIC_BACKUP);

            }
        });
    }

    @OnClick({R2.id.tv_setting, R2.id.iv_default_man})
    public void onViewClicked(View view) {

        if(view.getId()==R.id.tv_setting){
            //NavitateUtil.startActivity(getYActivity(), SettingActivity.class);
            ARouterUtil.startActivity(ARouterConfig.MNEMONIC_TRUSTEESHIP_MANAGER_PAGE);
        }
    }
}
