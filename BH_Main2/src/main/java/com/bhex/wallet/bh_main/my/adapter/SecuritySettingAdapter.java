package com.bhex.wallet.bh_main.my.adapter;

import android.view.View;

import com.bhex.wallet.bh_main.R;
import com.bhex.wallet.bh_main.my.model.SecuritySettingItem;
import com.bhex.wallet.bh_main.my.ui.item.MyItem;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/3/12
 * Time: 11:19
 */
public class SecuritySettingAdapter extends BaseQuickAdapter<SecuritySettingItem, BaseViewHolder> {

    private CheckItemListener mCheckItemListener;

    public SecuritySettingAdapter(@Nullable List<SecuritySettingItem> data, CheckItemListener listener) {
        super(R.layout.item_language, data);
        this.mCheckItemListener = listener;
        addChildClickViewIds(R.id.ck_select);

    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, @Nullable SecuritySettingItem item) {
        helper.setText(R.id.tv_language,item.title);
        helper.getView(R.id.iv_choosed).setVisibility(View.INVISIBLE);
        if(item.isSelected){
            helper.getView(R.id.iv_choosed).setVisibility(View.VISIBLE);
        }
        //viewHolder.getView(R.id.iv_arrow).setVisibility(View.INVISIBLE);
        /*viewHolder.setVisible(R.id.sc_theme,false);
        AppCompatCheckedTextView ck = viewHolder.getView(R.id.ck_select);
        ck.setChecked(myItem.isArrow);
        ck.setVisibility(View.VISIBLE);
        ck.setOnClickListener(v -> {
            if(mCheckItemListener!=null){
                mCheckItemListener.checkItemStatus(getItemPosition(myItem),ck.isChecked());
            }
        });*/
        /*helper.setText(R.id.tv_language,item.getFullName());
        helper.getView(R.id.iv_choosed).setVisibility(View.INVISIBLE);
        if(item.isSelected()){
            helper.getView(R.id.iv_choosed).setVisibility(View.VISIBLE);
        }*/
    }

    public interface  CheckItemListener{
        public void checkItemStatus(int position, boolean isChecked);
    }
}
