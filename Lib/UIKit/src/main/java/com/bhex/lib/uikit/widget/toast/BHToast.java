package com.bhex.lib.uikit.widget.toast;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bhex.lib.uikit.R;
import com.bhex.lib.uikit.util.ColorUtil;
import com.bhex.lib.uikit.util.PixelUtils;

/**
 * @author gongdongyang
 * 2020-5-19 12:16:41
 * Toast
 */
public class BHToast {
    private Toast mToast;


    public BHToast(Context context, String msg, int color,  int duration) {
        //加载Toast布局
        View toastRoot = LayoutInflater.from(context).inflate(R.layout.bh_toast, null);
        //初始化布局控件
        TextView mTextView = toastRoot.findViewById(R.id.toast_msg);
        //为控件设置属性
        mTextView.setText(msg);
        mTextView.setTextColor(color);
        //Toast的初始化
        mToast = new Toast(context);
        mToast.setDuration(duration);
        mToast.setView(toastRoot);

        RelativeLayout relativeLayout = toastRoot.findViewById(R.id.toast_ll);
        //获取屏幕高度
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) relativeLayout.getLayoutParams();

        lp.width = width - PixelUtils.dp2px(context,24);

        mToast.setGravity(Gravity.TOP, 0, 0);
    }

    public static BHToast makeText(Context context, String text, int color, int bg, int duration) {
        return new BHToast(context, text, color,duration);
    }

    public static BHToast showDefault(Context context, String text) {
        return new BHToast(context, text,
                ColorUtil.getColor(context,android.R.color.white),Toast.LENGTH_SHORT);

    }

    public void show() {
        if (mToast != null) {
            mToast.show();
        }
    }
}
