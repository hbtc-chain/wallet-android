package com.bhex.wallet.mnemonic.ui.activity;

import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.tools.utils.NavitateUtil;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.mnemonic.R;
import com.bhex.wallet.mnemonic.R2;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 导入助记词
 *
 * @author gdy
 * 2020-3-11 18:37:15
 */
@Route(path = ARouterConfig.TRUSTEESHIP_MNEMONIC_IMPORT)
public class ImportMnemonicActivity extends BaseActivity {

    @BindView(R2.id.btn_next)
    AppCompatTextView btn_next;

    @BindView(R2.id.et_mnemonic)
    AppCompatEditText et_mnemonic;

    private List<String> mnemonicItems = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_import_mnemonic;
    }

    @Override
    protected void initView() {
        et_mnemonic.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        et_mnemonic.setGravity(Gravity.TOP);
        et_mnemonic.setSingleLine(false);
        et_mnemonic.setHorizontallyScrolling(false);
    }

    @Override
    protected void addEvent() {

    }

    @OnClick({R2.id.btn_next})
    public void onViewClicked(View view) {
        if(view.getId()==R.id.btn_next){
            importMnemoic();

        }
    }

    /**
     * 导入助记词
     */
    private void importMnemoic() {
        String mnemoicStr = et_mnemonic.getText().toString();
        if(!TextUtils.isEmpty(mnemoicStr)){
            String []array = mnemoicStr.split(" ");
            for (int i = 0; i < array.length; i++) {
                if(!TextUtils.isEmpty(array[i])){
                    mnemonicItems.add(array[i]);
                }
            }
        }

        if(mnemonicItems.size()==12){
            BHUserManager.getInstance().getTmpBhWallet().setWay(1);
            BHUserManager.getInstance().getTmpBhWallet().setMnemonic(et_mnemonic.getText().toString());
            BHUserManager.getInstance().getTmpBhWallet().setWords(mnemonicItems);
            NavitateUtil.startActivity(this,TrusteeshipActivity.class);
        }

        //walletViewModel.importMnemonic(this,mnemonicItems,"gongdongyang","q1234567");
        //BHWalletUtils.importMnemonic(BHWalletUtils.BH_CUSTOM_TYPE,mnemonicItems,"gongdongyang","q1234567");
    }

}
