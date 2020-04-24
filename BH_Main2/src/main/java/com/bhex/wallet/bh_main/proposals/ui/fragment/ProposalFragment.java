package com.bhex.wallet.bh_main.proposals.ui.fragment;


import android.text.Editable;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
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
import com.bhex.wallet.bh_main.proposals.adapter.ProposalAdapter;
import com.bhex.wallet.bh_main.proposals.presenter.ProposalFragmentPresenter;
import com.bhex.wallet.bh_main.proposals.viewmodel.ProposalViewModel;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.model.ProposalInfo;
import com.bhex.wallet.common.model.ProposalQueryResult;
import com.bhex.wallet.common.model.ValidatorInfo;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author zhou chang
 * 2020-3-12
 * 验证人
 */
public class ProposalFragment extends BaseFragment<ProposalFragmentPresenter> {


    @BindView(R2.id.tv_create_proposal)
    AppCompatTextView tv_create_proposal;
    @BindView(R2.id.ed_search_content)
    AppCompatEditText ed_search_content;
    @BindView(R2.id.recycler_proposal)
    SwipeRecyclerView recycler_proposal;

    @BindView(R2.id.swipeRefresh)
    SmartRefreshLayout smartRefreshLayout;

    @BindView(R2.id.empty_layout)
    EmptyLayout empty_layout;
    ProposalViewModel mProposalViewModel;
    ProposalAdapter mProposalAdapter;
    List<ProposalInfo> mOriginProposalInfoList;
    List<ProposalInfo> mProposalInfoList;

    private int mCurrentPage = 1;

    public ProposalFragment() {
        // Required empty public constructor
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_proposal;
    }

    @Override
    protected void initView() {

        LinearLayoutManager layoutManager = new MyLinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_proposal.setLayoutManager(layoutManager);
        recycler_proposal.setNestedScrollingEnabled(true);

        mProposalViewModel = ViewModelProviders.of(this).get(ProposalViewModel.class);
        mProposalAdapter = new ProposalAdapter(R.layout.item_proposal, mProposalInfoList);

        recycler_proposal.setAdapter(mProposalAdapter);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ProposalFragmentPresenter(getYActivity());
    }


    @OnClick({R2.id.tv_create_proposal})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.tv_create_proposal) {

            ARouter.getInstance().build(ARouterConfig.Create_Proposal)
                    .navigation();
        }
    }


    @Override
    protected void addEvent() {
//        mProposalAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
//            @Override
//            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//                List<ProposalInfo> data = adapter.getData();
//                if (data != null) {
//                    final ProposalInfo item = data.get(position);
//                    if (view.getId() == R.id.tv_view) {
//                        ARouter.getInstance().build(ARouterConfig.Proposal_Detail)
//                                .withObject("proposalInfo", item)
//                                .navigation();
//                    }
//                }
//            }
//        });
        //点击事件
        mProposalAdapter.setOnItemClickListener((adapter, view, position) -> {
            List<ProposalInfo> data = mProposalAdapter.getData();
            if (data != null) {
                final ProposalInfo item = data.get(position);

                ARouter.getInstance().build(ARouterConfig.Proposal_Detail)
                        .withObject("proposalInfo", item)
                        .navigation();
            }
        });
        mProposalViewModel.proposalLiveData.observe(this, ldm -> {
            smartRefreshLayout.finishRefresh();
            if (ldm.loadingStatus == LoadingStatus.SUCCESS) {
                if (ldm.getData() != null && ldm.getData().getProposals() != null) {
                    if (ldm.getData().getTotal() < mCurrentPage * ldm.getData().getPage_size()) {
                        smartRefreshLayout.finishLoadMoreWithNoMoreData();
                    }
                } else {
                    smartRefreshLayout.finishLoadMore(true);
                }
                empty_layout.loadSuccess();
                updateOriginRecord(ldm.getData());
            } else {
                smartRefreshLayout.finishLoadMore(false);
                empty_layout.showNeterror(view -> {

                });
            }
        });
        smartRefreshLayout.setOnRefreshListener(refreshLayout1 -> {
            mCurrentPage = 1;
            getRecord(false, mCurrentPage);
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                getRecord(false, mCurrentPage + 1);

            }
        });
        ed_search_content.addTextChangedListener(ValidatorTextWatcher);
    }

    private void updateOriginRecord(ProposalQueryResult data) {
        if (data != null && data.getProposals() != null) {
            if (data.getTotal() < mCurrentPage * data.getPage_size()) {
                smartRefreshLayout.finishLoadMoreWithNoMoreData();
            }
            if (data.getPage() == 1) {
                mOriginProposalInfoList = data.getProposals();
            } else {
                mOriginProposalInfoList.addAll(data.getProposals());
            }
            mCurrentPage = data.getPage();
            updateRecord(mOriginProposalInfoList);
        }
    }

    private void getRecord(boolean showDialog, int page) {
        if (showDialog) {
            empty_layout.showProgess();
        }
        mProposalViewModel.queryProposals(getYActivity(), page);
    }

    @Override
    public void onResume() {
        super.onResume();
        getRecord(true, mCurrentPage);
    }

    public void updateRecord(List<ProposalInfo> datas) {
        mOriginProposalInfoList = datas;
        List<ProposalInfo> result = new ArrayList<>();

        String searchContent = ed_search_content.getText().toString().trim();
        if (mOriginProposalInfoList != null) {
            if (TextUtils.isEmpty(searchContent)) {
                for (int i = 0; i < mOriginProposalInfoList.size(); i++) {
                    ProposalInfo item = mOriginProposalInfoList.get(i);
                    result.add(item);
                }

            } else {
                for (int i = 0; i < mOriginProposalInfoList.size(); i++) {
                    ProposalInfo item = mOriginProposalInfoList.get(i);
                    if (item.getDescription() != null && item.getTitle().toLowerCase().contains(searchContent.toLowerCase())) {
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
        mProposalInfoList = result;
        mProposalAdapter.setNewData(mProposalInfoList);
    }

    private SimpleTextWatcher ValidatorTextWatcher = new SimpleTextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            super.afterTextChanged(s);
            updateRecord(mOriginProposalInfoList);

        }
    };
}
