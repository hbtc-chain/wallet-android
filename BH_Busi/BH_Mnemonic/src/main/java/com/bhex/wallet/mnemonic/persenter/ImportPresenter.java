package com.bhex.wallet.mnemonic.persenter;

import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.network.mvx.base.BasePresenter;
import com.bhex.tools.utils.LogUtils;
import com.bhex.wallet.common.enums.MAKE_WALLET_TYPE;
import com.bhex.wallet.mnemonic.R;
import com.bhex.wallet.mnemonic.ui.item.FunctionItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java8.util.stream.Collectors;
import java8.util.stream.IntStreams;
import java8.util.stream.StreamSupport;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/3/22
 * Time: 20:45
 */
public class ImportPresenter extends BasePresenter {

    public ImportPresenter(BaseActivity activity) {
        super(activity);
    }

    /**
     * 获取导入方式
     * @return
     */
    public List<FunctionItem> loadImportList(){
        List<FunctionItem> list = new ArrayList<>();

        FunctionItem wordItem = new FunctionItem(MAKE_WALLET_TYPE.导入助记词.getWay(),R.mipmap.ic_import_word,getActivity().getString(R.string.import_mnemonic),
                getActivity().getString(R.string.hint_import_way));

        list.add(wordItem);

        FunctionItem ksItem = new FunctionItem(MAKE_WALLET_TYPE.导入KS.getWay(),R.mipmap.ic_import_ks,getActivity().getString(R.string.import_keystore),
                getActivity().getString(R.string.hint_import_way));

        list.add(ksItem);

        FunctionItem privateKeyItem = new FunctionItem(MAKE_WALLET_TYPE.PK.getWay(),R.mipmap.ic_import_pk,
                getActivity().getString(R.string.import_private_key),getActivity().getString(R.string.hint_import_way));

        list.add(privateKeyItem);
        return list;
    }

    //
    public List<FunctionItem> loadFunctionList(){

        List<FunctionItem> list = new ArrayList<>();
        list.add(0,new FunctionItem(getActivity().getString(R.string.string_import),FunctionItem.TYPE_TITLE));

        FunctionItem wordItem = new FunctionItem(MAKE_WALLET_TYPE.导入助记词.getWay(),R.mipmap.ic_import_word,getActivity().getString(R.string.import_mnemonic),
                "");
        list.add(wordItem);

        FunctionItem ksItem = new FunctionItem(MAKE_WALLET_TYPE.导入KS.getWay(),R.mipmap.ic_import_ks,getActivity().getString(R.string.import_keystore),
                "");
        list.add(ksItem);

        FunctionItem privateKeyItem = new FunctionItem(MAKE_WALLET_TYPE.PK.getWay(),R.mipmap.ic_import_pk,
                getActivity().getString(R.string.import_private_key),"");
        list.add(privateKeyItem);

        list.add(4,new FunctionItem(getActivity().getString(R.string.create),FunctionItem.TYPE_TITLE));

        FunctionItem createItem = new FunctionItem(MAKE_WALLET_TYPE.创建助记词.getWay(),R.mipmap.ic_create_wallet,
                getActivity().getString(R.string.create_wallet_account),"");
        list.add(createItem);
        return list;
    }



}
