package com.bhex.wallet.common.db.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * @author gongdongyang
 * 2020-10-31 10:19:07
 * 用户和Token关系表
 */
@Entity(tableName = "tab_user_token")
public class BHUserToken {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "address")
    public String address;

    @ColumnInfo(name = "symbol")
    public String symbol;

}
