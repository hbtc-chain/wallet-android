package com.bhex.wallet.common.utils;

import android.util.Log;

import com.bhex.network.utils.Base64;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.crypto.CryptoUtil;
import com.bhex.tools.crypto.HexUtils;
import com.bhex.tools.crypto.Sha256;
import com.bhex.tools.utils.LogUtils;
import com.bhex.tools.utils.MD5;
import com.fasterxml.jackson.databind.ser.Serializers;

import org.bitcoinj.core.Base58;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Utils;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Arrays;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/3/31
 * Time: 12:26
 */
public class BHKey {

    private static final String TAG = BHKey.class.getSimpleName();

    private static final String CHARSET = "qpzry9x8gf2tvdw0s3jn54khce6mua7l";

    private static final String BH_PRE_PUB_KEY = "eb5ae98721";

    public static byte[] convertBits(byte[] data, int frombits, int tobits, boolean pad) throws Exception {
        int acc = 0;
        int bits = 0;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int maxv = (1 << tobits) - 1;
        for (int i = 0; i < data.length; i++) {
            int value = data[i] & 0xff;
            if ((value >>> frombits) != 0) {
                throw new Exception("invalid data range: data[" + i + "]=" + value + " (frombits=" + frombits + ")");
            }
            acc = (acc << frombits) | value;
            bits += frombits;
            while (bits >= tobits) {
                bits -= tobits;
                baos.write((acc >>> bits) & maxv);
            }
        }
        if (pad) {
            if (bits > 0) {
                baos.write((acc << (tobits - bits)) & maxv);
            }
        } else if (bits >= frombits) {
            throw new Exception("illegal zero padding");
        } else if (((acc << (tobits - bits)) & maxv) != 0) {
            throw new Exception("non-zero padding");
        }
        return baos.toByteArray();
    }

    public static String bech32Encode(byte[] hrp, byte[] data) {
        byte[] chk = createChecksum(hrp, data);
        byte[] combined = new byte[chk.length + data.length];

        System.arraycopy(data, 0, combined, 0, data.length);
        System.arraycopy(chk, 0, combined, data.length, chk.length);

        byte[] xlat = new byte[combined.length];
        for(int i = 0; i < combined.length; i++)   {
            xlat[i] = (byte)CHARSET.charAt(combined[i]);
        }

        byte[] ret = new byte[hrp.length + xlat.length + 1];
        System.arraycopy(hrp, 0, ret, 0, hrp.length);
        System.arraycopy(new byte[] { 0x31 }, 0, ret, hrp.length, 1);
        System.arraycopy(xlat, 0, ret, hrp.length + 1, xlat.length);

        return new String(ret);
    }

    private static byte[] createChecksum(byte[] hrp, byte[] data)  {
        byte[] zeroes = new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
        byte[] expanded = hrpExpand(hrp);
        byte[] values = new byte[zeroes.length + expanded.length + data.length];

        System.arraycopy(expanded, 0, values, 0, expanded.length);
        System.arraycopy(data, 0, values, expanded.length, data.length);
        System.arraycopy(zeroes, 0, values, expanded.length + data.length, zeroes.length);

        int polymod = polymod(values) ^ 1;
        byte[] ret = new byte[6];
        for(int i = 0; i < ret.length; i++)   {
            ret[i] = (byte)((polymod >> 5 * (5 - i)) & 0x1f);
        }

        return ret;
    }

    private static int polymod(byte[] values)  {
        final int[] GENERATORS = { 0x3b6a57b2, 0x26508e6d, 0x1ea119fa, 0x3d4233dd, 0x2a1462b3 };

        int chk = 1;

        for(byte b : values)   {
            byte top = (byte)(chk >> 0x19);
            chk = b ^ ((chk & 0x1ffffff) << 5);
            for(int i = 0; i < 5; i++)   {
                chk ^= ((top >> i) & 1) == 1 ? GENERATORS[i] : 0;
            }
        }

        return chk;
    }

    private static byte[] hrpExpand(byte[] hrp) {
        byte[] buf1 = new byte[hrp.length];
        byte[] buf2 = new byte[hrp.length];
        byte[] mid = new byte[1];

        for (int i = 0; i < hrp.length; i++)   {
            buf1[i] = (byte)(hrp[i] >> 5);
        }
        mid[0] = 0x00;
        for (int i = 0; i < hrp.length; i++)   {
            buf2[i] = (byte)(hrp[i] & 0x1f);
        }

        byte[] ret = new byte[(hrp.length * 2) + 1];
        System.arraycopy(buf1, 0, ret, 0, buf1.length);
        System.arraycopy(mid, 0, ret, buf1.length, mid.length);
        System.arraycopy(buf2, 0, ret, buf1.length + mid.length, buf2.length);

        return ret;
    }


    public static String getBhexUserDpPubKey(BigInteger publicKey){
        String result = null;
        String pubHex = compressPubKey(publicKey);
        LogUtils.d("BHWalletUtils==>:","pubHex==>:"+pubHex);
        LogUtils.d("BHWalletUtils==>:","pubHex=base64=>:"+Base64.encode(pubHex.getBytes()));
        String sumHex = BH_PRE_PUB_KEY + pubHex;
        byte[] sumHexByte = HexUtils.toBytes(sumHex);
        try {
            byte[] converted = convertBits(sumHexByte, 8,5,true);
            result = bech32Encode("hbtcpub".getBytes(), converted);
        } catch (Exception e) {
            Log.w(TAG,"getBHUserDpPubKey Error");

        }
        return result;
    }

    public static String getPocUserAddress(String publicKey){
        String result = null;
        //String pubHex = compressPubKey(publicKey);
        //LogUtils.d("BHWalletUtils==>:","pubHex==>:"+pubHex);
        //LogUtils.d("BHWalletUtils==>:","pubHex=base64=>:"+Base64.encode(pubHex.getBytes()));
        //String sumHex = BH_PRE_PUB_KEY + publicKey;
        byte[] sumHexByte = HexUtils.toBytes(publicKey);
        try {
            byte[] converted = convertBits(sumHexByte, 8,5,true);
            result = bech32Encode("poc".getBytes(), converted);
        } catch (Exception e) {
            Log.w(TAG,"getBHUserDpPubKey Error");

        }
        return result;
    }

    public static String getBhexUserDpAddress(BigInteger publicKey){
        //公钥压缩
        String pubKey_compress = compressPubKey(publicKey);
        //公钥hash
        byte[] pubKeyHash = pubKeyCompressToHash(pubKey_compress);
        //base58编码
        String address = base58Address(pubKeyHash);
        return address;
    }

    public static String getBhexUserDpAddress2(BigInteger publicKey){
        //公钥压缩
        //String pubKey_compress = compressPubKey(publicKey);
        //公钥hash
        byte[] pubKeyHash = pubKeyCompressToHash(HexUtils.toHex(publicKey.toByteArray()));
        //base58编码
        String address = base58Address(pubKeyHash);
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

        byte[] bh_prefix = BHConstants.HBT;

        byte[] hash_new = new byte[bh_prefix.length + hash.length];

        System.arraycopy(bh_prefix, 0, hash_new, 0, bh_prefix.length);

        System.arraycopy(hash, 0, hash_new, bh_prefix.length, hash.length);

        byte[] check = BHBase58.checkSum(hash_new);

        byte[] hash_new2 = new byte[bh_prefix.length + hash.length + 4];

        System.arraycopy(bh_prefix, 0, hash_new2, 0,  bh_prefix.length);

        System.arraycopy(hash, 0, hash_new2,  bh_prefix.length, hash.length);

        System.arraycopy(check, 0, hash_new2,  bh_prefix.length + hash.length, 4);

        encode = BHBase58.encode(hash_new2);

        return encode;
    }


    public static String  signECDSA(ECKey.ECDSASignature sig){
        byte[] sigData = new byte[64];
        System.arraycopy(org.bitcoinj.core.Utils.bigIntegerToBytes(sig.r, 32), 0, sigData, 0, 32);
        System.arraycopy(Utils.bigIntegerToBytes(sig.s, 32), 0, sigData, 32, 32);
        String base64_signData = Base64.encode(sigData);
        return base64_signData;
    }


    public static void test(){
       /*byte[]bhbytes = Base58.decode("B");

       byte[]hbcbytes = Base58.decode("H");

       LogUtils.d("BHKey===>:","bhbytes==>:"+Arrays.toString(bhbytes)+"==hbcbytes=="+Arrays.toString(hbcbytes));*/

       String address = "B5AD24DD9E5D60E1F0734AF2D819FF9A198A2A38";
       String dp = getPocUserAddress(address);
       LogUtils.d("BHKey===>:","dp==>:"+dp);
    }




}
