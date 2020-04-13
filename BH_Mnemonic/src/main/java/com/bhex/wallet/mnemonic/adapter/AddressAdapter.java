package com.bhex.wallet.mnemonic.adapter;

import android.widget.CompoundButton;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatCheckedTextView;
import androidx.appcompat.widget.AppCompatTextView;

import com.bhex.lib.uikit.util.ColorUtil;
import com.bhex.tools.utils.LogUtils;
import com.bhex.wallet.common.db.entity.BHWallet;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.mnemonic.R;
import com.bhex.wallet.mnemonic.ui.item.BHWalletItem;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/3/20
 * Time: 16:15
 */
public class AddressAdapter extends BaseQuickAdapter<BHWalletItem, BaseViewHolder> {

    private AddressCheckChangeListener mCheckChangeListener;

    public AddressAdapter(int layoutResId, @Nullable List<BHWalletItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder viewHolder, @Nullable BHWalletItem bhWallet) {

        int position = getItemPosition(bhWallet);

        viewHolder.setText(R.id.tv_address,bhWallet.address);


        BHWallet currentWallet = BHUserManager.getInstance().getCurrentBhWallet();
        AppCompatCheckedTextView ck = viewHolder.getView(R.id.ck_ok);
        AppCompatTextView tv_address = viewHolder.getView(R.id.tv_address);


        //LogUtils.d("AddressAdapter==>:","=position=");
        ck.setChecked(false);
        tv_address.setTextColor(ColorUtil.getColor(getContext(),R.color.gray_ACB5C3));
        if(bhWallet.isDefault==1 ){
            ck.setChecked(true);
            tv_address.setTextColor(ColorUtil.getColor(getContext(),R.color.blue));
        }else{
            ck.setChecked(false);
            tv_address.setTextColor(ColorUtil.getColor(getContext(),R.color.main_text_black));

        }
        ck.setOnClickListener(v -> {

            if(mCheckChangeListener!=null){
                mCheckChangeListener.onCheckedChanged(position,ck.isChecked());
            }
        });
        /*ck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (getWeakRecyclerView().get().isComputingLayout()) {
                    getWeakRecyclerView().get().post(()->{
                        if(mCheckChangeListener!=null){
                            mCheckChangeListener.onCheckedChanged(position,isChecked);
                        }
                    });
                }else{
                    if(mCheckChangeListener!=null){
                        mCheckChangeListener.onCheckedChanged(position,isChecked);
                    }
                }
                *//*if(mCheckChangeListener!=null){
                    mCheckChangeListener.onCheckedChanged(position,isChecked);
                }*//*
            }
        });*/

    }

    public void setCheckChangeListener(AddressCheckChangeListener mCheckChangeListener) {
        this.mCheckChangeListener = mCheckChangeListener;
    }

    public interface AddressCheckChangeListener{
        void onCheckedChanged(int position,boolean isChecked);
    }
}
