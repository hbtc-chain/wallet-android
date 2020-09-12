package com.bhex.wallet.market.ui.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bhex.lib.uikit.util.ColorUtil;
import com.bhex.lib.uikit.util.PixelUtils;
import com.bhex.network.base.LoadDataModel;
import com.bhex.network.base.LoadingStatus;
import com.bhex.network.mvx.base.BaseDialogFragment;
import com.bhex.tools.utils.ShapeUtils;
import com.bhex.wallet.common.db.entity.BHWallet;
import com.bhex.wallet.common.manager.BHUserManager;
import com.bhex.wallet.common.model.BHWalletItem;
import com.bhex.wallet.common.viewmodel.WalletViewModel;
import com.bhex.wallet.market.R;
import com.bhex.wallet.market.R2;
import com.bhex.wallet.market.adapter.BHWalletAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author gongdongyang
 * 2020-9-11 23:17:05
 */
public class ChooseWalletFragment extends BaseDialogFragment implements BHWalletAdapter.OnCheckClickListener {

    @BindView(R2.id.rcv_wallet)
    RecyclerView rcv_wallet;
    @BindView(R2.id.btn_cancel)
    AppCompatTextView btn_cancel;

    private BHWalletAdapter mAllWalletAdapter;

    WalletViewModel walletViewModel;

    private List<BHWalletItem> mAllWalletList;

    @Override
    public int getLayout() {
        return R.layout.fragment_choose_wallet;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.getAttributes().windowAnimations = R.style.bottomDialogStyle;

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = dm.widthPixels;
        window.setAttributes(params);
        walletViewModel = ViewModelProviders.of(this).get(WalletViewModel.class);
        walletViewModel.loadWallet(getActivity());
        walletViewModel.mutableWallentLiveData.observe(this, ldm -> {
            udpatWalletList(ldm, null);
        });
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        GradientDrawable drawable = ShapeUtils.getRoundRectTopDrawable(PixelUtils.dp2px(getContext(), 6), ColorUtil.getColor(getContext(), R.color.app_bg), true, 0);
        mRootView.setBackground(drawable);


        //初始化RecycleView
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcv_wallet.setLayoutManager(layoutManager);

        mAllWalletAdapter = new BHWalletAdapter(mAllWalletList);
        rcv_wallet.setAdapter(mAllWalletAdapter);

        btn_cancel.setOnClickListener(v -> {
            dismiss();
        });
    }


    public void udpatWalletList(LoadDataModel ldm, String flag) {
        if (ldm.getLoadingStatus() == LoadingStatus.SUCCESS) {
            mAllWalletList = getAllBHWalletItem();
            mAllWalletAdapter.getData().clear();
            mAllWalletAdapter.addData(mAllWalletList);
        }
    }

    public static void showDialog(FragmentManager fm, String tag) {
        ChooseWalletFragment fragment = new ChooseWalletFragment();
        fragment.show(fm, tag);
    }

    public List<BHWalletItem> getAllBHWalletItem() {
        List<BHWalletItem> result = null;
        List<BHWallet> origin = BHUserManager.getInstance().getAllWallet();
        if (origin != null && origin.size() > 0) {
            result = new ArrayList<>();
            for (BHWallet tmp : origin) {
                BHWalletItem item = BHWalletItem.makeBHWalletItem(tmp);
                result.add(item);
            }
        }
        return result;
    }

    @Override
    public void checkClickListener(int position, BHWalletItem bhWalletItem) {

    }
}