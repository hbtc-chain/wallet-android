package com.bhex.wallet.mnemonic.persenter;

import androidx.appcompat.widget.AppCompatButton;

import com.bhex.wallet.common.base.BaseActivity;
import com.bhex.wallet.common.base.BasePresenter;
import com.bhex.wallet.mnemonic.R;
import com.bhex.wallet.mnemonic.ui.item.MnemonicItem;

import java.util.List;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/3/11
 * Time: 15:39
 */
public class VerifyPresenter extends BasePresenter {

    public VerifyPresenter(BaseActivity activity) {
        super(activity);
    }

    /**
     * 验证助记词
     * @param aboveList
     * @param orginList
     * @return
     */
    public boolean  verifyMnmonic(List<MnemonicItem> aboveList, List<MnemonicItem> orginList, AppCompatButton btn){
        boolean flag = true;
        for (int i = 0; i < orginList.size(); i++) {
            MnemonicItem aboveItem = aboveList.get(i);
            MnemonicItem orginItem = orginList.get(i);
            if(!orginItem.getWord().equals(aboveItem.getWord())){
                flag = false;
                break ;
            }

        }

        if (!flag) {
            btn.setBackgroundResource(R.drawable.btn_disabled_gray);
            btn.setEnabled(false);
        }else{
            btn.setBackgroundResource(R.drawable.btn_bg_blue_6_corner);
            btn.setEnabled(true);
        }
        return flag;
    }
}
