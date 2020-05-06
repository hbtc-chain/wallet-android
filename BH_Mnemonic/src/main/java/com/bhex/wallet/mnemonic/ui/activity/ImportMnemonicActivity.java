package com.bhex.wallet.mnemonic.ui.activity;

import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bhex.lib.uikit.util.PixelUtils;
import com.bhex.lib.uikit.widget.MnemonicInputView;
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
public class ImportMnemonicActivity extends BaseCacheActivity implements MnemonicInputView.MnemonicInputViewChangeListener {

    @BindView(R2.id.btn_next)
    AppCompatTextView btn_next;

    @BindView(R2.id.et_mnemonic)
    AppCompatEditText et_mnemonic;

    @BindView(R2.id.input_mnemonic)
    MnemonicInputView input_mnemonic;


    private List<String> mnemonicItems = new ArrayList<>();

    List<String> mOriginWords = BHUserManager.getInstance().getWordList();

    private List<String> mMnemonicWords = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_import_mnemonic;
    }

    @Override
    protected void initView() {
        /*mMnemonicWords.add("burst");
        mMnemonicWords.add("clean");
        mMnemonicWords.add("range");
        mMnemonicWords.add("coral");
        mMnemonicWords.add("dumb");*/
        input_mnemonic.setWordList(mOriginWords);
        input_mnemonic.setInputViewChangeListener(this);
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

        input_mnemonic.addEditText(null);
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
        return false;
    }
    /**
     * 导入助记词
     */
    private void importMnemoic(String mnemoicStr) {
        boolean flag = true;

        /*if(!TextUtils.isEmpty(mnemoicStr)){
            String []array = mnemoicStr.split("\\s+");
            for (int i = 0; i < array.length; i++) {
                if(!TextUtils.isEmpty(array[i].trim())){
                    mnemonicItems.add(array[i].trim());
                }
            }
        }*/

        mnemonicItems = input_mnemonic.getAllMnemonic();

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
    }

    @Override
    public void inputViewChange() {
        List<String> words = input_mnemonic.getAllMnemonic();
        if(words==null && words.size()==0){
            return;
        }
        boolean flag = true;
        for (int i = 0; i < words.size(); i++) {
            String tmp = words.get(i);
            if(!mOriginWords.contains(tmp)){
                flag = false;
                break;
            }
        }

        if(flag){
            flag = words.size()!=12?false:true;
        }

        if(flag){
            btn_next.setBackgroundResource(R.drawable.btn_bg_blue_6_corner);
            btn_next.setEnabled(true);
        }else{
            btn_next.setBackgroundResource(R.drawable.btn_gray_corner);
            btn_next.setEnabled(false);
        }

        input_mnemonic.setInputViewStatus();
    }
}
