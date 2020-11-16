package com.bhex.wallet.bh_main.proposals.adapter;

import com.bhex.wallet.bh_main.R;
import com.bhex.wallet.bh_main.proposals.model.ProposalInfo;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by BHEX.
 * User: zhouchang
 * Date: 2020/4/21
 */
public class ProposalAdapter extends BaseQuickAdapter<ProposalInfo, BaseViewHolder> {

    public ProposalAdapter(int layoutResId, @Nullable List<ProposalInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder viewHolder, @Nullable ProposalInfo proposalInfo) {
        viewHolder.setText(R.id.tv_id, getContext().getResources().getString(R.string.proposal_id_format, proposalInfo.getId()));

        viewHolder.setText(R.id.tv_proposal_name, proposalInfo.getTitle());
        viewHolder.setText(R.id.tv_proposal_desc, proposalInfo.getDescription());

        if (proposalInfo.getStatus() == 1) {
            viewHolder.setText(R.id.tv_status, getContext().getResources().getString(R.string.proposal_in_payment));
            viewHolder.setTextColor(R.id.tv_status, getContext().getResources().getColor(R.color.proposal_in_payment));
            viewHolder.setImageResource(R.id.iv_status, R.drawable.ic_pledge_collection);
        } else if (proposalInfo.getStatus() == 2) {
            viewHolder.setText(R.id.tv_status, getContext().getResources().getString(R.string.proposal_voting));
            viewHolder.setTextColor(R.id.tv_status, getContext().getResources().getColor(R.color.proposal_voting));
            viewHolder.setImageResource(R.id.iv_status, R.mipmap.icon_proposal_voting);
        } else if (proposalInfo.getStatus() == 3) {
            viewHolder.setText(R.id.tv_status, getContext().getResources().getString(R.string.proposal_passed));
            viewHolder.setTextColor(R.id.tv_status, getContext().getResources().getColor(R.color.proposal_passed));
            viewHolder.setImageResource(R.id.iv_status, R.drawable.ic_proposal_passed);
        } else if (proposalInfo.getStatus() == 4) {
            viewHolder.setText(R.id.tv_status, getContext().getResources().getString(R.string.proposal_reject));
            viewHolder.setTextColor(R.id.tv_status, getContext().getResources().getColor(R.color.proposal_reject));
            viewHolder.setImageResource(R.id.iv_status, R.drawable.ic_proposal_reject);
        } else if (proposalInfo.getStatus() == 5) {
            viewHolder.setText(R.id.tv_status, getContext().getResources().getString(R.string.proposal_failed));
            viewHolder.setTextColor(R.id.tv_status, getContext().getResources().getColor(R.color.proposal_failed));
            viewHolder.setImageResource(R.id.iv_status, R.drawable.ic_proposal_failed);
        }


    }

}
