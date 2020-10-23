package com.bhex.lib.uikit.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.bhex.lib.uikit.R;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/3/12
 * Time: 20:57
 */
public class BHToolBar extends Toolbar {

    private View mView;
    private AppCompatTextView mCenterTv;

    private Context mContext;

    public BHToolBar(Context context) {
        this(context,null);
    }

    public BHToolBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,-1);
    }

    public BHToolBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    /**
     * 初始化toolbar
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {
        mContext = context;

        mView = LayoutInflater.from(context).inflate(R.layout.layout_toolbar,null);
        mCenterTv = mView.findViewById(R.id.tv_center_title);

        TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.BH_toolbar);
        String centerTitle = ta.getString(R.styleable.BH_toolbar_centerTitle);
        if(!TextUtils.isEmpty(centerTitle)){
            mCenterTv.setText(centerTitle);
        }
        ta.recycle();
    }


}
