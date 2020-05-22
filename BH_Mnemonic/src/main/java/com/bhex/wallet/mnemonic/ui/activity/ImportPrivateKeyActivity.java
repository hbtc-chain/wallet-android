package com.bhex.wallet.mnemonic.ui.activity;


import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bhex.lib.uikit.widget.editor.SimpleTextWatcher;
import com.bhex.tools.utils.LogUtils;
import com.bhex.tools.utils.NavigateUtil;
import com.bhex.tools.utils.RegexUtil;
import com.bhex.wallet.common.base.BaseCacheActivity;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.mnemonic.R;
import com.bhex.wallet.mnemonic.R2;

import butterknife.BindView;
import butterknife.OnClick;

/**
 *
 * @author gongdongyang
 * 私钥导入
 * 2020-3-18 18:26:18
 */
@Route(path = ARouterConfig.TRUSTEESHIP_IMPORT_PRIVATEKEY)
public class ImportPrivateKeyActivity extends BaseCacheActivity {

    @BindView(R2.id.tv_center_title)
    AppCompatTextView tv_center_title;
    @BindView(R2.id.et_private_key)
    AppCompatEditText et_private_key;

    @BindView(R2.id.btn_next)
    AppCompatTextView btn_next;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_import_privatekey;
    }

    @Override
    protected void initView() {
        tv_center_title.setText(getString(R.string.import_private_key));
        et_private_key.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        et_private_key.setGravity(Gravity.TOP);
        et_private_key.setSingleLine(false);
        et_private_key.setHorizontallyScrolling(false);

    }

    @Override
    protected void addEvent() {
        et_private_key.addTextChangedListener(new SimpleTextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                String origin = et_private_key.getText().toString().trim();
                boolean flag = RegexUtil.checkIsHex(origin);
                LogUtils.d("");
                if(!TextUtils.isEmpty(origin)){
                    btn_next.setBackgroundResource(R.drawable.btn_bg_blue_6_corner);
                    btn_next.setEnabled(true);
                }else{
                    btn_next.setBackgroundResource(R.drawable.btn_disabled_gray);
                    btn_next.setEnabled(false);
                }
            }
        });
    }

    @OnClick({R2.id.btn_next})
    public void onViewClicked(View view) {
        if(view.getId()==R.id.btn_next){
            importPrivateKey();
        }
    }

    /**
     * 导入privateKey
     */
    private void importPrivateKey() {
        String privateKey = et_private_key.getText().toString();
        BHUserManager.getInstance().getTmpBhWallet().setWay(2);
        BHUserManager.getInstance().getTmpBhWallet().setPrivateKey(privateKey.trim());
        NavigateUtil.startActivity(this,TrusteeshipActivity.class);
    }

}
