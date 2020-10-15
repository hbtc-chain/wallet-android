package com.bhex.wallet.bh_main.my.ui.activity;

import androidx.appcompat.widget.AppCompatCheckedTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bhex.lib.uikit.util.ColorUtil;
import com.bhex.lib.uikit.util.PixelUtils;
import com.bhex.lib.uikit.widget.RecycleViewExtDivider;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.network.utils.ToastUtils;
import com.bhex.wallet.bh_main.R;
import com.bhex.wallet.bh_main.R2;
import com.bhex.wallet.bh_main.my.adapter.SecuritySettingAdapter;
import com.bhex.wallet.bh_main.my.helper.MyHelper;
import com.bhex.wallet.bh_main.my.ui.item.MyItem;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.manager.MMKVManager;
import com.bhex.wallet.common.manager.SecuritySettingManager;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

import butterknife.BindView;

/**
 * @author gongdongyang
 * 2020-10-15 20:27:22
 * 安全设置
 */
@Route(path = ARouterConfig.MY_Security_Setting,name = "安全设置")
public class SecuritySettingActivity extends BaseActivity implements SecuritySettingAdapter.CheckItemListener {

    @BindView(R2.id.rcy_choose_list)
    RecyclerView rcy_choose_list;

    private SecuritySettingAdapter mSecSetAdapter;
    private List<MyItem> mItems;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_security_setting;
    }

    @Override
    protected void initView() {
        mItems = MyHelper.getSecSettingItems(this);
        mSecSetAdapter = new SecuritySettingAdapter(mItems,this);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rcy_choose_list.setAdapter(mSecSetAdapter);
        rcy_choose_list.setLayoutManager(llm);

        RecycleViewExtDivider ItemDecoration = new RecycleViewExtDivider(
                this,LinearLayoutManager.VERTICAL,
                PixelUtils.dp2px(this,16),0,
                ColorUtil.getColor(this,R.color.global_divider_color));

        rcy_choose_list.addItemDecoration(ItemDecoration);
    }

    @Override
    protected void addEvent() {

    }

    @Override
    public void checkItemStatus(int position, boolean isChecked) {
        int count = 0;
        for(int i=0;i<mItems.size();i++){
            BaseViewHolder holder = (BaseViewHolder)rcy_choose_list.findViewHolderForAdapterPosition(i);
            AppCompatCheckedTextView ck = holder.getView(R.id.ck_select);
            if(ck.isChecked()){
                count++;
            }
        }

        if(count>0 && !isChecked){
            ToastUtils.showToast("不能同时开启");
            return;
        }

        BaseViewHolder holder = (BaseViewHolder)rcy_choose_list.findViewHolderForAdapterPosition(position);
        AppCompatCheckedTextView ck = holder.getView(R.id.ck_select);
        ck.toggle();

        switch (position){
            case 0:
                SecuritySettingManager.getInstance().save_every_time_pwd(ck.isChecked());
                break;
            case 1:
                SecuritySettingManager.getInstance().save_thirty_in_time(ck.isChecked());
                break;
            default:
                break;
        }
    }
}