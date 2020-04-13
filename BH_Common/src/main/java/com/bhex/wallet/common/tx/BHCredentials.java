package com.bhex.wallet.common.tx;

import com.bhex.network.utils.Base64;
import com.bhex.tools.crypto.HexUtils;

import org.bitcoinj.core.ECKey;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/4/2
 * Time: 22:59
 */
public class BHCredentials {
    public String hexPubkey;
    public String base64Pubkey;
    public String hexPriKey;
    public ECKey ecKey;

    public static BHCredentials createBHCredentials(String hexPK){
        BHCredentials bhCredentials = new BHCredentials();
        bhCredentials.ecKey =  ECKey.fromPrivate(HexUtils.toBytes(hexPK),true);
        bhCredentials.base64Pubkey = Base64.encode(bhCredentials.ecKey.getPubKey());
        bhCredentials.hexPriKey = hexPK;
        bhCredentials.hexPubkey = HexUtils.toHex(bhCredentials.ecKey.getPubKey());
        return bhCredentials;
    }
}
