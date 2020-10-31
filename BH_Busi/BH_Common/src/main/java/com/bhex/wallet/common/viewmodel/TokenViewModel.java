package com.bhex.wallet.common.viewmodel;

import androidx.lifecycle.ViewModel;

import com.bhex.network.app.BaseApplication;
import com.bhex.wallet.common.db.AppDataBase;
import com.bhex.wallet.common.db.dao.BHTokenDao;

/**
 * @author gongdongyang
 * 2020年10月31日11:18:48
 */
public class TokenViewModel extends ViewModel {

    private BHTokenDao bhTokenDao;

    public TokenViewModel(){
        bhTokenDao = AppDataBase.getInstance(BaseApplication.getInstance()).bhTokenDao();
    }



}
