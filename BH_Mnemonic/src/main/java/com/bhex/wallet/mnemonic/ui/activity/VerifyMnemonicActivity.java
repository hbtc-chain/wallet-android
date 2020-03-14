package com.bhex.wallet.mnemonic.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bhex.lib.uikit.widget.recyclerview.GridDividerItemDecoration;
import com.bhex.lib.uikit.widget.recyclerview.GridLayoutItemDecoration;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.network.utils.ToastUtils;
import com.bhex.tools.utils.LogUtils;
import com.bhex.tools.utils.NavitateUtil;
import com.bhex.wallet.common.base.BaseCacheActivity;
import com.bhex.wallet.mnemonic.R;
import com.bhex.wallet.mnemonic.R2;
import com.bhex.wallet.mnemonic.adapter.AboveMnemonicAdapter;
import com.bhex.wallet.mnemonic.adapter.UnderMnemonicAdapter;
import com.bhex.wallet.mnemonic.helper.MnemonicDataHelper;
import com.bhex.wallet.mnemonic.persenter.VerifyPresenter;
import com.bhex.wallet.mnemonic.ui.item.MnemonicItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;

/**
 * 验证助记词
 * 2020年3月8日14:30:00
 * @author gdy
 */
public class VerifyMnemonicActivity extends BaseCacheActivity<VerifyPresenter> {

    @BindView(R2.id.recycler_mnemonic_under)
    RecyclerView recycler_mnemonic_under;

    @BindView(R2.id.recycler_mnemonic_above)
    RecyclerView recycler_mnemonic_above;

    @BindView(R2.id.btn_start_bakcup)
    AppCompatButton btn_start_bakcup;


    UnderMnemonicAdapter underMnemonicAdapter;
    AboveMnemonicAdapter aboveMnemonicAdapter;

    private List<MnemonicItem> orginMnemonicItemList;

    private List<MnemonicItem> underMnemonicItemList;

    private List<MnemonicItem> aboverMnemonicItemList;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_verify_mnemonic;
    }

    @Override
    protected void initView() {

        orginMnemonicItemList = MnemonicDataHelper.makeMnemonic();
        underMnemonicItemList = MnemonicDataHelper.makeNewMnemonicList(orginMnemonicItemList);

        Collections.shuffle(underMnemonicItemList);

        aboverMnemonicItemList = MnemonicDataHelper.makeAboveMnemonicList();

        GridLayoutManager layoutManager = new GridLayoutManager(this,3);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);

        recycler_mnemonic_under.setLayoutManager(layoutManager);

        underMnemonicAdapter = new UnderMnemonicAdapter(R.layout.item_mnemonic,underMnemonicItemList);
        recycler_mnemonic_under.setAdapter(underMnemonicAdapter);

        underMnemonicAdapter.setOnItemClickListener((adapter, view, position) -> {
            MnemonicItem underMnemonicItem =  underMnemonicItemList.get(position);
            if(!underMnemonicItem.isSelected()){
                underMnemonicItem.setSelected(true);
                underMnemonicItem.setIndex(aboverMnemonicItemList.size()+1);
                int aboveIndex = MnemonicDataHelper.getAboveFillIndex(aboverMnemonicItemList);
                MnemonicItem abvoeMnemonicItem  = aboverMnemonicItemList.get(aboveIndex);
                abvoeMnemonicItem.setWord(underMnemonicItem.getWord());
            }

            aboveMnemonicAdapter.notifyDataSetChanged();
            underMnemonicAdapter.notifyDataSetChanged();

            getPresenter().verifyMnmonic(aboverMnemonicItemList,orginMnemonicItemList,btn_start_bakcup);
        });

        GridLayoutManager unlayoutManager = new GridLayoutManager(this,3);
        unlayoutManager.setOrientation(GridLayoutManager.VERTICAL);

        recycler_mnemonic_above.setLayoutManager(unlayoutManager);
        recycler_mnemonic_above.addItemDecoration(new GridLayoutItemDecoration(this,R.drawable.item_divider));

        aboveMnemonicAdapter = new AboveMnemonicAdapter(R.layout.item_mnemonic_above,aboverMnemonicItemList,orginMnemonicItemList);
        recycler_mnemonic_above.setAdapter(aboveMnemonicAdapter);

        aboveMnemonicAdapter.setOnItemClickListener((adapter, view, position) -> {

            MnemonicItem aboveItem =  aboverMnemonicItemList.get(position);
            if(TextUtils.isEmpty(aboveItem.getWord())){
                return;
            }

            int index = MnemonicDataHelper.getUnderFillIndex(underMnemonicItemList,aboveItem);

            MnemonicItem underItem =  underMnemonicItemList.get(index);
            underItem.setSelected(false);

            aboveItem.setWord("");

            aboveMnemonicAdapter.notifyDataSetChanged();
            underMnemonicAdapter.notifyDataSetChanged();
            getPresenter().verifyMnmonic(aboverMnemonicItemList,orginMnemonicItemList,btn_start_bakcup);

        });

    }

    @Override
    protected void initPresenter() {
        mPresenter = new VerifyPresenter(this);
    }

    @Override
    protected void addEvent() {
        btn_start_bakcup.setOnClickListener(v -> {
            NavitateUtil.startMainActivity(VerifyMnemonicActivity.this);
            finish();
        });
    }
}
