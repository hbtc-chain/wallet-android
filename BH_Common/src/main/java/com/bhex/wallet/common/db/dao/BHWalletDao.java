package com.bhex.wallet.common.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.bhex.wallet.common.db.entity.BHWallet;

import java.util.List;


/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/3/4
 * Time: 23:02
 */
@Dao
public interface BHWalletDao {

    //@Insert(onConflict = OnConflictStrategy.REPLACE)
    @Insert
    Long insert(BHWallet wallet);

    @Update
    void update(BHWallet wallet);

    @Delete
    void delete(BHWallet wallet);

    @Query("SELECT * FROM tab_wallet ORDER BY isDefault DESC")
    List<BHWallet> loadAll();

    @Query("SELECT max(id) FROM tab_wallet")
    int loadMaxId();


    @Query("update tab_wallet set isDefault=:isDefault where Id=:bh_id")
    void update(int bh_id,int isDefault);


    @Query("update tab_wallet set isDefault=:status")
    void updateNoDefault(int status);

}
