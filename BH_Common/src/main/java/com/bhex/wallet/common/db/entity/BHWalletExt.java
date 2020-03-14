package com.bhex.wallet.common.db.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/3/4
 * Time: 23:12
 */
@Entity(tableName = "tab_wallet")
public class BHWalletExt {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "address")
    private String address;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "password")
    private String password;

    @ColumnInfo(name = "keystorePath")
    private String keystorePath;

    @ColumnInfo(name = "mnemonic")
    private String mnemonic;

    @ColumnInfo(name = "isBackup")
    private int isBackup; //1备份 2未备份

    @ColumnInfo(name = "isDefault")
    private int isDefault;//1默认 0默认




    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getKeystorePath() {
        return keystorePath;
    }

    public void setKeystorePath(String keystorePath) {
        this.keystorePath = keystorePath;
    }

    public String getMnemonic() {
        return mnemonic;
    }

    public void setMnemonic(String mnemonic) {
        this.mnemonic = mnemonic;
    }

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }

    public int getIsBackup() {
        return isBackup;
    }

    public void setIsBackup(int isBackup) {
        this.isBackup = isBackup;
    }
}
