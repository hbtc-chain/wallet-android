package com.bhex.tools.crypto;

import com.bhex.tools.utils.LogUtils;
import com.bhex.tools.utils.MD5;

import org.spongycastle.jce.provider.BouncyCastleProvider;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.security.Key;
import java.security.MessageDigest;
import java.security.Security;
import java.util.Arrays;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/3/16
 * Time: 12:26
 */
public class CryptoUtil {

    private static byte[] iv = { 0x38, 0x37, 0x36, 0x35, 0x34, 0x33, 0x32, 0x31, 0x38, 0x37,0x36, 0x35, 0x34, 0x33, 0x32, 0x31 };
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * 加密
     * @param content  需要加密的内容
     * @param password 加密密码
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(byte[] content, String password) throws Exception {
        Key key = new SecretKeySpec(password.getBytes("utf-8"), "AES");
        Cipher in = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
        in.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
        byte[] enc = in.doFinal(content);
        return enc;
    }


    /**
     * 解密
     * @param content
     * @param password
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(byte[] content, String password) throws Exception {
        Key key = new SecretKeySpec(password.getBytes("utf-8"), "AES");
        Cipher out = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
        out.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
        byte[] dec = out.doFinal(content);
        return dec;
    }



    //助记词加密
    public static String encryptMnemonic(String origin,String pwd){
        String encrypt = "";
        try{
            String key = MD5.md5(pwd);
            byte[] result = CryptoUtil.encrypt(origin.getBytes(),key);
            encrypt = HexUtils.toHex(result);
        }catch (Exception e){

        }
        return encrypt;
    }

    //解密助记词
    public static String decryptMnemonic(String encryptMnemonic,String pwd){
        String encrypt = "";
        try{
            byte[] result = CryptoUtil.decrypt(HexUtils.toBytes(encryptMnemonic),pwd);
            encrypt = new String(result);
        }catch (Exception e){
            e.printStackTrace();
        }
        return encrypt;
    }

    //加密私钥
    public static String encryptPK(BigInteger originPK, String pwd){
        String encrypt_PK = "";
        try{
            byte []hex= HexUtils.toBytes(originPK.toString(16));
            byte[] result = CryptoUtil.encrypt(hex,MD5.md5(pwd));
            encrypt_PK = HexUtils.toHex(result);
        }catch (Exception e){
            e.printStackTrace();
        }
        return encrypt_PK;
    }

    /**
     * 解密私钥 - 16进制
     * @param encryptPK
     * @param pwd
     * @return
     */
    public static String decryptPK(String encryptPK,String pwd){
        String decrypt = "";
        try{
            byte[] byte_pk = CryptoUtil.decrypt(HexUtils.toBytes(encryptPK),pwd);

            BigInteger big_pk = Numeric.toBigInt(byte_pk);
            decrypt = big_pk.toString(16);
            //LogUtils.d("CryptoUtil==>:","decryptPK=hex==>:"+ Arrays.toString(byte_pk));
            //LogUtils.d("CryptoUtil==>:","decryptPK=b==>:"+ big_pk.toString(16));
        }catch (Exception e){
            e.printStackTrace();
        }
        return decrypt;
    }


    private static String convertMnemonicList(List<String> mnemonics) {
        StringBuilder sb = new StringBuilder();
        for (String mnemonic : mnemonics) {
            sb.append(mnemonic);
            sb.append(" ");
        }
        return sb.toString();
    }


    public static String sha256(String origin){
        String res = "";
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(origin.getBytes("UTF-8"));
            res = HexUtils.toHex(md.digest());
        }catch (Exception e){

        }
        return res;
    }

}
