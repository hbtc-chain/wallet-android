package com.bhex.wallet.bh_main.my.ui.fragment;


import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bhex.lib.uikit.widget.util.ColorUtil;
import com.bhex.lib.uikit.widget.util.PixelUtils;
import com.bhex.network.mvx.base.BaseFragment;
import com.bhex.tools.utils.NavitateUtil;
import com.bhex.wallet.bh_main.R;
import com.bhex.wallet.bh_main.R2;
import com.bhex.wallet.bh_main.my.adapter.MyAdapter;
import com.bhex.wallet.bh_main.my.helper.MyHelper;
import com.bhex.wallet.bh_main.my.ui.MyRecyclerViewDivider;
import com.bhex.wallet.bh_main.my.ui.activity.SettingActivity;
import com.bhex.wallet.bh_main.my.ui.item.MyItem;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author gdy
 * 2020-3-12
 * 我的
 */
public class MyFragment extends BaseFragment {


    @BindView(R2.id.recycler_my)
    RecyclerView recycler_my;

    @BindView(R2.id.iv_setting)
    AppCompatImageView iv_setting;

    private List<MyItem> mItems;

    private MyAdapter mMyAdapter;

    public MyFragment() {
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_my;
    }

    @Override
    protected void initView() {
        mItems = MyHelper.getAllItems(getYActivity());
        mMyAdapter = new MyAdapter(R.layout.item_my, mItems);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getYActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_my.setLayoutManager(layoutManager);
        mMyAdapter.setHasStableIds(true);


        MyRecyclerViewDivider myRecyclerDivider = new MyRecyclerViewDivider(
                getYActivity(), DividerItemDecoration.VERTICAL,
                PixelUtils.dp2px(getYActivity(), 8), ColorUtil.getColor(getYActivity(), R.color.gray_f9f9fb)
        );

        recycler_my.addItemDecoration(myRecyclerDivider);

        recycler_my.setAdapter(mMyAdapter);


    }


    @Override
    protected void addEvent() {
        mMyAdapter.setOnItemClickListener((adapter, view, position) -> {

        });
    }

    @OnClick({R2.id.iv_setting, R2.id.iv_default_man})
    public void onViewClicked(View view) {
        /*switch (view.getId()) {
            case R.id.iv_setting:
                break;
            case R.id.iv_default_man:
                break;
        }*/
        if(view.getId()==R.id.iv_setting){
            NavitateUtil.startActivity(getYActivity(), SettingActivity.class);
        }
    }
}
