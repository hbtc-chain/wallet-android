package com.bhex.wallet.common.menu;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

import com.bhex.tools.utils.ColorUtil;
import com.bhex.tools.utils.PixelUtils;
import com.bhex.lib.uikit.util.ShapeUtils;
import com.bhex.lib.uikit.widget.RecycleViewDivider;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bhex.wallet.common.R;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class MenuListFragment extends BottomSheetDialogFragment {

    private static final String ARG_ITEM_COUNT = "item_count";
    private View mRootView;
    private RecyclerView mRecyclerView;
    private MenuListAdapter mMenuListAdapter;
    private List<MenuItem> menuItems;
    private MenuListListener menuListListener;

    public static MenuListFragment newInstance(ArrayList<MenuItem> data) {
        final MenuListFragment fragment = new MenuListFragment();
        final Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_ITEM_COUNT, data);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_menu_list, container, false);
        mRootView.findViewById(R.id.btn_cancel).setOnClickListener(v -> {
            dismiss();
        });

        GradientDrawable drawable = ShapeUtils.getRoundRectTopDrawable(PixelUtils.dp2px(getContext(),6), ColorUtil.getColor(getContext(),R.color.app_bg),true,0);
        mRootView.setBackground(drawable);

        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mRecyclerView = mRootView.findViewById(R.id.list);
        menuItems = getArguments().getParcelableArrayList(ARG_ITEM_COUNT);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        RecycleViewDivider itemDecoration = new RecycleViewDivider(
                getContext(),LinearLayoutManager.VERTICAL,
                PixelUtils.dp2px(getContext(),1),
                ColorUtil.getColor(getContext(),R.color.global_divider_color));
        mRecyclerView.addItemDecoration(itemDecoration);
        mMenuListAdapter = new MenuListAdapter(menuItems);
        mRecyclerView.setAdapter(mMenuListAdapter);

        mMenuListAdapter.setOnItemClickListener((adapter, itemView, position) -> {
            if(menuListListener==null){
                return;
            }
            MenuItem item = menuItems.get(position);
            menuListListener.onItemClick(item,itemView,position);
            if(!isHidden()){
                dismiss();
            }
        });
    }

    public interface  MenuListListener{
        public void onItemClick(MenuItem item,View itemView,int position);
    }

    public void setMenuListListener(MenuListListener menuListListener) {
        this.menuListListener = menuListListener;
    }
}