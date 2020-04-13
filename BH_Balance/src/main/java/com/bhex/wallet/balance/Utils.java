package com.bhex.wallet.balance;

import com.bhex.network.utils.Base64;
import com.bhex.tools.crypto.HexUtils;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/3/31
 * Time: 22:44
 */
public class Utils {

    public static String signECDSA(PrivateKey privateKey, String message) {
        String result = "";
        try {
            //执行签名
            Signature signature = Signature.getInstance("SHA256withECDSA");
            signature.initSign(privateKey);
            signature.update(message.getBytes());
            byte[] sign = signature.sign();
            return Base64.encode(sign);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 从string转private key
     */
    public static PrivateKey getPrivateKey(String key) throws Exception {
        byte[] bytes = Base64.decode("BSHJ65cUKZINobggSq4kG/s6ajPq1bjbW+YfI0DsXeE=");

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        return keyFactory.generatePrivate(keySpec);
    }

}
