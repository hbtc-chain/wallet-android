package com.bhex.tools.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;

/**
 * 语言判断
 */
public class ToolUtils {
    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private static final int MIN_CLICK_DELAY_TIME = 1500;
    private static long lastClickTime;
    /**
     * @param context
     * @return
     */
    public static boolean isZh(Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if (language.endsWith("zh"))
            return true;
        else
            return false;
    }

    //关闭软键盘
    public static void hintKeyBoard(Activity context) {
        //拿到InputMethodManager
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        //如果window上view获取焦点 && view不为空
        if (imm.isActive() && context.getCurrentFocus() != null) {
            //拿到view的token 不为空
            if (context.getCurrentFocus().getWindowToken() != null) {
                //表示软键盘窗口总是隐藏，除非开始时以SHOW_FORCED显示。
                imm.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
    //关闭软键盘
    public static void hintKeyBoard(Activity context, View view){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    /**
     * 防快速点击
     * @return
     */
    public static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }

    public static String dateToStamp(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

    /**
     * 粘贴文本
     */
    public static void clipText(EditText editText, Context context){
        try{
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            if(clipboard.hasPrimaryClip() && clipboard.getPrimaryClipDescription().hasMimeType(MIMETYPE_TEXT_PLAIN))
            {
                ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
                if(!TextUtils.isEmpty(item.getText().toString())){
                    String pasteData = item.getText()+"";//获取纯文本数据
                    editText.setText(pasteData);
                    editText.setSelection(pasteData.length());
                }
            }
        }catch (Exception e){

        }

    }

    /**
     * 复制文本内容
     * @param copy
     * @param context
     */
    public static void copyText(String copy, Context context){
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 将文本内容放到系统剪贴板里。
        //cm.setText(((TextView)mLayoutItem1.findViewById(R.id.tv_item_hint)).getText());
        cm.setPrimaryClip(ClipData.newPlainText(null,copy));
        if (cm.hasPrimaryClip()){
            cm.getPrimaryClip().getItemAt(0).getText();
        }
        //cm.setText(textView.getText().toString().trim());
    }


    //生成随机数
    public static int getRandom(int count) {
        return (int) Math.round(Math.random() * (count));
    }

    //生成随机数
    /*public static String getRandomString(int length) {
        StringBuffer sb = new StringBuffer();
        int len = BHConstants.RANDOMSTR.length();
        for (int i = 0; i < length; i++) {
            sb.append(BHConstants.RANDOMSTR.charAt(getRandom(len - 1)));
        }
        return sb.toString();
    }*/
}
