package com.bhex.wallet.balance.ui.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bhex.lib.uikit.widget.editor.SimpleTextWatcher;
import com.bhex.network.mvx.base.BaseDialogFragment;
import com.bhex.tools.utils.PixelUtils;
import com.bhex.wallet.balance.R;
import com.bhex.wallet.balance.adapter.ChooseTokenAdapter;
import com.bhex.wallet.balance.helper.BHBalanceHelper;
import com.bhex.wallet.common.model.BHToken;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

/**
 * 选择币种
 * 2020-12-4 09:36:15
 */
public class ChooseTokenFragment extends BaseDialogFragment {

    private List<BHToken> mDatas;
    private String mSymbol;
    private ChooseTokenAdapter mChooseTokenAdapter;
    private RecyclerView rec_token_list;
    AppCompatImageView iv_close;
    AppCompatEditText input_search_content;
    private OnChooseTokenListener mOnChooseItemListener;
    @Override
    public int getLayout() {
        return R.layout.fragment_choose_token_r;
    }

    @Override
    public void onStart() {
        super.onStart();
        setStyle(DialogFragment.STYLE_NO_TITLE, STYLE_NO_TITLE);

        Window window = getDialog().getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.getAttributes().windowAnimations = R.style.bottomDialogStyle;

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = dm.heightPixels-PixelUtils.dp2px(getContext(),120);
        window.setAttributes(params);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rec_token_list = mRootView.findViewById(R.id.rec_token_list);
        mDatas = BHBalanceHelper.loadTokenList();
        rec_token_list.setAdapter(mChooseTokenAdapter = new ChooseTokenAdapter(mDatas,mSymbol));

        mChooseTokenAdapter.setOnItemClickListener(this::onItemClick);
    }

    private void onItemClick(BaseQuickAdapter<?,?> baseQuickAdapter, View view, int position) {
        BHToken item = mChooseTokenAdapter.getData().get(position);
        if(getDialog()!=null && getDialog().isShowing()){
            dismissAllowingStateLoss();
        }
        if(mOnChooseItemListener==null){
            return;
        }
        mOnChooseItemListener.onChooseClickListener(item.symbol,position);
    }

    @Override
    protected void initView() {
        iv_close = mRootView.findViewById(R.id.iv_close);
        input_search_content = mRootView.findViewById(R.id.input_search_content);
        iv_close.setOnClickListener(v -> {
            dismissAllowingStateLoss();
        });
        input_search_content.addTextChangedListener(mTextWatcher);
    }

    public static ChooseTokenFragment showFragment(String symbol,OnChooseTokenListener listener){
        ChooseTokenFragment fragment = new ChooseTokenFragment();
        fragment.mOnChooseItemListener = listener;
        fragment.mSymbol = symbol;
        return  fragment;
    }

    //设置点击
    private SimpleTextWatcher mTextWatcher = new SimpleTextWatcher(){
        @Override
        public void afterTextChanged(Editable s) {
            String search_key = input_search_content.getText().toString().trim();
            if(TextUtils.isEmpty(search_key)){
                mChooseTokenAdapter.setNewData(mDatas);
               return;
            }

            List<BHToken> result = StreamSupport.stream(mDatas).filter(item->
                    item.symbol.toLowerCase().contains(search_key.toLowerCase())
            ).collect(Collectors.toList());

            mChooseTokenAdapter.setNewData(result);
        }
    };

    public interface OnChooseTokenListener{
        void onChooseClickListener(String symbol,int position);
    }


    public void setOnChooseItemListener(OnChooseTokenListener mOnChooseItemListener) {
        this.mOnChooseItemListener = mOnChooseItemListener;
    }
}