package com.bhex.wallet.mnemonic.ui.activity;


import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bhex.lib.uikit.widget.RecycleViewDivider;
import com.bhex.tools.utils.ColorUtil;
import com.bhex.tools.utils.PixelUtils;
import com.bhex.wallet.common.base.BaseCacheActivity;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.mnemonic.R;
import com.bhex.wallet.mnemonic.R2;
import com.bhex.wallet.mnemonic.adapter.ImportAdapter;
import com.bhex.wallet.mnemonic.persenter.ImportPresenter;
import com.bhex.wallet.mnemonic.ui.item.FunctionItem;

import java.util.List;

import butterknife.BindView;

/**
 * @author gongdongyang
 * 托管单元导入首页
 * 2020-3-18 16:35:22
 */
@Route(path = ARouterConfig.TRUSTEESHIP_IMPORT_INDEX)
public class ImportIndexActivity extends BaseCacheActivity<ImportPresenter> {

    @BindView(R2.id.tv_center_title)
    AppCompatTextView tv_center_title;

    @BindView(R2.id.recycler_import_way)
    RecyclerView recycler_import_way;

    private ImportAdapter importAdapter;

    private List<FunctionItem> mDatas;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_import_index;
    }

    @Override
    protected void initView() {
        tv_center_title.setText(getResources().getString(R.string.wallet_import_trusteeship));
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ImportPresenter(this);
    }

    @Override
    protected void addEvent() {

        mDatas = mPresenter.loadImportList();
        recycler_import_way.setAdapter( importAdapter = new ImportAdapter(mDatas));

        RecycleViewDivider divider = new RecycleViewDivider(this, LinearLayoutManager.VERTICAL,
                PixelUtils.dp2px(this,16), ColorUtil.getColor(this,R.color.app_bg));
        recycler_import_way.addItemDecoration(divider);

        importAdapter.setOnItemClickListener((adapter, view, position) -> {
            switch (position){
                case 0:
                    ARouter.getInstance().build(ARouterConfig.TRUSTEESHIP_IMPORT_MNEMONIC)
                            .navigation();
                    break;
                case 1:
                    ARouter.getInstance().build(ARouterConfig.TRUSTEESHIP_IMPORT_KEYSTORE)
                            .navigation();
                    break;
                case 2:
                    ARouter.getInstance().build(ARouterConfig.TRUSTEESHIP_IMPORT_PRIVATEKEY)
                            .navigation();
                    break;
            }
        });
    }

}
