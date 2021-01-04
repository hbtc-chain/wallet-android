package com.bhex.wallet.common.db.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.List;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/3/4
 * Time: 23:12
 */
//@Entity(tableName = "tab_wallet",indices = {@Index(value = {"mnemonic"},unique = true)})
@Entity(tableName = "tab_wallet")
public class BHWallet {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "address")
    public String address;

    @ColumnInfo(name = "name")
    public String name;

    //@ColumnInfo(name = "password")
    @Ignore
    public String password;

    @ColumnInfo(name = "keystorePath")
    public String keystorePath;

    //@ColumnInfo(name = "mnemonic")
    @Ignore
    public String mnemonic;

    @ColumnInfo(name = "isBackup")
    public int isBackup; //1备份 0未备份

    @ColumnInfo(name = "isDefault")
    public int isDefault;//1默认 0默认

    //@ColumnInfo(name = "privateKey")
    @Ignore
    public String privateKey;

    @ColumnInfo(name = "publicKey")
    public String publicKey;

    @Ignore
    public List<String> mWords;

    @Ignore
    public String pwd;

    @ColumnInfo(name = "way")
    public int way;//0 生成 1 助记词导入 2 导入keystore 3.私钥


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

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public List<String> getWords() {
        return mWords;
    }

    public void setWords(List<String> mWords) {
        this.mWords = mWords;
    }

    public int getWay() {
        return way;
    }

    public void setWay(int way) {
        this.way = way;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
