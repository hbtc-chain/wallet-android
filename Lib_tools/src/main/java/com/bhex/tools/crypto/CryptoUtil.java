package com.bhex.tools.crypto;

import org.spongycastle.jce.provider.BouncyCastleProvider;

import java.security.Key;
import java.security.Security;

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

}
