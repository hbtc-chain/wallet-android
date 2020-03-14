package com.bhex.wallet.common.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.bhex.wallet.common.db.entity.BHWalletExt;

import java.util.List;


/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/3/4
 * Time: 23:02
 */
@Dao
public interface BHWalletDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(BHWalletExt wallet);

    @Update
    void update(BHWalletExt wallet);

    @Delete
    void delete(BHWalletExt wallet);

    @Query("SELECT * FROM tab_wallet ORDER BY isDefault DESC")
    List<BHWalletExt> loadAll();
}
