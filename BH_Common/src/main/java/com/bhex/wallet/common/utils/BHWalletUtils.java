package com.bhex.wallet.common.utils;

import android.os.SystemClock;
import android.util.Log;

import com.bhex.tools.crypto.CryptoUtil;
import com.bhex.tools.crypto.HexUtils;
import com.bhex.tools.crypto.Sha256;
import com.bhex.tools.utils.LogUtils;
import com.bhex.tools.utils.MD5;
import com.bhex.wallet.common.config.BHFilePath;
import com.bhex.wallet.common.db.entity.BHWallet;
import com.bhex.wallet.common.manager.BHUserManager;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.bitcoinj.core.Bech32;
import org.bitcoinj.core.SegwitAddress;
import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDKeyDerivation;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.utils.Numeric;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/3/4
 * Time: 22:13
 */
public class BHWalletUtils {

    private static String TAG = BHWalletUtils.class.getSimpleName();

    private static ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();

    private static final SecureRandom secureRandom = SecureRandomUtils.secureRandom();

    private Credentials credentials;
    /**
     * 通用的以太坊基于bip44协议的助记词路径 （
     */
    public static String BH_CUSTOM_TYPE = "m/44'/496'/0'/0/0";

    /**
     * 创建助记词，并通过助记词创建钱包
     * @param walletName
     * @param pwd
     * @return
     */
    public static BHWallet generateMnemonic(String walletName, String pwd) {
        String[] pathArray = BH_CUSTOM_TYPE.split("/");
        String passphrase = "";
        //long creationTimeSeconds = System.currentTimeMillis() / 1000;
        DeterministicSeed ds = new DeterministicSeed(secureRandom, 128, passphrase);
        //种子
        byte[] seeds = ds.getSeedBytes();
        //助记词
        List<String> mnemonic = ds.getMnemonicCode();

        ECKeyPair keyPair = makeECKeyPair(seeds);
        BHWallet bhWallet = generateWallet(walletName, pwd, keyPair,mnemonic);

        //BHWallet bhWallet = generateWalletByMnemonic(walletName, ds, pathArray, pwd);
        return bhWallet;
    }

    //助记词导入
    public static BHWallet importMnemonic(String path, List<String> list,String walletName,String pwd) {

        String[] pathArray = path.split("/");


        String passphrase = "";
        long creationTimeSeconds = System.currentTimeMillis() / 1000;

        DeterministicSeed ds = new DeterministicSeed(list, null, passphrase, creationTimeSeconds);
        //种子
        byte[] seedBytes = ds.getSeedBytes();
        //助记词
        List<String> mnemonic = ds.getMnemonicCode();
        /*System.out.println(Arrays.toString(mnemonic.toArray()));

        if (seedBytes == null)
            return null;

        DeterministicKey dkKey = HDKeyDerivation.createMasterPrivateKey(seedBytes);
        for (int i = 1; i < pathArray.length; i++) {
            ChildNumber childNumber;
            if (pathArray[i].endsWith("'")) {
                int number = Integer.parseInt(pathArray[i].substring(0,
                        pathArray[i].length() - 1));
                childNumber = new ChildNumber(number, true);
            } else {
                int number = Integer.parseInt(pathArray[i]);
                childNumber = new ChildNumber(number, false);
            }
            dkKey = HDKeyDerivation.deriveChildKey(dkKey, childNumber);
        }

        ECKeyPair keyPair = ECKeyPair.create(dkKey.getPrivKeyBytes());*/
        ECKeyPair keyPair = makeECKeyPair(seedBytes);
        BHWallet bhWallet = generateWallet(walletName, pwd, keyPair,mnemonic);

        if (bhWallet != null) {
            String old_mnemonic =  convertMnemonicList(mnemonic);
            String encrypt_mnemonic = CryptoUtil.encryptMnemonic(old_mnemonic,pwd);
            bhWallet.setMnemonic(encrypt_mnemonic);
        }

        return bhWallet;
    }

    /**
     * 导入私钥 私钥16进制
     * @param privateKey
     */
    public static BHWallet importPrivateKey(String privateKey,String walletName,String pwd){
        ECKeyPair keyPair = ECKeyPair.create(Numeric.toBigInt(privateKey));
        BHWallet bhWallet = generateWallet(walletName, pwd, keyPair,null);
        return bhWallet;
    }

    /**
     * @param walletName 钱包名称
     * @param ds         助记词加密种子
     * @param pathArray  助记词标准
     * @param pwd        密码
     * @return
     */
    /*private static BHWallet generateWalletByMnemonic(String walletName, DeterministicSeed ds,
                                                     String[] pathArray, String pwd) {
        //种子
        byte[] seeds = ds.getSeedBytes();
        //助记词
        List<String> mnemonic = ds.getMnemonicCode();
        *//*if (seeds == null)
            return null;

        //创建主私钥
        DeterministicKey dkKey = HDKeyDerivation.createMasterPrivateKey(seeds);
        //
        for (int i = 1; i < pathArray.length; i++) {
            ChildNumber childNumber;
            if (pathArray[i].endsWith("'")) {
                int number = Integer.parseInt(pathArray[i].substring(0, pathArray[i].length() - 1));
                childNumber = new ChildNumber(number, true);

            } else {
                int number = Integer.parseInt(pathArray[i]);
                childNumber = new ChildNumber(number, false);
            }
            dkKey = HDKeyDerivation.deriveChildKey(dkKey, childNumber);
        }

        ECKeyPair keyPair = ECKeyPair.create(dkKey.getPrivKeyBytes());*//*
        ECKeyPair keyPair = makeECKeyPair(seeds);
        BHWallet bhWallet = generateWallet(walletName, pwd, keyPair,mnemonic);
        //bhWallet.setMnemonic(encryptMnemonic(mnemonic,pwd));
        *//*if (bhWallet != null) {
            String old_mnemonic =  convertMnemonicList(mnemonic);
            String encrypt_mnemonic = CryptoUtil.encryptMnemonic(old_mnemonic,pwd);
            bhWallet.setMnemonic(encrypt_mnemonic);
            //LogUtils.d(TAG+"==>:","old_mnemonic=="+old_mnemonic);
        }*//*

        return bhWallet;
    }*/




    /**
     * @param walletName
     * @param pwd
     * @param keyPair
     * @return
     */
    private static BHWallet generateWallet(String walletName, String pwd, ECKeyPair keyPair,List<String> mnemonics) {
        BHWallet bhWallet = null;
        try {
            WalletFile walletFile = Wallet.create(pwd, keyPair, 1024, 1);
            // 生成BH-地址
            String bh_adress = BHKey.getBhexUserDpAddress(keyPair.getPublicKey());
            walletFile.setAddress(bh_adress);
            //LogUtils.d(TAG+"==>:","bh_adress:"+bh_adress);

            //生成bench32地址
            String bh_bech_pubkey = BHKey.getBhexUserDpPubKey(keyPair.getPublicKey());
            //LogUtils.d(TAG+"==>:","bh_bech_pubkey:"+bh_bech_pubkey);
            //keystore存储
            String ks_path = save_keystore(walletFile,walletName);
            String pk_str = keyPair.getPrivateKey().toString(16);
            //LogUtils.d(TAG+"==>:","pk_str:"+pk_str);
            //私钥加密
            String encryptPK = CryptoUtil.encryptPK(keyPair.getPrivateKey(),pwd);

            bhWallet = BHUserManager.getInstance().getTmpBhWallet();
            bhWallet.setName(walletName);
            bhWallet.setAddress(bh_adress);
            bhWallet.setPublicKey(bh_bech_pubkey);
            bhWallet.setPrivateKey(encryptPK);
            bhWallet.setKeystorePath(ks_path);
            bhWallet.setPassword(MD5.md5(pwd));
            bhWallet.setIsDefault(0);
            bhWallet.setIsBackup(0);

            if(mnemonics!=null && mnemonics.size()==12){
                String old_mnemonic =  convertMnemonicList(mnemonics);
                String encrypt_mnemonic = CryptoUtil.encryptMnemonic(old_mnemonic,pwd);
                bhWallet.setMnemonic(encrypt_mnemonic);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return bhWallet;
    }

    /**
     * 保存KeyStore 文件
     * @param walletFile
     */
    private static String save_keystore(WalletFile walletFile,String walletName) {
        String ks_name = "ks_" + SystemClock.elapsedRealtime() + ".json";
        File ks_path = new File(BHFilePath.PATH_KEYSTORE,  ks_name);
        //目录不存在则创建目录，创建不了则报错
        if (!createParentDir(ks_path)) {
            return null;
        }
        try {
            objectMapper.writeValue(ks_path, walletFile);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return ks_path.getAbsolutePath();
    }

    private static boolean createParentDir(File file) {
        //判断目标文件所在的目录是否存在
        if (!file.getParentFile().exists()) {
            //如果目标文件所在的目录不存在，则创建父目录
            //LogUtils.d("BHWalletUtils", "目标文件所在目录不存在，准备创建");
            if (!file.getParentFile().mkdirs()) {
                //LogUtils.d("BHWalletUtils", "创建目标文件所在目录失败！");
                return false;
            }
        }
        return true;
    }




    /**
     * 生成公私钥对
     * @return
     */
    public static ECKeyPair makeECKeyPair(byte[] seedBytes){
        String[] pathArray = BH_CUSTOM_TYPE.split("/");
        DeterministicKey dkKey = HDKeyDerivation.createMasterPrivateKey(seedBytes);
        for (int i = 1; i < pathArray.length; i++) {
            ChildNumber childNumber;
            if (pathArray[i].endsWith("'")) {
                int number = Integer.parseInt(pathArray[i].substring(0,
                        pathArray[i].length() - 1));
                childNumber = new ChildNumber(number, true);
            } else {
                int number = Integer.parseInt(pathArray[i]);
                childNumber = new ChildNumber(number, false);
            }
            dkKey = HDKeyDerivation.deriveChildKey(dkKey, childNumber);
        }

        ECKeyPair keyPair = ECKeyPair.create(dkKey.getPrivKeyBytes());

        return keyPair;
    }

    /**
     * 助记词 List 转 String
     * @param mnemonics
     * @return
     */
    private static String convertMnemonicList(List<String> mnemonics) {
        StringBuilder sb = new StringBuilder();
        for (String mnemonic : mnemonics) {
            sb.append(mnemonic);
            sb.append(" ");
        }
        return sb.toString();
    }
}