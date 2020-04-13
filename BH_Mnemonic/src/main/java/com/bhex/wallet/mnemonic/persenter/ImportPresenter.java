package com.bhex.wallet.mnemonic.persenter;

import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.network.mvx.base.BasePresenter;
import com.bhex.wallet.mnemonic.R;
import com.bhex.wallet.mnemonic.ui.item.ImportItem;

import java.util.ArrayList;
import java.util.List;

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
    public List<ImportItem> loadAllItem(){
        List<ImportItem> list = new ArrayList<>();

        ImportItem wordItem = new ImportItem(getActivity().getString(R.string.import_mnemonic),
                getActivity().getString(R.string.hint_import_way),R.mipmap.ic_import_word);

        list.add(wordItem);

        ImportItem ksItem = new ImportItem(getActivity().getString(R.string.import_keystore),
                getActivity().getString(R.string.hint_import_way),R.mipmap.ic_import_ks);

        list.add(ksItem);

        ImportItem privateKeyItem = new ImportItem(getActivity().getString(R.string.import_private_key),
                getActivity().getString(R.string.hint_import_way),R.mipmap.ic_import_pk);

        list.add(privateKeyItem);
        return list;
    }
}
