package com.bhex.wallet.bh_main.my.ui.fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bhex.lib.uikit.widget.EmptyLayout;
import com.bhex.network.base.LoadDataModel;
import com.bhex.network.base.LoadingStatus;
import com.bhex.network.mvx.base.BaseFragment;
import com.bhex.tools.utils.LogUtils;
import com.bhex.wallet.bh_main.R;
import com.bhex.wallet.bh_main.R2;
import com.bhex.wallet.bh_main.my.adapter.MessageAdapter;
import com.bhex.wallet.bh_main.my.model.BHMessage;
import com.bhex.wallet.bh_main.my.viewmodel.MessageViewModel;
import com.bhex.wallet.common.model.BHPage;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends BaseFragment implements OnRefreshLoadMoreListener {

    private MessageAdapter messageAdapter;
    private List<BHMessage> mList;

    @BindView(R2.id.recycler_message)
    RecyclerView recycler_message;
    @BindView(R2.id.empty_layout)
    EmptyLayout empty_layout;
    @BindView(R2.id.refreshLayout)
    SmartRefreshLayout refreshLayout;


    private int mCurrentPage = 1;
    private MessageViewModel messageViewModel;
    private String mType;

    @Override
    protected void addEvent() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_notification;
    }

    @Override
    protected void initView() {
        /*mList = new ArrayList<>();
        messageViewModel = ViewModelProviders.of(this).get(MessageViewModel.class);

        mType = getArgumentValue(MessageListFragment.Message_Type);
        LogUtils.d("MessageListFragment==>:","MessageListFragment=="+mType);

        LinearLayoutManager llm = new LinearLayoutManager(getYActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_message.setLayoutManager(llm);

        messageAdapter = new MessageAdapter(R.layout.item_message,mList);
        recycler_message.setAdapter(messageAdapter);

        refreshLayout.setOnRefreshLoadMoreListener(this);*/
    }

    private String getArgumentValue(String key){
        String result = "";
        if(getArguments()!=null){
            result = getArguments().getString(key,"0");
        }
        return result;
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        //messageViewModel.loadMessageByAddress(this, mCurrentPage, mType);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mCurrentPage = 1;
       // messageViewModel.loadMessageByAddress(this, mCurrentPage, mType);

    }

    /**
     * 更新message列表
     *
     * @param ldm
     */
    private void updateMessageList(LoadDataModel ldm) {
        if(ldm.getLoadingStatus() == LoadingStatus.SUCCESS){
            BHPage<BHMessage> page = (BHPage) ldm.getData();
            if(mCurrentPage==1){
                messageAdapter.getData().clear();
                mList.clear();
                mList = page.items;
            }
            if(page.items!=null && page.items.size()>0){
                //mList.addAll(page.notifications);
                messageAdapter.addData(page.items);
                mCurrentPage++;
            }
            refreshLayout.finishLoadMore();
            refreshLayout.finishRefresh();
            empty_layout.loadSuccess();
        }else{
            empty_layout.showNeterror(null);
        }
    }
}
