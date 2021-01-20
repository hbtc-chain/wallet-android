package com.bhex.wallet.mnemonic.ui.activity;

import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bhex.tools.utils.ColorUtil;
import com.bhex.wallet.common.base.BaseCacheActivity;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.enums.MAKE_WALLET_TYPE;
import com.bhex.wallet.common.manager.MainActivityManager;
import com.bhex.wallet.common.utils.ARouterUtil;
import com.bhex.wallet.mnemonic.R;
import com.bhex.wallet.mnemonic.R2;
import com.bhex.wallet.mnemonic.persenter.ImportPresenter;
import com.bhex.wallet.mnemonic.ui.AddWalletDecoration;
import com.bhex.wallet.mnemonic.ui.item.FunctionItem;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import butterknife.BindView;

/**
 * @author gdy
 * 2020-12-2 10:47:43
 */
@Route(
        path = ARouterConfig.Trusteeship.Trusteeship_Add_Index,
        name = "添加钱包"
)
public class AddWalletActivity extends BaseCacheActivity<ImportPresenter> {

    @Autowired (name="flag")
    int flag = 0;

    @BindView(R2.id.tv_center_title)
    AppCompatTextView tv_center_title;

    @BindView(R2.id.rec_function)
    RecyclerView rec_function;

    List<FunctionItem> mFunctionItemList;
    private AddWalletAdapter mAddWalletAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_wallet;
    }

    @Override
    protected void initView() {
        ARouter.getInstance().inject(this);
        String title = (flag==0)?getResources().getString(R.string.wallet_import_trusteeship)
                :getResources().getString(R.string.add_wallet);

        tv_center_title.setText(title);
        mFunctionItemList = (flag==0)?getPresenter().loadImportList():getPresenter().loadFunctionList();
        rec_function.setAdapter(mAddWalletAdapter = new AddWalletAdapter(mFunctionItemList));
        //添加分割线
        rec_function.addItemDecoration(new AddWalletDecoration(this,
                ColorUtil.getColor(this,R.color.app_bg),
                        new int[]{3}));

    }



    @Override
    protected void addEvent() {
        mAddWalletAdapter.setOnItemClickListener(this::onItemClick);
    }

    //添加点击事件
    private void onItemClick(BaseQuickAdapter<?,?> baseQuickAdapter, View view, int position) {
        FunctionItem item = mAddWalletAdapter.getData().get(position);
        switch (MAKE_WALLET_TYPE.getWay(item.index)){
            case 导入助记词:
                ARouter.getInstance().build(ARouterConfig.TRUSTEESHIP_IMPORT_MNEMONIC)
                        .navigation();
                //MainActivityManager.getInstance().setTargetClass(TrusteeshipManagerActivity.class);
                break;

            case 导入KS:
                ARouter.getInstance().build(ARouterConfig.TRUSTEESHIP_IMPORT_KEYSTORE)
                        .navigation();
                //MainActivityManager.getInstance().setTargetClass(TrusteeshipManagerActivity.class);
                break;

            case PK:
                ARouter.getInstance().build(ARouterConfig.TRUSTEESHIP_IMPORT_PRIVATEKEY)
                        .navigation();
                //MainActivityManager.getInstance().setTargetClass(TrusteeshipManagerActivity.class);
                break;

            case 创建助记词:
                ARouter.getInstance().build(ARouterConfig.TRUSTEESHIP_MNEMONIC_FRIST)
                        .navigation();
                //MainActivityManager.getInstance().setTargetClass(TrusteeshipManagerActivity.class);
                break;
        }
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ImportPresenter(this);
    }

    //添加钱包
    public class AddWalletAdapter extends BaseMultiItemQuickAdapter< FunctionItem, BaseViewHolder> {

        public AddWalletAdapter( @Nullable List<FunctionItem> data) {
            //super(R.layout.item_import_way, data);
            super(data);
            addItemType(FunctionItem.TYPE_ITEM,R.layout.item_import_way);
            addItemType(FunctionItem.TYPE_TITLE,R.layout.item_title);
        }

        @Override
        protected void convert(@NotNull BaseViewHolder viewHolder, FunctionItem functionItem) {
            switch (functionItem.getItemType()) {
                case FunctionItem.TYPE_ITEM:
                    viewHolder.setImageResource(R.id.iv_import_ic,functionItem.resId);
                    viewHolder.setText(R.id.tv_import_title,functionItem.title);
                    break;
                case FunctionItem.TYPE_TITLE:
                    viewHolder.setText(R.id.tv_title,functionItem.title);
                    break;
            }

        }
    }
}