package com.bhex.wallet.common.utils;

import com.bhex.tools.utils.LogUtils;

import org.bitcoinj.core.Base58;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/3/10
 * Time: 18:11
 */
public class BHBase58 extends Base58 {

    public static byte[] checkSum( byte[] payload) {

        //byte[] addressBytes = new byte[1 + payload.length + 4];
        //System.arraycopy(payload, 0, addressBytes, 1, payload.length);
        //LogUtils.d("BHBase58==>:",Arrays.toString(payload));
        byte[] checksum = hashTwiceSHA256(payload);

        //System.out.println("BHWallUtils=>"+"checksum-btyes:"+ Arrays.toString(checksum));
        byte[] checksum1 = new byte[4];

        //System.arraycopy(checksum1, 0, checksum, checksum.length-4, 4);

        System.arraycopy(checksum, 0, checksum1, 0, 4);

        //System.out.println("BHWallUtils=>"+"checksum-1-:"+Arrays.toString(checksum1));
        return checksum1;
    }


    public static byte[] hashTwiceSHA256( byte[] payload) {
        MessageDigest digest = newDigest();
        digest.update(payload, 0, payload.length);
        byte []hash1 = digest.digest();
        //LogUtils.d("BHBase58==>:","hash--one-"+Arrays.toString(hash1));

        byte []hash2 = digest.digest(hash1);
        //LogUtils.d("BHBase58==>:","hash--two-"+Arrays.toString(hash2));

        return hash2;
    }


    public static MessageDigest newDigest() {
        try {
            return MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);  // Can't happen.
        }
    }
}
