package com.bhex.wallet.bh_main.validator.ui.fragment;


import android.os.Bundle;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bhex.lib.uikit.util.ColorUtil;
import com.bhex.lib.uikit.widget.RecycleViewDivider;
import com.bhex.lib.uikit.widget.editor.SimpleTextWatcher;
import com.bhex.lib.uikit.widget.recyclerview.MyLinearLayoutManager;
import com.bhex.network.mvx.base.BaseFragment;
import com.bhex.wallet.bh_main.R;
import com.bhex.wallet.bh_main.R2;
import com.bhex.wallet.bh_main.validator.adapter.ValidatorAdapter;
import com.bhex.wallet.bh_main.validator.presenter.ValidatorFragmentPresenter;
import com.bhex.wallet.bh_main.validator.presenter.ValidatorListFragmentPresenter;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.model.BHBalance;
import com.bhex.wallet.common.model.ValidatorInfo;
import com.google.android.material.tabs.TabLayout;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class ValidatorListFragment extends BaseFragment<ValidatorListFragmentPresenter> {

    public static String KEY_VALIDATOR_TYPE = "validator_type";

    ValidatorAdapter mValidatorAdapter;
    @BindView(R2.id.ed_search_content)
    AppCompatEditText ed_search_content;
    @BindView(R2.id.recycler_validator)
    SwipeRecyclerView recycler_validator;

    List<ValidatorInfo> mOriginValidatorInfoList;
    List<ValidatorInfo> mValidatorInfoList;
    public ValidatorListFragment() {
    }



    @Override
    public int getLayoutId() {
        return R.layout.fragment_validator_list;
    }

    @Override
    protected void initView() {

        LinearLayoutManager layoutManager = new MyLinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_validator.setLayoutManager(layoutManager);
        recycler_validator.setNestedScrollingEnabled(true);


        mOriginValidatorInfoList = new ArrayList<>();
        mOriginValidatorInfoList.add(new ValidatorInfo());
        mOriginValidatorInfoList.add(new ValidatorInfo());
        mOriginValidatorInfoList.add(new ValidatorInfo());
        mOriginValidatorInfoList.add(new ValidatorInfo());
        mValidatorInfoList.addAll(mOriginValidatorInfoList);
        mValidatorAdapter = new ValidatorAdapter(R.layout.item_validator, mValidatorInfoList);
        recycler_validator.setAdapter(mValidatorAdapter);
    }

    @Override
    protected void addEvent() {
        ed_search_content.addTextChangedListener(ValidatorTextWatcher);
        //点击事件
        mValidatorAdapter.setOnItemClickListener((adapter, view, position) -> {
            ValidatorInfo item =  mValidatorAdapter.getData().get(position);
            ARouter.getInstance().build(ARouterConfig.Validator_Detail)
                    .withObject("validatorInfo",item)
                    .navigation();
        });
    }


    @Override
    protected void initPresenter() {
        mPresenter = new ValidatorListFragmentPresenter(getYActivity());
    }
    private SimpleTextWatcher ValidatorTextWatcher = new SimpleTextWatcher(){
        @Override
        public void afterTextChanged(Editable s) {
            super.afterTextChanged(s);
            String searchContent = ed_search_content.getText().toString().trim();
            List<ValidatorInfo> result = new ArrayList<>();

            if(TextUtils.isEmpty(searchContent)){
                for(int i=0;i<mOriginValidatorInfoList.size();i++){
                    ValidatorInfo item = mOriginValidatorInfoList.get(i);
                    result.add(item);
                }

            }else{
                for(int i=0;i<mOriginValidatorInfoList.size();i++){
                    ValidatorInfo item = mOriginValidatorInfoList.get(i);
                    if(item.getAddress().toLowerCase().contains(searchContent.toLowerCase())){
                        result.add(item);
                    }
                }
            }
            mValidatorAdapter.getData().clear();
            mValidatorAdapter.addData(result);

        }
    };
}
