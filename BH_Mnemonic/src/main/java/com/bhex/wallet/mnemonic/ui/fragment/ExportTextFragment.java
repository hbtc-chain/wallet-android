package com.bhex.wallet.mnemonic.ui.fragment;


import androidx.appcompat.widget.AppCompatEditText;

import com.bhex.network.mvx.base.BaseFragment;
import com.bhex.network.utils.ToastUtils;
import com.bhex.tools.utils.ToolUtils;
import com.bhex.wallet.common.db.entity.BHWallet;
import com.bhex.wallet.common.enums.BH_BUSI_TYPE;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.mnemonic.R;
import com.bhex.wallet.mnemonic.R2;
import com.google.android.material.button.MaterialButton;

import butterknife.BindView;


/**
 * @author gongdongyang
 * 私钥或Keystore文本导出
 * 2020-5-17 00:08:19
 */
public class ExportTextFragment extends BaseFragment {

    public static final String KEY_FLAG = "flag";

    @BindView(R2.id.et_private_key)
    AppCompatEditText et_private_key;

    @BindView(R2.id.btn_copy)
    MaterialButton btn_copy;

    BHWallet mCurrentWallet;

    private String flag;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_export_text;
    }

    @Override
    protected void initView() {
        et_private_key.setFocusable(false);
        et_private_key.setFocusableInTouchMode(false);

        mCurrentWallet = BHUserManager.getInstance().getCurrentBhWallet();
        flag = getArgumentValue(ExportTextFragment.KEY_FLAG);

        if(BH_BUSI_TYPE.备份私钥.value.equals(flag)){
            et_private_key.setText(BHUserManager.getInstance().getOriginContext(mCurrentWallet.privateKey));
            btn_copy.setText(getString(R.string.copy_privatekey));
        }else{
            et_private_key.setText(mCurrentWallet.keystorePath);
            btn_copy.setText(getString(R.string.copy_keystore));
        }
    }


    @Override
    protected void addEvent() {
        btn_copy.setOnClickListener(v -> {
            ToolUtils.copyText(et_private_key.getText().toString(),getContext());
            ToastUtils.showToast(getString(R.string.copyed));
        });
    }

    private String getArgumentValue(String key){
        String result = "";
        if(getArguments()!=null){
            result = getArguments().getString(ExportTextFragment.KEY_FLAG,"1");
        }
        return result;
    }

}
