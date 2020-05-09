package com.bhex.wallet.bh_main.validator.ui.fragment;


import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bhex.lib.uikit.widget.GradientTabLayout;
import com.bhex.network.mvx.base.BaseFragment;
import com.bhex.tools.constants.BHConstants;
import com.bhex.wallet.bh_main.R;
import com.bhex.wallet.bh_main.R2;
import com.bhex.wallet.bh_main.validator.presenter.ValidatorFragmentPresenter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author zhou chang
 * 2020-3-12
 * 验证人
 */
public class ValidatorFragment extends BaseFragment<ValidatorFragmentPresenter> {


//
//    @BindView(R2.id.tv_create_validator)
//    AppCompatTextView tv_create_validator;
    @BindView(R2.id.tab)
    GradientTabLayout tab;
    @BindView(R2.id.viewPager)
    ViewPager viewPager;
    @BindView(R2.id.appBarLayout)
    AppBarLayout appBarLayout;

    public ValidatorFragment() {
        // Required empty public constructor
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_validator;
    }

    @Override
    protected void initView() {
        initTab();
    }

    @Override
    protected void addEvent() {
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            //verticalOffset是当前appbarLayout的高度与最开始appbarlayout高度的差，向上滑动的话是负数
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(verticalOffset<0){
                    appBarLayout.setBackgroundColor(getResources().getColor(R.color.app_bg));
                } else {
                    appBarLayout.setBackgroundResource(R.drawable.bg_validator_header);
                }
            }
        });
    }


    @Override
    protected void initPresenter() {
        mPresenter = new ValidatorFragmentPresenter(getYActivity());
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initTab() {
        List<Pair<String, Fragment>> items = new ArrayList<>();
        ValidatorListFragment  validListFragment= new ValidatorListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ValidatorListFragment.KEY_VALIDATOR_TYPE, BHConstants.VALIDATOR_VALID);
        validListFragment.setArguments(bundle);

        ValidatorListFragment invalidListFragment = new ValidatorListFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putInt(ValidatorListFragment.KEY_VALIDATOR_TYPE,  BHConstants.VALIDATOR_INVALID);
        invalidListFragment.setArguments(bundle1);

        items.add(new Pair<String, Fragment>(getString(R.string.tab_valid), validListFragment));
        items.add(new Pair<String, Fragment>(getString(R.string.tab_invalid), invalidListFragment));

        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public int getCount() {
                return items.size();
            }

            @Override
            public Fragment getItem(int position) {
                return items.get(position).second;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {//添加标题Tab
                return items.get(position).first;
            }
        });

        tab.setViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

//    @OnClick({R2.id.tv_create_validator})
//    public void onViewClicked(View view) {
//        if(view.getId()==R.id.tv_create_validator){
//
//        }
//    }


}
