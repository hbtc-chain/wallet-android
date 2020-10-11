package com.bhex.wallet.market.ui.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bhex.lib.uikit.util.ColorUtil;
import com.bhex.lib.uikit.util.PixelUtils;
import com.bhex.lib.uikit.widget.RecycleViewDivider;
import com.bhex.network.mvx.base.BaseDialogFragment;
import com.bhex.wallet.common.cache.CacheCenter;
import com.bhex.wallet.common.model.BHTokenMapping;
import com.bhex.wallet.market.R;
import com.bhex.wallet.market.R2;
import com.bhex.wallet.market.adapter.ChooseTokenAdapter;

import java.util.List;

import butterknife.BindView;

/**
 * @author gongdongyang
 * 2020-10-10 16:20:03
 */
public class ChooseTokenFragment extends BaseDialogFragment {

    @BindView(R2.id.rec_token_list)
    RecyclerView rec_token_list;
    @BindView(R2.id.iv_close)
    AppCompatImageView iv_close;


    ChooseTokenAdapter mChooseTokenAdapter;

    private List<BHTokenMapping> mDatas;

    private String mSymbol;

    private ChooseTokenListener mChooseTokenListener;

    @Override
    public int getLayout() {
        return R.layout.fragment_choose_token;
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

        params.width = ViewGroup.LayoutParams.MATCH_PARENT;

        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        window.setAttributes(params);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDatas = CacheCenter.getInstance().getTokenMapCache().getTokenMappings();
        mChooseTokenAdapter = new ChooseTokenAdapter(mDatas,mSymbol);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rec_token_list.setLayoutManager(llm);

        RecycleViewDivider itemDecoration = new RecycleViewDivider(
                getContext(),LinearLayoutManager.VERTICAL,
                PixelUtils.dp2px(getContext(),1),
                ColorUtil.getColor(getContext(), com.bhex.wallet.common.R.color.global_divider_color));
        rec_token_list.addItemDecoration(itemDecoration);

        rec_token_list.setAdapter(mChooseTokenAdapter);

        iv_close.setOnClickListener(v -> {
            dismissAllowingStateLoss();
        });

        mChooseTokenAdapter.setOnItemClickListener((adapter, itemView, position) -> {
            dismissAllowingStateLoss();
            if(mChooseTokenListener==null){
                return;
            }
            BHTokenMapping tokenMapping = mDatas.get(position);
            mChooseTokenListener.clickTokenPosition(tokenMapping);
        });
    }

    public static void showDialog(FragmentManager fm, String tag,String symbol,ChooseTokenListener listener) {
        ChooseTokenFragment fragment = new ChooseTokenFragment();
        fragment.mSymbol = symbol;
        fragment.mChooseTokenListener = listener;
        fragment.show(fm, tag);
    }

    public interface ChooseTokenListener{
        void clickTokenPosition(BHTokenMapping position);
    }

}