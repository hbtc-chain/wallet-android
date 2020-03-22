package com.bhex.wallet.mnemonic;

import android.view.View;

import androidx.appcompat.widget.AppCompatButton;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bhex.tools.constants.Constants;
import com.bhex.tools.utils.StatusBarUtil;
import com.bhex.wallet.common.base.BaseCacheActivity;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.utils.ARouterUtil;
import com.bhex.wallet.mnemonic.helper.MnemonicDataHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author gongdongyang
 * 托管单元首页
 */
@Route(path= ARouterConfig.MNEMONIC_INDEX_PAGE)
public class MnemonicIndexActivity extends BaseCacheActivity {
    @BindView(R2.id.btn_generate_wallet)
    AppCompatButton btn_generate_wallet;

    @BindView(R2.id.btn_import_wallet)
    AppCompatButton btn_import_wallet;

    private List<String> mWords;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_nnemonic_index;
    }

    @Override
    protected void initView() {
        StatusBarUtil.setStatusColor(this,false,true,R.color.white);

    }

    @Override
    protected void addEvent() {
        mWords = MnemonicDataHelper.makeMnemonicString();
    }

    @OnClick({R2.id.btn_generate_wallet,R2.id.btn_import_wallet})
    public void onViewClicked(View view) {
        if(view.getId()== R.id.btn_generate_wallet){
            ARouterUtil.startActivity(ARouterConfig.TRUSTEESHIP_MNEMONIC_FRIST);
        }else if(view.getId()== R.id.btn_import_wallet){
            ARouterUtil.startActivity(ARouterConfig.TRUSTEESHIP_IMPORT_INDEX);
        }

    }

}
