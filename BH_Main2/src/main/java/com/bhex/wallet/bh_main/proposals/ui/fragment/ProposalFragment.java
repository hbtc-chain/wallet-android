package com.bhex.wallet.bh_main.proposals.ui.fragment;


import android.text.Editable;
import android.text.TextUtils;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.bhex.tools.utils.LogUtils;
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
import com.google.android.material.appbar.AppBarLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import rx.functions.Action1;

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

    @BindView(R2.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R2.id.iv_proposal_header)
    ImageView iv_proposal_header;

    ProposalViewModel mProposalViewModel;
    ProposalAdapter mProposalAdapter;
    List<ProposalInfo> mOriginProposalInfoList;
    List<ProposalInfo> mProposalInfoList;

    private int mCurrentPage = 1;
    private int mQueryCurrentPage = 1;

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
        mOriginProposalInfoList = new ArrayList<>();
        mProposalAdapter = new ProposalAdapter(R.layout.item_proposal, mOriginProposalInfoList);

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
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            //verticalOffset是当前appbarLayout的高度与最开始appbarlayout高度的差，向上滑动的话是负数
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(verticalOffset<0){
                    iv_proposal_header.setVisibility(View.GONE);
                } else {
                    iv_proposal_header.setVisibility(View.VISIBLE);
                }
            }
        });
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
                smartRefreshLayout.finishLoadMore(true);
                if (ldm.getData() != null) {
                    mCurrentPage = ldm.getData().getPage();
                    if (ldm.getData().getTotal() < mCurrentPage * ldm.getData().getPage_size()) {
                        smartRefreshLayout.setNoMoreData(true);
                    }
                }
                empty_layout.loadSuccess();
                updateOriginRecord(ldm.getData());
            } else {
                smartRefreshLayout.finishLoadMore(false);


                if(mOriginProposalInfoList==null || mOriginProposalInfoList.size()==0){
                    empty_layout.showNeterror(new EmptyLayout.onReloadClickListener() {
                        @Override
                        public void onClick(View view) {
                            getRecord(true, mQueryCurrentPage);
                        }
                    });
                }
            }
        });
        smartRefreshLayout.setOnRefreshListener(refreshLayout1 -> {
            mQueryCurrentPage = 1;
            mCurrentPage = 1;
            smartRefreshLayout.resetNoMoreData();
            getRecord(false, mQueryCurrentPage);
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mQueryCurrentPage = mCurrentPage + 1;
                getRecord(false, mQueryCurrentPage);

            }
        });
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) {
                ed_search_content.addTextChangedListener(new SimpleTextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        super.afterTextChanged(s);
                        emitter.onNext(s.toString());
                    }
                });

            }
        }).sample(500, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
            @Override
            public void accept(String value) {
                mQueryCurrentPage = 1;
                mCurrentPage = 1;
                smartRefreshLayout.resetNoMoreData();
                getRecord(true, mQueryCurrentPage);

            }
        });
    }

    private void updateOriginRecord(ProposalQueryResult data) {
        if (data != null && data.getProposals() != null) {
            if (data.getPage() == 1) {
                mOriginProposalInfoList.clear();
                mOriginProposalInfoList.addAll(data.getProposals());
            } else {
                mOriginProposalInfoList.addAll(data.getProposals());
            }

            if (mOriginProposalInfoList != null && mOriginProposalInfoList.size() > 0) {
                empty_layout.loadSuccess();
            } else {
                empty_layout.showNoData();
            }
            mProposalAdapter.notifyDataSetChanged();
//            mProposalAdapter.setNewData(mOriginProposalInfoList);
        }
    }

    private void getRecord(boolean showDialog, int page) {
        if (showDialog) {
            empty_layout.showProgess();
        }
        String searchContent = ed_search_content.getText().toString().trim();
        mProposalViewModel.queryProposals(getYActivity(), page, searchContent);
    }

    @Override
    public void onResume() {
        super.onResume();
        getRecord(true, mCurrentPage);
    }
}
