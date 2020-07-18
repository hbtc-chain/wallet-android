package com.bhex.wallet.common.utils;

import android.os.SystemClock;
import android.util.Log;

import com.bhex.network.utils.JsonUtils;
import com.bhex.tools.crypto.CryptoUtil;
import com.bhex.tools.crypto.HexUtils;
import com.bhex.tools.crypto.Sha256;
import com.bhex.tools.utils.LogUtils;
import com.bhex.tools.utils.MD5;
import com.bhex.wallet.common.config.BHFilePath;
import com.bhex.wallet.common.db.entity.BHWallet;
import com.bhex.wallet.common.manager.BHUserManager;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.bitcoinj.core.Bech32;
import org.bitcoinj.core.SegwitAddress;
import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDKeyDerivation;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.crypto.WalletUtils;
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

    //private static final ObjectMapper objectMapper = new ObjectMapper();
    /*static {
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }*/
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

        DeterministicSeed ds = new DeterministicSeed(secureRandom, 128, passphrase);
        //种子
        byte[] seeds = ds.getSeedBytes();
        //助记词
        List<String> mnemonic = ds.getMnemonicCode();

        ECKeyPair keyPair = makeECKeyPair(seeds);
        BHWallet bhWallet = generateWallet(walletName, pwd, keyPair,mnemonic);

        return bhWallet;
    }

    //助记词导入
    public static BHWallet importMnemonic(String path, List<String> list,String walletName,String pwd) {
        //String[] pathArray = path.split("/");
        String passphrase = "";
        long creationTimeSeconds = System.currentTimeMillis() / 1000;

        DeterministicSeed ds = new DeterministicSeed(list, null, passphrase, creationTimeSeconds);
        //种子
        byte[] seedBytes = ds.getSeedBytes();
        //助记词
        List<String> mnemonic = ds.getMnemonicCode();
        //生成密钥对
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
     * 导入KeyStroe
     * @param pwd
     * @return
     */
    public static BHWallet importKeyStore(String keystore,String name,String pwd){
        BHWallet bhWallet = null;
        try{
            Credentials credentials = null;
            WalletFile walletFile = objectMapper.readValue(keystore, WalletFile.class);
            credentials = Credentials.create(HLWallet.decrypt(pwd, walletFile));
            String pk = credentials.getEcKeyPair().getPrivateKey().toString(16);
            //走导入私钥的补助
            if(credentials!=null){
                bhWallet = generateWallet(name, pwd, credentials.getEcKeyPair(),null);
            }
        }catch (CipherException e){
            e.printStackTrace();
            return null;
        }catch (Exception e){
            e.printStackTrace();
        }
        return  bhWallet;
    }

    public static BHWallet importKeyStoreExt(Credentials credentials,String name,String pwd){
        BHWallet bhWallet = null;
        try{
            //String pk = credentials.getEcKeyPair().getPrivateKey().toString(16);
            //走导入私钥的补助
            if(credentials!=null){
                bhWallet = generateWallet(name, pwd, credentials.getEcKeyPair(),null);
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return  bhWallet;
    }


    /**
     * 从私钥到地址
     * @param privateKey
     * @return
     */
    public static String privatekeyToAddress(String privateKey){
        ECKeyPair keyPair = ECKeyPair.create(Numeric.toBigInt(privateKey));
        String bh_adress = BHKey.getBhexUserDpAddress(keyPair.getPublicKey());
        return bh_adress;
    }

    /**
     * 从KeyStore到地址
     * @param keyStore
     * @param pwd
     * @return
     */
    public static String keyStoreToAddress(String keyStore,String pwd) throws CipherException,IOException{
        String bh_address = null;

        WalletFile walletFile = objectMapper.readValue(keyStore, WalletFile.class);

        Credentials credentials = Credentials.create(HLWallet.decrypt(pwd, walletFile));
        if(credentials!=null){
            bh_address = BHKey.getBhexUserDpAddress(credentials.getEcKeyPair().getPublicKey());
        }else{
            return null;
        }
        return  bh_address;
    }

    /**
     * 验证Keystore
     * @param keyStore
     * @param pwd
     * @return
     */
    public static Credentials verifyKeystore(String keyStore,String pwd) throws CipherException,IOException{
        //String bh_address = null;

        WalletFile walletFile = objectMapper.readValue(keyStore, WalletFile.class);

        Credentials credentials = Credentials.create(HLWallet.decrypt(pwd, walletFile));
        if(credentials!=null){
            //bh_address = BHKey.getBhexUserDpAddress(credentials.getEcKeyPair().getPublicKey());
            return credentials;
        }else{
            return null;
        }
    }

    /**
     * 助记词到公钥
     * @return
     */
    public static String memonicToAddress( List<String> list){
        String passphrase = "";
        long creationTimeSeconds = System.currentTimeMillis() / 1000;

        DeterministicSeed ds = new DeterministicSeed(list, null, passphrase, creationTimeSeconds);
        //种子
        byte[] seedBytes = ds.getSeedBytes();
        //助记词
        List<String> mnemonic = ds.getMnemonicCode();
        //生成密钥对
        ECKeyPair keyPair = makeECKeyPair(seedBytes);

        String bh_adress = BHKey.getBhexUserDpAddress(keyPair.getPublicKey());

        return bh_adress;
    }


    /**
     * @param walletName
     * @param pwd
     * @param keyPair
     * @return
     */
    private static BHWallet generateWallet(String walletName, String pwd, ECKeyPair keyPair,List<String> mnemonics) {
        BHWallet bhWallet = null;
        try {
            //WalletUtils.generateLightNewWalletFile("a12345678", file);
            WalletFile walletFile = Wallet.create(pwd, keyPair, 1024, 1);
            // 生成BH-地址
            String bh_adress = BHKey.getBhexUserDpAddress(keyPair.getPublicKey());
            walletFile.setAddress(bh_adress);
            //生成bench32地址
            String bh_bech_pubkey = BHKey.getBhexUserDpPubKey(keyPair.getPublicKey());
            //keystore存储
            //String ks_path = save_keystore(walletFile,walletName);
            //私钥加密
            String encryptPK = CryptoUtil.encryptPK(keyPair.getPrivateKey(),pwd);
            String raw_json = JsonUtils.toJson(walletFile);
            //LogUtils.d("BHWalletUtils==>:","raw_json=="+raw_json);
            bhWallet = BHUserManager.getInstance().getTmpBhWallet();
            bhWallet.setName(walletName);
            bhWallet.setAddress(bh_adress);
            bhWallet.setPublicKey(bh_bech_pubkey);
            bhWallet.setPrivateKey(encryptPK);
            bhWallet.setKeystorePath(raw_json);
            bhWallet.setPassword(MD5.generate(pwd));
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