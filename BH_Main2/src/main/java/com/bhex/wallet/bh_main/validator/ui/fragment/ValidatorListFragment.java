package com.bhex.wallet.bh_main.validator.ui.fragment;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bhex.lib.uikit.widget.EmptyLayout;
import com.bhex.lib.uikit.widget.editor.SimpleTextWatcher;
import com.bhex.lib.uikit.widget.recyclerview.MyLinearLayoutManager;
import com.bhex.network.base.LoadingStatus;
import com.bhex.network.mvx.base.BaseFragment;
import com.bhex.wallet.bh_main.R;
import com.bhex.wallet.bh_main.R2;
import com.bhex.wallet.bh_main.validator.adapter.ValidatorAdapter;
import com.bhex.wallet.bh_main.validator.presenter.ValidatorListFragmentPresenter;
import com.bhex.wallet.bh_main.validator.viewmodel.ValidatorViewModel;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.model.ValidatorInfo;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class ValidatorListFragment extends BaseFragment<ValidatorListFragmentPresenter> {

    public static String KEY_VALIDATOR_TYPE = "validator_type";
    private int mValidatorType = 0;
    ValidatorAdapter mValidatorAdapter;
    @BindView(R2.id.ed_search_content)
    AppCompatEditText ed_search_content;
    @BindView(R2.id.recycler_validator)
    SwipeRecyclerView recycler_validator;

    @BindView(R2.id.swipeRefresh)
    SmartRefreshLayout swipeRefresh;

    @BindView(R2.id.empty_layout)
    EmptyLayout empty_layout;

    List<ValidatorInfo> mOriginValidatorInfoList;
    List<ValidatorInfo> mValidatorInfoList;

    ValidatorViewModel mValidatorViewModel;

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

        Bundle arguments = getArguments();
        if (arguments != null) {
            mValidatorType = arguments.getInt(KEY_VALIDATOR_TYPE, 0);
        }
        mValidatorViewModel = ViewModelProviders.of(this).get(ValidatorViewModel.class);
        mValidatorAdapter = new ValidatorAdapter(mValidatorType, R.layout.item_validator, mValidatorInfoList);
        recycler_validator.setAdapter(mValidatorAdapter);
    }

    @Override
    protected void addEvent() {
        empty_layout.showProgess();
        mValidatorViewModel.validatorsLiveData.observe(this, ldm -> {
            swipeRefresh.finishRefresh();
            if (ldm.loadingStatus == LoadingStatus.SUCCESS) {
                empty_layout.loadSuccess();
                updateRecord(ldm.getData());
            } else {
                if(mOriginValidatorInfoList==null || mOriginValidatorInfoList.size()==0){
                    empty_layout.showNeterror(new EmptyLayout.onReloadClickListener() {
                        @Override
                        public void onClick(View view) {
                            getRecord(true);
                        }
                    });
                }
            }
        });
        swipeRefresh.setOnRefreshListener(refreshLayout1 -> {
            getRecord(false);
        });
        ed_search_content.addTextChangedListener(ValidatorTextWatcher);
        //点击事件
        mValidatorAdapter.setOnItemClickListener((adapter, view, position) -> {
            ValidatorInfo item = mValidatorAdapter.getData().get(position);
            ARouter.getInstance().build(ARouterConfig.Validator_Detail)
                    .withObject("validatorInfo", item)
                    .withInt("valid", mValidatorType)
                    .navigation();
        });
    }

    private void getRecord(boolean showDialog) {
        if (showDialog) {
            empty_layout.showProgess();
        }
        mValidatorViewModel.getValidatorInfos(getYActivity(),
                mValidatorType);
    }

    @Override
    public void onResume() {
        super.onResume();
        getRecord(true);
    }

    public void updateRecord(List<ValidatorInfo> datas) {
        mOriginValidatorInfoList = datas;
        List<ValidatorInfo> result = new ArrayList<>();

        String searchContent = ed_search_content.getText().toString().trim();
        if (mOriginValidatorInfoList != null) {
            if (TextUtils.isEmpty(searchContent)) {
                for (int i = 0; i < mOriginValidatorInfoList.size(); i++) {
                    ValidatorInfo item = mOriginValidatorInfoList.get(i);
                    result.add(item);
                }

            } else {
                for (int i = 0; i < mOriginValidatorInfoList.size(); i++) {
                    ValidatorInfo item = mOriginValidatorInfoList.get(i);
                    if (item.getDescription() != null && item.getDescription().getMoniker().toLowerCase().contains(searchContent.toLowerCase())) {
                        result.add(item);
                    }
                }
            }
        }
        if (result.size() > 0) {
            empty_layout.loadSuccess();
        } else {
            empty_layout.showNoData();
        }
        mValidatorAdapter.getData().clear();
        mValidatorAdapter.addData(result);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ValidatorListFragmentPresenter(getYActivity());
    }

    private SimpleTextWatcher ValidatorTextWatcher = new SimpleTextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            super.afterTextChanged(s);
            updateRecord(mOriginValidatorInfoList);

        }
    };

}
