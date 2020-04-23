package com.bhex.wallet.mnemonic.ui.activity;


import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bhex.lib.uikit.util.ColorUtil;
import com.bhex.lib.uikit.util.PixelUtils;
import com.bhex.lib.uikit.widget.RecycleViewDivider;
import com.bhex.wallet.common.base.BaseCacheActivity;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.utils.ARouterUtil;
import com.bhex.wallet.mnemonic.R;
import com.bhex.wallet.mnemonic.R2;
import com.bhex.wallet.mnemonic.adapter.ImportAdapter;
import com.bhex.wallet.mnemonic.persenter.ImportPresenter;
import com.bhex.wallet.mnemonic.ui.item.ImportItem;

import java.util.List;

import butterknife.BindView;

/**
 * @author gongdongyang
 * 托管单元导入首页
 * 2020-3-18 16:35:22
 */
@Route(path = ARouterConfig.TRUSTEESHIP_IMPORT_INDEX)
public class ImportIndexActivity extends BaseCacheActivity<ImportPresenter> {

    /*@BindView(R2.id.btn_import_mnemonic)
    MaterialButton btn_import_mnemonic;
    @BindView(R2.id.btn_import_ks)
    MaterialButton btn_import_ks;
    @BindView(R2.id.btn_import_private)
    MaterialButton btn_import_private;*/
    @BindView(R2.id.tv_center_title)
    AppCompatTextView tv_center_title;

    @BindView(R2.id.recycler_import_way)
    RecyclerView recycler_import_way;

    private ImportAdapter importAdapter;

    private List<ImportItem> mDatas;

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

        mDatas = mPresenter.loadAllItem();

        importAdapter = new ImportAdapter(R.layout.item_import_way,mDatas);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.VERTICAL);

        recycler_import_way.setLayoutManager(lm);

        recycler_import_way.setAdapter(importAdapter);

        RecycleViewDivider divider = new RecycleViewDivider(this, LinearLayoutManager.VERTICAL,
                PixelUtils.dp2px(this,16), ColorUtil.getColor(this,R.color.main_backgound));

        recycler_import_way.addItemDecoration(divider);

        importAdapter.setOnItemClickListener((adapter, view, position) -> {
            switch (position){
                case 0:
                    ARouterUtil.startActivity(ARouterConfig.TRUSTEESHIP_MNEMONIC_IMPORT);
                    break;
                case 1:
                    break;
                case 2:
                    ARouterUtil.startActivity(ARouterConfig.TRUSTEESHIP_PRIVATEKEY_IMPORT);
                    break;
            }
        });
    }


    /*@OnClick({R2.id.btn_import_mnemonic, R2.id.btn_import_ks, R2.id.btn_import_private})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.btn_import_mnemonic) {
            ARouterUtil.startActivity(ARouterConfig.TRUSTEESHIP_MNEMONIC_IMPORT);
        } else if (view.getId() == R.id.btn_import_ks) {

        } else if (view.getId() == R.id.btn_import_private) {
            ARouterUtil.startActivity(ARouterConfig.TRUSTEESHIP_PRIVATEKEY_IMPORT);
        }
    }*/

}
