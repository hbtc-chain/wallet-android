package com.bhex.wallet.common.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.bhex.wallet.common.db.entity.BHWallet;
import com.bhex.wallet.common.model.BHToken;

/**
 * @author gongdongyang
 * 2020-10-31 10:08:40
 */
@Dao
public interface BHTokenDao {
    @Insert
    Long insert(BHToken token);

    @Query("delete from tab_token")
    void delete();
}
