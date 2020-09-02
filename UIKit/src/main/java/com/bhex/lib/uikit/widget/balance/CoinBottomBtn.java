package com.bhex.lib.uikit.widget.balance;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.bhex.lib.uikit.R;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/4/6
 * Time: 22:22
 */
public class CoinBottomBtn extends RelativeLayout {

    private Context mContext;

    private RelativeLayout mRootView;

    public AppCompatImageView iv_coin_icon;

    public AppCompatTextView tv_bottom_text;

    public AppCompatImageView iv_arrow;

    public CoinBottomBtn(Context context) {
        this(context,null);
    }

    public CoinBottomBtn(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CoinBottomBtn(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    /**
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        mRootView = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.layout_coin_bottom_btn,this);
        iv_coin_icon = mRootView.findViewById(R.id.iv_coin_icon);
        tv_bottom_text = mRootView.findViewById(R.id.tv_bottom_text);
        iv_arrow = mRootView.findViewById(R.id.iv_arrow);

        TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.Coin_Button);
        int actionImage = ta.getResourceId(R.styleable.Coin_Button_actionImage,0);
        iv_coin_icon.setImageDrawable(ContextCompat.getDrawable(mContext,actionImage));

        boolean actionMore = ta.getBoolean(R.styleable.Coin_Button_actionMore,false);
        iv_arrow.setVisibility(actionMore?VISIBLE:GONE);
        String actionText = ta.getString(R.styleable.Coin_Button_actionText);
        tv_bottom_text.setText(actionText);
    }


    public void setActionMore(int visibility){
        iv_arrow.setVisibility(visibility);
    }



}
