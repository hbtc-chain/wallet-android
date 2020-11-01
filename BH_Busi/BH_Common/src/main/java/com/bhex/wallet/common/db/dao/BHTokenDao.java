package com.bhex.wallet.common.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.bhex.wallet.common.db.entity.BHWallet;
import com.bhex.wallet.common.model.BHToken;

import java.util.List;

/**
 * @author gongdongyang
 * 2020-10-31 10:08:40
 */
@Dao
public interface BHTokenDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<BHToken> token);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    Integer update(BHWallet wallet);

    @Query("delete from tab_token")
    void delete();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertSingle(BHToken token);


    @Query("SELECT * FROM tab_token")
    List<BHToken> loadAllToken();
}
