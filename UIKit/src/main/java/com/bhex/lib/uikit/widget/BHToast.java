package com.bhex.lib.uikit.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bhex.lib.uikit.R;
import com.bhex.lib.uikit.util.PixelUtils;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/3/23
 * Time: 12:51
 */
public class BHToast   {

    private Toast mToast;

    public BHToast(Context context, String msg, int bg, int duration) {
        //加载Toast布局
        View toastRoot = LayoutInflater.from(context).inflate(R.layout.bh_toast, null);
        //初始化布局控件
        TextView mTextView = toastRoot.findViewById(R.id.toast_msg);
        //ImageView mImageView =  toastRoot.findViewById(R.id.toast_img);
        //为控件设置属性
        mTextView.setText(msg);
        //mImageView.setImageResource(bg);
        //Toast的初始化
        mToast = new Toast(context);
        mToast.setDuration(duration);
        mToast.setView(toastRoot);

        RelativeLayout relativeLayout = toastRoot.findViewById(R.id.toast_ll);
        //        //获取屏幕高度
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        ViewGroup.LayoutParams layoutParams = relativeLayout.getLayoutParams();
        layoutParams.height =  PixelUtils.dp2px(context, 54);
        layoutParams.width = PixelUtils.dp2px(context, width-32);
        //FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams((int) DpPxUtils.dip2px(context, width), (int) DpPxUtils.dip2px(context, 54));
        relativeLayout.setLayoutParams(layoutParams);
        mToast.setGravity(Gravity.TOP, 0, 0);
    }

    public static BHToast makeTextError(Context context, String text, int color, int bg, int duration) {
        return new BHToast(context, text, bg, duration);
    }

    public void show() {
        if (mToast != null) {
            mToast.show();
        }
    }

}
