package com.bhex.wallet.mnemonic.ui.activity;

import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bhex.lib.uikit.widget.editor.SimpleTextWatcher;
import com.bhex.network.utils.ToastUtils;
import com.bhex.tools.utils.LogUtils;
import com.bhex.tools.utils.NavitateUtil;
import com.bhex.tools.utils.RegexUtil;
import com.bhex.wallet.common.base.BaseCacheActivity;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.enums.MAKE_WALLET_TYPE;
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
public class ImportMnemonicActivity extends BaseCacheActivity {

    @BindView(R2.id.btn_next)
    AppCompatTextView btn_next;

    @BindView(R2.id.et_mnemonic)
    AppCompatEditText et_mnemonic;

    private List<String> mnemonicItems = new ArrayList<>();

    List<String> mOriginWords = BHUserManager.getInstance().getWordList();

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
        et_mnemonic.addTextChangedListener(new SimpleTextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                if(checkIsEmpty()){
                    btn_next.setBackgroundResource(R.drawable.btn_gray_corner);
                    btn_next.setEnabled(false);
                }else{
                    btn_next.setBackgroundResource(R.drawable.btn_bg_blue_6_corner);
                    btn_next.setEnabled(true);
                }
            }
        });
    }

    @Override
    protected void addEvent() {

    }

    @OnClick({R2.id.btn_next})
    public void onViewClicked(View view) {
        if(view.getId()==R.id.btn_next){
            importMnemoic(et_mnemonic.getText().toString().trim());

        }
    }

    private boolean checkIsEmpty(){
        String mnemoicStr = et_mnemonic.getText().toString().trim();

        boolean flag = RegexUtil.checkIsLetter(mnemoicStr);

        String []array = mnemoicStr.split("\\s+");

        if(!flag || array.length < 12){
            return true;
        }

        /*for (int i = 0; i < array.length; i++) {
            if(!mOriginWords.contains(array[i])){
                //flag = false;
                return true;
            }
        }*/

        return false;
    }
    /**
     * 导入助记词
     */
    private void importMnemoic(String mnemoicStr) {
        boolean flag = true;

        if(!TextUtils.isEmpty(mnemoicStr)){
            String []array = mnemoicStr.split("\\s+");
            for (int i = 0; i < array.length; i++) {
                if(!TextUtils.isEmpty(array[i].trim())){
                    mnemonicItems.add(array[i].trim());
                }
            }
        }


        for (int i = 0; i < mnemonicItems.size(); i++) {
            String tmp = mnemonicItems.get(i);
            if(!mOriginWords.contains(tmp)){
                flag = false;
                break;
            }
        }

        if(!flag){
            ToastUtils.showToast(getResources().getString(R.string.error_mnemonic_form));
            return;
        }
        String mnemonic_text = et_mnemonic.getText().toString().replaceAll("\\s+"," ");
        LogUtils.d("ImportMnemonicActivity===>:","mnemonic_text=="+mnemonic_text);

        BHUserManager.getInstance().getTmpBhWallet().setWay(MAKE_WALLET_TYPE.导入助记词.getWay());
        BHUserManager.getInstance().getTmpBhWallet().setMnemonic(mnemonic_text);
        BHUserManager.getInstance().getTmpBhWallet().setWords(mnemonicItems);
        ARouter.getInstance().build(ARouterConfig.TRUSTEESHIP_MNEMONIC_FRIST).navigation();
        //NavitateUtil.startActivity(this,TrusteeshipActivity.class);
        //walletViewModel.importMnemonic(this,mnemonicItems,"gongdongyang","q1234567");
        //BHWalletUtils.importMnemonic(BHWalletUtils.BH_CUSTOM_TYPE,mnemonicItems,"gongdongyang","q1234567");
    }

}
