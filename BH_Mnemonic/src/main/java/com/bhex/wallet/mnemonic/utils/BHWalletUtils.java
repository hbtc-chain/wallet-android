package com.bhex.wallet.mnemonic.utils;

import android.os.SystemClock;

import com.bhex.tools.crypto.HexUtils;
import com.bhex.tools.crypto.Sha256;
import com.bhex.tools.utils.LogUtils;
import com.bhex.tools.utils.MD5;
import com.bhex.wallet.common.config.BHFilePath;
import com.bhex.wallet.common.db.entity.BHWallet;
import com.bhex.wallet.common.manager.BHUserManager;
import com.fasterxml.jackson.databind.ObjectMapper;

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

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/3/4
 * Time: 22:13
 */
public class BHWalletUtils {
    private static ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();

    private static final SecureRandom secureRandom = SecureRandomUtils.secureRandom();

    private Credentials credentials;
    /**
     * 通用的以太坊基于bip44协议的助记词路径 （imtoken jaxx Metamask myetherwallet）
     */
    /*public static String ETH_JAXX_TYPE = "m/44'/60'/0'/0/0";
    public static String ETH_LEDGER_TYPE = "m/44'/60'/0'/0";
    public static String ETH_CUSTOM_TYPE = "m/44'/60'/1'/0/0";*/
    public static String BH_CUSTOM_TYPE = "m/44'/496'/0'/0/0";

    /**
     * 创建助记词，并通过助记词创建钱包
     *
     * @param walletName
     * @param pwd
     * @return
     */
    public static BHWallet generateMnemonic(String walletName, String pwd) {
        String[] pathArray = BH_CUSTOM_TYPE.split("/");
        String passphrase = "";
        long creationTimeSeconds = System.currentTimeMillis() / 1000;

        DeterministicSeed ds = new DeterministicSeed(secureRandom, 128, passphrase);
        return generateWalletByMnemonic(walletName, ds, pathArray, pwd);

    }

    /**
     * @param walletName 钱包名称
     * @param ds         助记词加密种子
     * @param pathArray  助记词标准
     * @param pwd        密码
     * @return
     */
    private static BHWallet generateWalletByMnemonic(String walletName, DeterministicSeed ds,
                                                     String[] pathArray, String pwd) {
        //种子
        byte[] seeds = ds.getSeedBytes();
        //助记词
        List<String> mnemonic = ds.getMnemonicCode();

        if (seeds == null)
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

        ECKeyPair keyPair = ECKeyPair.create(dkKey.getPrivKeyBytes());
        //LogUtils.d();keyPair.getPublicKey().bitLength();

        BHWallet bhWalletExt = generateWallet(walletName, pwd, keyPair);

        if (bhWalletExt != null) {
            bhWalletExt.setMnemonic(convertMnemonicList(mnemonic));
        }
        return bhWalletExt;
    }

    private static String convertMnemonicList(List<String> mnemonics) {
        StringBuilder sb = new StringBuilder();
        for (String mnemonic : mnemonics) {
            sb.append(mnemonic);
            sb.append(" ");
        }
        return sb.toString();
    }


    /**
     * @param walletName
     * @param pwd
     * @param keyPair
     * @return
     */
    private static BHWallet generateWallet(String walletName, String pwd, ECKeyPair keyPair) {
        WalletFile walletFile;
        try {
            walletFile = Wallet.create(pwd, keyPair, 1024, 1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        // 生成BH-地址
        String bh_adress = pubKey_to_adress(keyPair.getPublicKey());
        walletFile.setAddress(bh_adress);

        //keystore存储
        String ks_path = save_keystore(walletFile,walletName);

        //byte [] t = Keys.serialize(keyPair);
        //LogUtils.d("BHWalletUtils==>:", pwd+"==pwd=="+MD5.md5(pwd));

        //LogUtils.d("BHWalletUtils", Keys.toChecksumAddress(walletFile.getAddress()));
        BHWallet bhWallet = BHUserManager.getInstance().getBhWallet();
        bhWallet.setName(walletName);
        bhWallet.setAddress(bh_adress);
        //walletExt.setAddress(Keys.toChecksumAddress(walletFile.getAddress()));
        //walletExt.setAddress(walletFile.getAddress());
        bhWallet.setKeystorePath(ks_path);
        bhWallet.setPassword(MD5.md5(pwd));
        bhWallet.setIsDefault(0);
        bhWallet.setIsBackup(0);
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
            LogUtils.d("BHWalletUtils", "目标文件所在目录不存在，准备创建");
            if (!file.getParentFile().mkdirs()) {
                LogUtils.d("BHWalletUtils", "创建目标文件所在目录失败！");
                return false;
            }
        }
        return true;
    }

    //助记词导入
    public static void importMnemonic(String path, List<String> list) {
        if (!path.startsWith("m") && !path.startsWith("M")) {
            //参数非法
            return;
        }
        String[] pathArray = path.split("/");
        if (pathArray.length <= 1) {
            //内容不对
            return;
        }
        /*if (password.length() < 8) {
            //密码过短
            return ;
        }*/
        String passphrase = "";
        long creationTimeSeconds = System.currentTimeMillis() / 1000;

        DeterministicSeed ds = new DeterministicSeed(list, null, passphrase, creationTimeSeconds);

        //种子
        byte[] seedBytes = ds.getSeedBytes();
        System.out.println(Arrays.toString(seedBytes));


        //助记词
        List<String> mnemonic = ds.getMnemonicCode();
        System.out.println(Arrays.toString(mnemonic.toArray()));

        if (seedBytes == null)
            return;

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

        String bh_address = pubKey_to_adress(keyPair.getPublicKey());
        /*//公钥压缩
        String pubKey_compress = compressPubKey(keyPair.getPublicKey());

        LogUtils.d("BHWallUtils=>","pubKey_compress:"+pubKey_compress);

        LogUtils.d("BHWallUtils=>","pubKey_compress-->Bytes:"+Arrays.toString(HexUtils.toBytes(pubKey_compress)));

        //公钥hash
        byte [] pubKeyHash = pubKeyCompressToHash(pubKey_compress);

        LogUtils.d("BHWallUtils=>","hash:"+Arrays.toString(pubKeyHash));

        //base58编码
        String address = base58Address(pubKeyHash);

        LogUtils.d("BHWallUtils=>","address:"+address);*/


    }

    public static String pubKey_to_adress(BigInteger publicKey) {
        //LogUtils.d("BHWallUtils=>", "publicKey:" + publicKey.toString(16));

        //LogUtils.d("BHWallUtils=>", "publicKey-->bytes:" + Arrays.toString(HexUtils.toBytes(publicKey.toString(16))));

        //公钥压缩
        String pubKey_compress = compressPubKey(publicKey);


        //LogUtils.d("BHWallUtils=>", "pubKey_compress:" + pubKey_compress);

        //LogUtils.d("BHWallUtils=>", "pubKey_compress-->Bytes:" + Arrays.toString(HexUtils.toBytes(pubKey_compress)));


        //公钥hash
        byte[] pubKeyHash = pubKeyCompressToHash(pubKey_compress);

        //LogUtils.d("BHWallUtils=>", "ripemd160->hash:" + Arrays.toString(pubKeyHash));
        //LogUtils.d("BHWallUtils=>", "ripemd160-hex16->:" + HexUtils.toHex(pubKeyHash));


        //base58编码
        String address = base58Address(pubKeyHash);

        //LogUtils.d("BHWallUtils=>", "address:" + address);


        //String pubk = Bech32.encode("bhpub", HexUtils.toBytes(publicKey.toString(16)));

        //LogUtils.d("BHWallUtils=>", "pubk:" + pubk);

        return address;
    }

    /**
     * pubkey- 2次 hash
     *
     * @return
     */
    public static byte[] pubKeyCompressToHash(String pubKey_compress) {
        MessageDigest digest = Sha256.getSha256Digest();

        byte[] hash = digest.digest(HexUtils.toBytes(pubKey_compress));

        //LogUtils.d("BHWallUtils=>", "Sha256-->:" + Arrays.toString(hash));

        //LogUtils.d("BHWallUtils=>", "Sha256-hex16->:" + HexUtils.toHex(hash));
        RIPEMD160Digest digest2 = new RIPEMD160Digest();
        digest2.update(hash, 0, hash.length);
        byte[] hash2 = new byte[digest2.getDigestSize()];
        digest2.doFinal(hash2, 0);
        return hash2;
    }

    /**
     * 公钥序列化 公钥 64bit 转 33bit
     *
     * @param pubKey
     * @return
     */
    public static String compressPubKey(BigInteger pubKey) {
        String pubKeyYPrefix = pubKey.testBit(0) ? "03" : "02";
        String pubKeyHex = pubKey.toString(16);
        String pubKeyX = pubKeyHex.substring(0, 64);
        return pubKeyYPrefix + pubKeyX;
    }

    /**
     * hash 转 base58
     *
     * @param hash
     * @return
     */
    public static String base58Address(byte[] hash) {
        String encode = "";
        byte[] bh_prefix = new byte[]{5, -54};

        byte[] hash_new = new byte[2 + hash.length];

        System.arraycopy(bh_prefix, 0, hash_new, 0, 2);

        System.arraycopy(hash, 0, hash_new, 2, hash.length);

        byte[] check = BHBase58.checkSum(hash_new);

        byte[] hash_new2 = new byte[2 + hash.length + 4];

        System.arraycopy(bh_prefix, 0, hash_new2, 0, 2);

        System.arraycopy(hash, 0, hash_new2, 2, hash.length);

        System.arraycopy(check, 0, hash_new2, 2 + hash.length, 4);

        encode = BHBase58.encode(hash_new2);
        //System.out.println();
        //System.out.println("BHWallUtils=>res==" + encode);

        //Base58.encodeChecked()

        //System.out.println("BHWallUtils=>"+"decode:"+Arrays.toString(Base58.decode("BHf68hJ65Y6Q4deCVTLmQYVeoFXKrSTVL8G")));
        return encode;
    }


    /*public static String bech32Encode(byte[] hrp, byte[] data) {
        byte[] chk = createChecksum(hrp, data);
        byte[] combined = new byte[chk.length + data.length];

        System.arraycopy(data, 0, combined, 0, data.length);
        System.arraycopy(chk, 0, combined, data.length, chk.length);

        byte[] xlat = new byte[combined.length];
        for (int i = 0; i < combined.length; i++) {
            xlat[i] = (byte) CHARSET.charAt(combined[i]);
        }

        byte[] ret = new byte[hrp.length + xlat.length + 1];
        System.arraycopy(hrp, 0, ret, 0, hrp.length);
        System.arraycopy(new byte[]{0x31}, 0, ret, hrp.length, 1);
        System.arraycopy(xlat, 0, ret, hrp.length + 1, xlat.length);


    }*/
}