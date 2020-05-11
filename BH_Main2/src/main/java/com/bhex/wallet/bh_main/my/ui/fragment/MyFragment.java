package com.bhex.wallet.bh_main.my.ui.fragment;


import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bhex.lib.uikit.util.ColorUtil;
import com.bhex.lib.uikit.util.PixelUtils;
import com.bhex.network.base.LoadDataModel;
import com.bhex.network.base.LoadingStatus;
import com.bhex.network.mvx.base.BaseFragment;
import com.bhex.network.utils.ToastUtils;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.utils.LogUtils;
import com.bhex.tools.utils.MD5;
import com.bhex.tools.utils.NavitateUtil;
import com.bhex.tools.utils.ToolUtils;
import com.bhex.wallet.bh_main.R;
import com.bhex.wallet.bh_main.R2;
import com.bhex.wallet.bh_main.my.adapter.MyAdapter;
import com.bhex.wallet.bh_main.my.helper.MyHelper;
import com.bhex.wallet.bh_main.my.ui.MyRecyclerViewDivider;
import com.bhex.wallet.bh_main.my.ui.activity.SettingActivity;
import com.bhex.wallet.bh_main.my.ui.item.MyItem;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.db.entity.BHWallet;
import com.bhex.wallet.common.event.WalletEvent;
import com.bhex.wallet.common.helper.AssetHelper;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.common.model.AccountInfo;
import com.bhex.wallet.common.ui.fragment.PasswordFragment;
import com.bhex.wallet.common.utils.ARouterUtil;
import com.bhex.wallet.common.utils.LiveDataBus;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author gdy
 * 2020-3-12
 * 我的
 */
public class MyFragment extends BaseFragment implements PasswordFragment.PasswordClickListener {

    @BindView(R2.id.recycler_my)
    RecyclerView recycler_my;

    @BindView(R2.id.tv_setting)
    AppCompatTextView tv_setting;

    @BindView(R2.id.tv_username)
    AppCompatTextView tv_username;

    @BindView(R2.id.tv_address)
    AppCompatTextView tv_address;

    @BindView(R2.id.iv_paste)
    AppCompatImageView iv_paste;

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

        mBhWallet = BHUserManager.getInstance().getCurrentBhWallet();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getYActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_my.setLayoutManager(layoutManager);
        mMyAdapter.setHasStableIds(true);


        MyRecyclerViewDivider myRecyclerDivider = new MyRecyclerViewDivider(
                getYActivity(), DividerItemDecoration.VERTICAL,
                PixelUtils.dp2px(getYActivity(), 8), ColorUtil.getColor(getYActivity(), R.color.global_divider_color)
        );

        recycler_my.addItemDecoration(myRecyclerDivider);

        recycler_my.setAdapter(mMyAdapter);

        tv_username.setText(mBhWallet.getName());
        //tv_address.setText(mBhWallet.getAddress());

        AssetHelper.proccessAddress(tv_address,mBhWallet.getAddress());
    }


    @Override
    protected void addEvent() {
        EventBus.getDefault().register(this);
        mMyAdapter.setOnItemClickListener((adapter, view, position) -> {
            MyItem item = mItems.get(position);
            switch (item.id){
                case 0:
                    PasswordFragment.showPasswordDialog(getChildFragmentManager(),
                            PasswordFragment.class.getName(),
                            MyFragment.this,position);
                    break;
                case 1:
                    //ARouterUtil.startActivity(ARouterConfig.MY_UPDATE_PASSWORD);
                    ARouter.getInstance().build(ARouterConfig.MY_UPDATE_PASSWORD).withString("title",item.title).navigation();
                    break;
                case 4:
                    //设置
                    NavitateUtil.startActivity(getYActivity(),SettingActivity.class);
                    break;

            }
        });

        //助记词备份订阅
        LiveDataBus.getInstance().with(BHConstants.Label_Mnemonic_Back, LoadDataModel.class).observe(this, ldm->{
            if(ldm.loadingStatus== LoadingStatus.SUCCESS){
                //更新item列表
                mItems = MyHelper.getAllItems(getYActivity());
                mMyAdapter.getData().clear();
                mMyAdapter.addData(mItems);
            }
        });
    }

    @OnClick({R2.id.tv_setting, R2.id.iv_default_man,R2.id.tv_address,R2.id.iv_paste})
    public void onViewClicked(View view) {
        if(view.getId()==R.id.tv_setting){
            ARouterUtil.startActivity(ARouterConfig.MNEMONIC_TRUSTEESHIP_MANAGER_PAGE);
        }else if(view.getId()==R.id.iv_paste){
            ToolUtils.copyText(mBhWallet.getAddress(),getYActivity());
            ToastUtils.showToast(getResources().getString(R.string.copyed));
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeAccount(WalletEvent walletEvent){
        mBhWallet = BHUserManager.getInstance().getCurrentBhWallet();
        tv_username.setText(mBhWallet.getName());
        MyHelper.proccessAddress(tv_address,mBhWallet.getAddress());

        mItems = MyHelper.getAllItems(getYActivity());
        mMyAdapter.getData().clear();
        mMyAdapter.addData(mItems);
        LogUtils.d("MyFragment===>:","mItems=="+mItems.size());
        //mMyAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void confirmAction(String password,int position) {
        if(TextUtils.isEmpty(password)){
            ToastUtils.showToast(getResources().getString(R.string.please_input_password));
            return;
        }

        if(!MD5.md5(password).equals(mBhWallet.getPassword())){
            ToastUtils.showToast(getResources().getString(R.string.error_password));
            return;
        }

        //备份助记词
        if(position==0){
            ARouterUtil.startActivity(ARouterConfig.MNEMONIC_BACKUP);
        }

    }
}
