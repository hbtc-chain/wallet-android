package com.bhex.lib.uikit.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import com.bhex.lib.uikit.R;
import com.bhex.lib.uikit.widget.load.HwLoadingView;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/4/11
 * Time: 0:45
 */
public class EmptyLayout extends LinearLayout {

    //a
    private ImageView empty_img;
    //b
    private HwLoadingView empty_progress;
    //private ProgressBar empty_progress;
    //e
    private AppCompatButton empty_bt_reload;
    //c
    private TextView empty_title;
    //d
    private TextView empty_sub_title;

    public EmptyLayout(Context context) {
        this(context,null);
    }

    public EmptyLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public EmptyLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initEmptyLayout(context,attrs);
    }

    private void initEmptyLayout(Context context, AttributeSet attrs) {
        LinearLayout.inflate(context, R.layout.empty_layout,this);

        this.empty_img = findViewById(R.id.empty_img);
        this.empty_progress = findViewById(R.id.empty_progress);
        this.empty_bt_reload =findViewById(R.id.empty_bt_reload);
        this.empty_title = findViewById(R.id.empty_title);
        this.empty_sub_title = findViewById(R.id.empty_sub_title);
    }

    public void loadSuccess() {
        setVisibility(View.GONE);
    }

    public void showNeterror(@NonNull onReloadClickListener reloadClickListener) {
        setVisibility(View.VISIBLE);
        this.empty_progress.setVisibility(View.GONE);
        this.empty_img.setVisibility(View.VISIBLE);
        this.empty_img.setImageResource(R.mipmap.ic_net_work_error);
        this.empty_title.setVisibility(View.VISIBLE);
        this.empty_title.setText(getResources().getString(R.string.app_net_error_msg));
        this.empty_sub_title.setVisibility(View.VISIBLE);
        this.empty_sub_title.setText(getResources().getString(R.string.app_net_error_msg_check));
        this.empty_bt_reload.setVisibility(View.VISIBLE);

        empty_bt_reload.setOnClickListener(v -> {
            if(reloadClickListener==null){
                return;
            }
            reloadClickListener.onClick(v);
        });
    }

    public void showNoData() { showNoData("", 0); }

    public void showNoData(String title, @DrawableRes int resId) {
        setVisibility(View.VISIBLE);
        this.empty_progress.setVisibility(View.GONE);
        this.empty_img.setVisibility(View.VISIBLE);
        if (resId == 0) {
            this.empty_img.setImageResource(R.mipmap.ic_order);
        } else {
            this.empty_img.setImageResource(resId);
        }
        this.empty_title.setVisibility(View.GONE);
        this.empty_sub_title.setVisibility(View.VISIBLE);
        if (title != null && title.trim().length() > 0) {
            this.empty_sub_title.setText(title);
        } else {
            this.empty_sub_title.setText(getResources().getString(R.string.app_no_data));
        }
        empty_bt_reload.setVisibility(View.GONE);
    }

    public void showProgess() {
        setVisibility(View.VISIBLE);
        empty_img.setVisibility(View.GONE);
        empty_progress.setVisibility(View.GONE);
        empty_title.setVisibility(View.GONE);
        empty_sub_title.setVisibility(View.GONE);
        empty_progress.setVisibility(View.VISIBLE);
        empty_bt_reload.setVisibility(GONE);
    }

    public static interface onReloadClickListener {
        void onClick(View view);
    }
}
