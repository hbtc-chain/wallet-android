package com.bhex.wallet.bh_main.validator.ui.activity;

import android.os.Bundle;
import android.util.Pair;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bhex.lib.uikit.widget.GradientTabLayout;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.tools.constants.BHConstants;
import com.bhex.wallet.bh_main.R;
import com.bhex.wallet.bh_main.R2;
import com.bhex.wallet.bh_main.validator.presenter.ValidatorFragmentPresenter;
import com.bhex.wallet.bh_main.validator.ui.fragment.ValidatorListFragment;
import com.bhex.wallet.common.config.ARouterConfig;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author gongdongyang
 * 2020-9-2 15:50:31
 */
@Route(path = ARouterConfig.Validator.Validator_Index)
public class ValidatorIndexActivity extends BaseActivity<ValidatorFragmentPresenter> {
    @BindView(R2.id.tab)
    GradientTabLayout tab;
    @BindView(R2.id.viewPager)
    ViewPager viewPager;
    @BindView(R2.id.tv_center_title)
    AppCompatTextView tv_center_title;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_validator_index;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ValidatorFragmentPresenter(this);
    }

    @Override
    protected void initView() {
        tv_center_title.setText(getResources().getString(R.string.tab_validator));
        initTab();
    }

    @Override
    protected void addEvent() {

    }

    private void initTab() {
        List<Pair<String, Fragment>> items = new ArrayList<>();
        ValidatorListFragment validListFragment = new ValidatorListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ValidatorListFragment.KEY_VALIDATOR_TYPE, BHConstants.VALIDATOR_VALID);
        validListFragment.setArguments(bundle);

        ValidatorListFragment invalidListFragment = new ValidatorListFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putInt(ValidatorListFragment.KEY_VALIDATOR_TYPE, BHConstants.VALIDATOR_INVALID);
        invalidListFragment.setArguments(bundle1);

        items.add(new Pair<String, Fragment>(getString(R.string.tab_valid), validListFragment));
        items.add(new Pair<String, Fragment>(getString(R.string.tab_invalid), invalidListFragment));

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
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
}
